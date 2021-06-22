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
public class CommodityPO {
    private Integer id;
    private String name;
    private BigDecimal price;

    public CommodityPO(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }
}
