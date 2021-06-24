package com.fzw.shardingspherespringdemo.pojo.po;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fzw
 * @description
 * @date 2021-06-24
 **/
@NoArgsConstructor
@Data
public class UserPO {
    private Integer id;
    private String username;
    private String password;
}
