package com.fzw.redisdemo.sync.util;

import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands;

import java.io.*;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.List;

/**
 * @author fzw
 * @description redis工具类
 **/
public class RedisUtil {

    private static final RedisURI REDIS_URI;
    private static final RedisClient REDIS_CLIENT;
    private static final StatefulRedisConnection<String, String> STATEFUL_REDIS_CONNECTION;
    private static final RedisCommands<String, String> REDIS_COMMANDS;
    private static final StatefulRedisPubSubConnection<String, String> STATEFUL_REDIS_PUB_SUB_CONNECTION;
    private static final RedisPubSubCommands<String, String> REDIS_PUB_SUB_COMMANDS;
    private static final LockPubSubListener LOCK_PUB_SUB_LISTENER;

    static {
        REDIS_URI = RedisURI.builder().withHost("127.0.0.1").withPort(6379).build();
        REDIS_CLIENT = RedisClient.create(REDIS_URI);
        STATEFUL_REDIS_CONNECTION = REDIS_CLIENT.connect();
        STATEFUL_REDIS_PUB_SUB_CONNECTION = REDIS_CLIENT.connectPubSub();
        LOCK_PUB_SUB_LISTENER = new LockPubSubListener();
        STATEFUL_REDIS_PUB_SUB_CONNECTION.addListener(LOCK_PUB_SUB_LISTENER);
        REDIS_COMMANDS = STATEFUL_REDIS_CONNECTION.sync();
        REDIS_PUB_SUB_COMMANDS = STATEFUL_REDIS_PUB_SUB_CONNECTION.sync();
    }

    public static boolean set(String key, String value) {
        String reply = REDIS_COMMANDS.set(key, value);
        return "OK".equals(reply);
    }

    public static String get(String key) {
        return REDIS_COMMANDS.get(key);
    }

    public static String load(File file) {
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader);) {
            StringBuilder script = new StringBuilder();
            CharBuffer charBuffer = CharBuffer.allocate(1024);
            int limit = -1;
            while ((limit = bufferedReader.read(charBuffer)) > 0) {
                charBuffer.flip();
                script.append(charBuffer, 0, limit);
                charBuffer.flip();
            }
            charBuffer.limit(0);
            charBuffer = null;
            return REDIS_COMMANDS.scriptLoad(script.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long eval(String digest, String[] key, String[] value) {
        List<Boolean> booleans = REDIS_COMMANDS.scriptExists(digest);
        Object evalsha = REDIS_COMMANDS.evalsha(digest, ScriptOutputType.INTEGER, key, value);
        return Long.parseLong(String.valueOf(evalsha));
    }

    public static boolean expire(String key, long expire) {
        return REDIS_COMMANDS.pexpire(key, expire);
    }

    public static long pub(String channel, String key) {
        return REDIS_PUB_SUB_COMMANDS.publish(channel, key);
    }

    public static void sub(String... channel) {
        REDIS_PUB_SUB_COMMANDS.subscribe(channel);
    }

    public static void unSub(String... channel) {
        REDIS_PUB_SUB_COMMANDS.unsubscribe(channel);
    }

    public static boolean flush() {
        String flush = REDIS_COMMANDS.scriptFlush();
        return "OK".equals(flush);
    }
}
