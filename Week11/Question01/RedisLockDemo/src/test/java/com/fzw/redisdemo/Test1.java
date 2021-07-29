package com.fzw.redisdemo;

import com.fzw.redisdemo.sync.util.RedisLock;
import com.fzw.redisdemo.sync.util.RedisUtil;

import java.util.UUID;

/**
 * @author fzw
 * @description
 **/
public class Test1 {

    public static void main(String[] args) throws InterruptedException {
        boolean flush = RedisUtil.flush();
        System.out.println(flush);

        RedisUtil.set("carla", "-1");
        final int[] sum = {0};

        for (int i = 0; i < 20; i++) {
            int finalI = i;
            Thread thread = new Thread() {
                @Override
                public void run() {
                    String uuid = UUID.randomUUID().toString();
                    RedisLock.lock("neon", uuid, 10L);
                    String value = RedisUtil.get("carla");

                    System.out.println(finalI + " : " + value + " : " + uuid);

                    RedisUtil.set("carla", String.valueOf(sum[0]));
                    sum[0]++;
                    RedisLock.unlock("neon", uuid);
                }
            };
            thread.start();
        }
    }
}
