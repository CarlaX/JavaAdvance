package com.fzw.redisdemo.sync.util;

import java.io.File;
import java.net.URL;
import java.util.concurrent.*;

/**
 * @author fzw
 * @description 分布式锁
 **/
public class RedisLock {


    private static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE;
    private static final String LOCK_PREFIX;
    private static String LOCK_LUA_SHA = null;
    private static String UN_LOCK_LUA_SHA = null;
    private static final ConcurrentHashMap<String, ScheduledFuture<?>> SCHEDULED_THREAD_MAP;
    private static final ConcurrentHashMap<String, Semaphore> BLOCK_LOCK;

    //    延期定时器
    private static class ExpireCheckThread implements Runnable {
        private final String key;
        private final long expire;

        public ExpireCheckThread(String key, long expire) {
            this.key = key;
            this.expire = expire;
        }

        @Override
        public void run() {
            RedisUtil.expire(key, expire);
        }
    }

    static {
        LOCK_PREFIX = "lock.";
        SCHEDULED_THREAD_MAP = new ConcurrentHashMap<>();
        BLOCK_LOCK = new ConcurrentHashMap<>();
        SCHEDULED_EXECUTOR_SERVICE = Executors.newScheduledThreadPool(16);
        scriptInit();
    }

    //    脚本初始化
    public static void scriptInit() {
        if (LOCK_LUA_SHA == null) {
            URL url0 = RedisLock.class.getClassLoader().getResource("lua/lock.lua");
            assert url0 != null;
            LOCK_LUA_SHA = RedisUtil.load(new File(url0.getPath()));
        }
        if (UN_LOCK_LUA_SHA == null) {
            URL url1 = RedisLock.class.getClassLoader().getResource("lua/unlock.lua");
            assert url1 != null;
            UN_LOCK_LUA_SHA = RedisUtil.load(new File(url1.getPath()));
        }
    }

    public static boolean lock(String lock, String value, long expire) {
        String key = LOCK_PREFIX + lock;
        BLOCK_LOCK.putIfAbsent(key, new Semaphore(0, true));
        long count = tryLock(value, expire, key);
        System.out.println("first try : " + count);
        if (count < 0) {
            try {
                RedisUtil.sub(key);
                while (true) {
                    count = tryLock(value, expire, key);
                    if (count > 0) {
                        break;
                    }
                    BLOCK_LOCK.get(key).acquire();
                    System.out.println("stop wait");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                RedisUtil.unSub(key);
            }
        }
        return count > 0;
    }

    private static long tryLock(String value, long expire, String key) {
        long count = RedisUtil.eval(LOCK_LUA_SHA, new String[]{key}, new String[]{value, String.valueOf(expire)});
        if (count == 1) {
            long delay = Math.max(expire - 5L, 1L);
            ExpireCheckThread expireCheckThread = new ExpireCheckThread(key, expire);
            ScheduledFuture<?> scheduledFuture = SCHEDULED_EXECUTOR_SERVICE.scheduleWithFixedDelay(expireCheckThread, delay, delay, TimeUnit.SECONDS);
            SCHEDULED_THREAD_MAP.putIfAbsent(key, scheduledFuture);
        }
        return count;
    }

    public static boolean unlock(String lock, String value) {
        String key = LOCK_PREFIX + lock;
        long count = RedisUtil.eval(UN_LOCK_LUA_SHA, new String[]{key}, new String[]{value});
        if (count == 0) {
            SCHEDULED_THREAD_MAP.remove(key).cancel(true);
            RedisUtil.unSub(key);
            long pub = RedisUtil.pub(key, key);
            if (pub == 0) {
                RedisLock.await(key);
            }
        }
        return count >= 0;
    }

    public static void await(String key) {
        BLOCK_LOCK.get(key).release();
    }

    public static void shutdown() {
        SCHEDULED_EXECUTOR_SERVICE.shutdown();
    }
}
