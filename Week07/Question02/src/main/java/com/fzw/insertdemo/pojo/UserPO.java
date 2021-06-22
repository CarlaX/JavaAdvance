package com.fzw.insertdemo.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fzw
 * @description
 **/
@NoArgsConstructor
@Data
public class UserPO {
    private Integer id;
    private String username;
    private String password;

    public UserPO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
