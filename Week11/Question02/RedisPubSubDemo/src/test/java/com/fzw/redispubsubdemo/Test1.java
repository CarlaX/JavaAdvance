package com.fzw.redispubsubdemo;

import com.fzw.redispubsubdemo.util.RedisUtil;

/**
 * @author fzw
 * @description
 **/
public class Test1 {
    public static void main(String[] args) throws InterruptedException {
        String channel = "order_store";
        RedisUtil.sub(channel);
        Thread.currentThread().join();
    }
}
