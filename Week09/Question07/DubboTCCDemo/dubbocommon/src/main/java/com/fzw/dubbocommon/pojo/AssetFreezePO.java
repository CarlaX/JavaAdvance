package com.fzw.dubbocommon.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author fzw
 * @description
 * @date 2021-07-06
 **/
@NoArgsConstructor
@Data
public class AssetFreezePO {
    private String id;
    private String userId;
    private BigDecimal amount;
    private String type;

    public AssetFreezePO(String id, String sellerId, BigDecimal amount, String type) {
        this.id = id;
        this.userId = sellerId;
        this.amount = amount;
        this.type = type;
    }
}
