package com.fzw.jdbccrud.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fzw
 * @description
 * @date 2021-06-07
 **/
@NoArgsConstructor
@Data
public class OrderPO {
    private Integer id;
    private String name;
    private Integer amount;
}
