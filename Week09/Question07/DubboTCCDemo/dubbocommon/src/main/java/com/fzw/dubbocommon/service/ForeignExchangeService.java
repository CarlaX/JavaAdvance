package com.fzw.dubbocommon.service;

import java.math.BigDecimal;

/**
 * @author fzw
 * @description
 * @date 2021-07-06
 **/
public interface ForeignExchangeService {

    public String dollarExchangeOut(String sellerId, BigDecimal dollarAmount);

    public String dollarExchangeIn(String buyerId, BigDecimal dollarAmount);

    public void dollarExchangeOutFinish(String sellerId, BigDecimal dollarAmount, String assetsId);

    public void dollarExchangeInFinish(String buyerId, BigDecimal dollarAmount, String assetsId);

    public void dollarExchangeOutRe(String sellerId, String assetsId);

    public void dollarExchangeInRe(String buyerId, String assetsId);

}
