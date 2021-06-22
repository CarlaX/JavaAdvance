package com.fzw.insertdemo.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author fzw
 * @description
 **/
@NoArgsConstructor
@Data
public class OrderCommodityPO {
    private Integer id;
    private Integer orderId;
    private Integer commodityId;
    private String commodityName;
    private BigDecimal commodityPrice;
    private Integer commodityAmount;

    public OrderCommodityPO(Integer orderId, Integer commodityId, String commodityName, BigDecimal commodityPrice, Integer commodityAmount) {
        this.orderId = orderId;
        this.commodityId = commodityId;
        this.commodityName = commodityName;
        this.commodityPrice = commodityPrice;
        this.commodityAmount = commodityAmount;
    }
}
