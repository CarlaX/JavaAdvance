package com.fzw.neonmqv3.common;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fzw
 * @description
 * @date 2021-08-19
 **/
@NoArgsConstructor
@Data
//@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class UserInfo {
    private String name;
    private Integer age;

    public UserInfo(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
