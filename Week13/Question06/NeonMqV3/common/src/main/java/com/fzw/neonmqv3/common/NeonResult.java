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
public class NeonResult<T> {
    private Integer code;
    private String msg;
    private T data;

    public NeonResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
