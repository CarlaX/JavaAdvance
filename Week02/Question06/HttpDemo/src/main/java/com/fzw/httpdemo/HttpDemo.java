package com.fzw.httpdemo;

import java.util.Map;

/**
 * @author fzw
 * @description
 **/
public class HttpDemo {
    public static void main(String[] args) {
        String response = HttpUtil.basicGet(Map.of(), "http://localhost:8801", null, null);
    }
}
