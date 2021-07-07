package com.fzw.dubboprovider.impl;

import com.fzw.dubbocommon.annotation.DynamicDatasource;
import com.fzw.dubbocommon.pojo.AssetFreezePO;
import com.fzw.dubbocommon.pojo.UserDollarAccountPO;
import com.fzw.dubbocommon.pojo.UserRmbAccountPO;
import com.fzw.dubbocommon.service.ForeignExchangeService;
import com.fzw.dubbocommon.config.DynamicRoutingDatasource;
import com.fzw.dubboprovider.mapper.AssetsFreezeMapper;
import com.fzw.dubboprovider.mapper.DollarAccountMapper;
import com.fzw.dubboprovider.mapper.RmbAccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author fzw
 * @description
 * @date 2021-07-06
 **/
@Slf4j
@DubboService
public class ForeignExchangeServiceImpl implements ForeignExchangeService {

    @Autowired
    private RmbAccountMapper rmbAccountMapper;

    @Autowired
    private DollarAccountMapper dollarAccountMapper;

    @Autowired
    private AssetsFreezeMapper assetsFreezeMapper;

    @DynamicDatasource(DynamicRoutingDatasource.MASTER_KEY)
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    @Override
    public String dollarExchangeOut(String sellerId, BigDecimal dollarAmount) {
        log.info("dollarExchangeOut");
        UUID uuid = UUID.randomUUID();
        UserDollarAccountPO userDollarAccountPO = this.dollarAccountMapper.selectById(sellerId);
        if (userDollarAccountPO.getDollarAmount().compareTo(dollarAmount) < 0) {
            return null;
        }
        this.dollarAccountMapper.minusDollarAccount(sellerId, dollarAmount);
        AssetFreezePO assetFreezePO = new AssetFreezePO(uuid.toString(), sellerId, dollarAmount, "dollar");
        this.assetsFreezeMapper.addAssetsFreeze(assetFreezePO);
        return uuid.toString();
    }

    @DynamicDatasource(DynamicRoutingDatasource.SLAVE_KEY)
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    @Override
    public String dollarExchangeIn(String buyerId, BigDecimal dollarAmount) {
        UUID uuid = UUID.randomUUID();

        UserRmbAccountPO userRmbAccountPO = this.rmbAccountMapper.selectById(buyerId);
        if (userRmbAccountPO.getRmbAmount().compareTo(dollarAmount.multiply(new BigDecimal("7"))) < 0) {
            return null;
        }
        this.rmbAccountMapper.minusRmbAccount(buyerId, dollarAmount.multiply(new BigDecimal("7")));
        AssetFreezePO assetFreezePO = new AssetFreezePO(uuid.toString(), buyerId, dollarAmount, "rmb");
        this.assetsFreezeMapper.addAssetsFreeze(assetFreezePO);
        return uuid.toString();
    }

    @DynamicDatasource(DynamicRoutingDatasource.MASTER_KEY)
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    @Override
    public void dollarExchangeOutFinish(String sellerId, BigDecimal dollarAmount, String assetsId) {
        if (assetsId != null) {
            this.rmbAccountMapper.plusRmbAccount(sellerId, dollarAmount.multiply(new BigDecimal("7")));
            this.assetsFreezeMapper.removeAssetsFreeze(assetsId);
        }
    }

    @DynamicDatasource(DynamicRoutingDatasource.SLAVE_KEY)
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    @Override
    public void dollarExchangeInFinish(String buyerId, BigDecimal dollarAmount, String assetsId) {
        if (assetsId != null) {
            this.dollarAccountMapper.plusDollarAccount(buyerId, dollarAmount);
            this.assetsFreezeMapper.removeAssetsFreeze(assetsId);
        }
    }

    @DynamicDatasource(DynamicRoutingDatasource.MASTER_KEY)
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    @Override
    public void dollarExchangeOutRe(String sellerId, String assetsId) {
        AssetFreezePO assetFreezePO = this.assetsFreezeMapper.selectById(assetsId);
        if (assetFreezePO != null) {
            this.assetsFreezeMapper.removeAssetsFreeze(assetsId);
            BigDecimal amount = assetFreezePO.getAmount();
            this.dollarAccountMapper.plusDollarAccount(sellerId, amount);
        }
    }

    @DynamicDatasource(DynamicRoutingDatasource.SLAVE_KEY)
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    @Override
    public void dollarExchangeInRe(String buyerId, String assetsId) {
        AssetFreezePO assetFreezePO = this.assetsFreezeMapper.selectById(assetsId);
        if (assetFreezePO != null) {
            this.assetsFreezeMapper.removeAssetsFreeze(assetsId);
            BigDecimal amount = assetFreezePO.getAmount();
            this.rmbAccountMapper.minusRmbAccount(buyerId, amount.multiply(new BigDecimal("7")));
        }
    }
}
