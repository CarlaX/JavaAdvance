package com.fzw.redisdemo.sync.service;

import com.fzw.redisdemo.sync.util.RedisLock;

/**
 * @author fzw
 * @description 分布式计数器
 **/
public class DistributedTimer {

    private long stock;
    private String key;

    public DistributedTimer(long stock, String key) {
        this.stock = stock;
        this.key = key;
    }

    public long increase(String id, long expire) {
        RedisLock.lock(this.key, id, expire);
        this.stock++;
        RedisLock.unlock(this.key, id);
        return this.stock;
    }

    public long decrease(String id, long expire) {
        RedisLock.lock(this.key, id, expire);
        this.stock--;
        RedisLock.unlock(this.key, id);
        return this.stock;
    }

}
