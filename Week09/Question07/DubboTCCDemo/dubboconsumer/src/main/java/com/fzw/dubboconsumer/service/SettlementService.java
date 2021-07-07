package com.fzw.dubboconsumer.service;

import java.math.BigDecimal;

/**
 * @author fzw
 * @description
 * @date 2021-07-06
 **/
public interface SettlementService {

    public boolean doSettlement(String xid, String buyerId, String sellerId, BigDecimal amount);

}
