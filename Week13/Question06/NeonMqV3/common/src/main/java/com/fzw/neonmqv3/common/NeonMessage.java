package com.fzw.neonmqv3.common;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

/**
 * @author fzw
 * @description
 * @date 2021-08-19
 **/
@NoArgsConstructor
@Data
//@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class NeonMessage<T> {
    private HashMap<String, String> headers;
    private T body;

    public NeonMessage(HashMap<String, String> headers, T body) {
        this.headers = headers;
        this.body = body;
    }
}
