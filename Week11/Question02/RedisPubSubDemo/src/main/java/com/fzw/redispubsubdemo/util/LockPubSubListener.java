package com.fzw.redispubsubdemo.util;

import io.lettuce.core.pubsub.RedisPubSubAdapter;

/**
 * @author fzw
 * @description
 **/
public class LockPubSubListener extends RedisPubSubAdapter<String, String> {
    @Override
    public void message(String channel, String message) {
        System.out.println("message channel : " + channel + "; message : " + message);
    }

    @Override
    public void message(String pattern, String channel, String message) {
        System.out.println("message pattern : " + pattern + "; channel : " + channel + "; message : " + message);
    }

    @Override
    public void subscribed(String channel, long count) {
        System.out.println("subscribed channel : " + channel + "; count : " + count);
    }

    @Override
    public void psubscribed(String pattern, long count) {
        System.out.println("psubscribed pattern : " + pattern + "; count : " + count);
    }

    @Override
    public void unsubscribed(String channel, long count) {
        System.out.println("unsubscribed channel : " + channel + "; count : " + count);
    }

    @Override
    public void punsubscribed(String pattern, long count) {
        System.out.println("punsubscribed pattern : " + pattern + "; count : " + count);
    }
}
