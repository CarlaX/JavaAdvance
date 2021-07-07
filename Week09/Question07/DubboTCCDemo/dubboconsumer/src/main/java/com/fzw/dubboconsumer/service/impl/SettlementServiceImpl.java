package com.fzw.dubboconsumer.service.impl;

import com.fzw.dubbocommon.service.ForeignExchangeService;
import com.fzw.dubboconsumer.service.SettlementService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.dromara.hmily.annotation.HmilyTCC;
import org.dromara.hmily.common.exception.HmilyRuntimeException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author fzw
 * @description
 * @date 2021-07-06
 **/
@Slf4j
@Service
public class SettlementServiceImpl implements SettlementService {
    @DubboReference
    private ForeignExchangeService foreignExchangeService;

    private static final ConcurrentMap<String, String> sellerAssets = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, String> buyerAssets = new ConcurrentHashMap<>();

    @HmilyTCC(confirmMethod = "doSettlementConfirm", cancelMethod = "doSettlementCancel")
    @Override
    public boolean doSettlement(String xid, String buyerId, String sellerId, BigDecimal amount) {
        log.info("doSettlement: {},{},{}", buyerId, sellerId, amount);
        String out = foreignExchangeService.dollarExchangeOut(sellerId, amount);
        if (out != null) {
            sellerAssets.put(xid, out);
        }
        String in = foreignExchangeService.dollarExchangeIn(buyerId, amount);
        if (in != null) {
            buyerAssets.put(xid, in);
        }
        if (out == null || in == null) {
            throw new HmilyRuntimeException("交易失败");
        }
        return true;
    }

    public boolean doSettlementConfirm(String xid, String buyerId, String sellerId, BigDecimal amount) {
        log.info("doSettlementConfirm: {},{},{}", buyerId, sellerId, amount);
        log.info("doSettlementConfirm: {},{}", sellerAssets.get(xid), buyerAssets.get(xid));
        try {
            this.foreignExchangeService.dollarExchangeOutFinish(sellerId, amount, sellerAssets.get(xid));
            this.foreignExchangeService.dollarExchangeInFinish(buyerId, amount, buyerAssets.get(xid));
        } finally {
            clearAssetsId(xid);
        }
        return true;
    }

    public boolean doSettlementCancel(String xid, String buyerId, String sellerId, BigDecimal amount) {
        try {
            log.info("doSettlementCancel: {},{},{}", buyerId, sellerId, amount);
            log.info("doSettlementCancel: {},{}", sellerAssets.get(xid), buyerAssets.get(xid));
            if (sellerAssets.get(xid) != null) {
                this.foreignExchangeService.dollarExchangeOutRe(sellerId, sellerAssets.get(xid));
            }
            if (buyerAssets.get(xid) != null) {
                this.foreignExchangeService.dollarExchangeInRe(buyerId, buyerAssets.get(xid));
            }
        } finally {
            clearAssetsId(xid);
        }
        return false;
    }

    private void clearAssetsId(String xid) {
        sellerAssets.remove(xid);
        buyerAssets.remove(xid);
    }
}
