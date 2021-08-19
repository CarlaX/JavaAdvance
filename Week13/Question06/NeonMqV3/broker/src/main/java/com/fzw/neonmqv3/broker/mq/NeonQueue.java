package com.fzw.neonmqv3.broker.mq;

import com.fzw.neonmqv3.common.NeonMessage;
import lombok.NoArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fzw
 * @description
 * @date 2021-08-19
 **/
@NoArgsConstructor
public class NeonQueue {

    private String topic;
    private NeonMessage<?>[] messages;
    private int capacity;
    private int write;
    private ConcurrentHashMap<String, Integer> readMap;

    public NeonQueue(String topic) {
        this.topic = topic;
        this.capacity = 1024;
        this.messages = new NeonMessage[this.capacity];
        this.readMap = new ConcurrentHashMap<>();
        this.write = 0;
    }

    private boolean isFull() {
        return this.write == this.capacity;
    }

    //    生产者对于同一个队列是同步写入
    public synchronized int write(NeonMessage<?> message) {
        if (!this.isFull()) {
            this.messages[this.write] = message;
            this.write++;
            return this.write - 1;
        }
        return -1;
    }

    //    不同消费者之间对于同一个队列可以并发消费
    public NeonMessage<?> read(String name) {
        int read = this.readMap.getOrDefault(name, 0);

        if (read == this.write) {
            return null;
        }

        NeonMessage<?> message = this.messages[read];
        if (read == 0) {
            this.readMap.put(name, read);
        }

        return message;
    }

    //    假设每个消费者自身是顺序消费的，只有当前一条消息被确认后才能消费下一条，否则始终获取的是之前的消息
    public void ack(String name) {
        Integer read = this.readMap.get(name);
        if (read == null) {
            throw new RuntimeException("consumer 不存在");
        }
        read++;
        this.readMap.put(name, read);
    }

    public String getTopic() {
        return topic;
    }

    public int getCapacity() {
        return capacity;
    }
}
