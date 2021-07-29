package com.fzw.redispubsubdemo;

import com.fzw.redispubsubdemo.util.RedisUtil;

import java.util.UUID;

/**
 * @author fzw
 * @description
 **/
public class Test3 {
    public static void main(String[] args) {
        String channel = "order_store";
        String orderId = UUID.randomUUID().toString();
        RedisUtil.pub(channel, orderId);
    }
}
