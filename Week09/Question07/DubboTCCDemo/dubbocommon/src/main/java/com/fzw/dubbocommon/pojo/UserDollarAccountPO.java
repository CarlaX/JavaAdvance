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
public class UserDollarAccountPO {
    private String id;
    private String username;
    private BigDecimal dollarAmount;
}
