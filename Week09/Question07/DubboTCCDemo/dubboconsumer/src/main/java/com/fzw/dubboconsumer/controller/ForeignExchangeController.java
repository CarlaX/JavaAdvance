package com.fzw.dubboconsumer.controller;

import com.fzw.dubbocommon.pojo.ResultVO;
import com.fzw.dubboconsumer.service.SettlementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author fzw
 * @description
 * @date 2021-07-06
 **/
@Slf4j
@RestController
@RequestMapping(path = "/foreignExchange")
public class ForeignExchangeController {

    @Autowired
    private SettlementService settlementService;

    @PostMapping(path = "/doDollarExchange")
    public ResultVO doDollarExchange(@RequestParam("buyerId") String buyerId, @RequestParam("sellerId") String sellerId, @RequestParam("amount") BigDecimal amount) {
        log.info("{},{},{}", buyerId, sellerId, amount);
        UUID uuid = UUID.randomUUID();
        boolean settlement = this.settlementService.doSettlement(uuid.toString(), buyerId, sellerId, amount);
        if (settlement) {
            log.info("success");
            return new ResultVO(100, "交易成功");
        }
        log.info("fail");
        return new ResultVO(200, "交易失败");
    }

}
