package com.fzw.redispubsubdemo.util;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.List;

/**
 * @author fzw
 * @description
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

    public static long pub(String channel, String key) {
        return REDIS_PUB_SUB_COMMANDS.publish(channel, key);
    }

    public static void sub(String... channel) {
        REDIS_PUB_SUB_COMMANDS.subscribe(channel);
    }

    public static void unSub(String... channel) {
        REDIS_PUB_SUB_COMMANDS.unsubscribe(channel);
    }
}
