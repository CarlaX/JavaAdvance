package com.fzw.insertdemo.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author fzw
 * @description
 **/
@NoArgsConstructor
@Data
public class OrderPO {
    private Integer id;
    private Integer userId;
    private BigDecimal totalPrice;
    private Integer totalAmount;
    private LocalDateTime createTime;
    private LocalDateTime payTime;
    private LocalDateTime finishTime;

    public OrderPO(Integer userId, BigDecimal totalPrice, Integer totalAmount, LocalDateTime createTime, LocalDateTime payTime, LocalDateTime finishTime) {
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.totalAmount = totalAmount;
        this.createTime = createTime;
        this.payTime = payTime;
        this.finishTime = finishTime;
    }
}
