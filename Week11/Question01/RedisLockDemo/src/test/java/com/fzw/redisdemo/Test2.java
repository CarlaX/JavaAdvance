package com.fzw.redisdemo;

import com.fzw.redisdemo.sync.service.DistributedTimer;
import com.fzw.redisdemo.sync.util.RedisLock;

import java.util.Random;
import java.util.UUID;

/**
 * @author fzw
 * @description
 **/
public class Test2 {
    public static void main(String[] args) throws InterruptedException {
        DistributedTimer distributedTimer = new DistributedTimer(100, "stock");
        Random random = new Random();

        for (int i = 1; i <= 20; i++) {
            int finalI = i;
            Thread thread = new Thread() {
                @Override
                public void run() {
                    String uuid = UUID.randomUUID().toString();

                    if (finalI <= 10) {
                        long increase = distributedTimer.increase(uuid, 10L);
                        System.out.println("increase : " + increase);
                    } else {
                        long decrease = distributedTimer.decrease(uuid, 10L);
                        System.out.println("decrease : " + decrease);
                    }
                }
            };
            thread.start();
        }
        Thread.currentThread().join(5000L);
        System.out.println("shutdown");
        RedisLock.shutdown();
    }
}
