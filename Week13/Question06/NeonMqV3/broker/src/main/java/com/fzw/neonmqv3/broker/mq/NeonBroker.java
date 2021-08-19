package com.fzw.neonmqv3.broker.mq;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fzw
 * @description
 * @date 2021-08-19
 **/
public class NeonBroker {
    public static final NeonBroker DEFAULT = new NeonBroker();
    private final ConcurrentHashMap<String, NeonQueue> TOPICS;


    public NeonBroker() {
        this.TOPICS = new ConcurrentHashMap<>();
    }

    public NeonQueue createTopic(String topic) {
        NeonQueue neonQueue = new NeonQueue(topic);
        NeonQueue neonQueue_pre = this.TOPICS.putIfAbsent(topic, neonQueue);
        return neonQueue_pre == null ? neonQueue : neonQueue_pre;
    }

    public NeonQueue getTopic(String topic) {
        return this.TOPICS.get(topic);
    }
}
