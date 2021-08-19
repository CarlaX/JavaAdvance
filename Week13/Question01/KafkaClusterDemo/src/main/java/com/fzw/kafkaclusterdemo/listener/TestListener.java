package com.fzw.kafkaclusterdemo.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;

/**
 * @author fzw
 * @description
 * @date 2021-08-19
 **/
@Slf4j
public class TestListener implements MessageListener<Object, Object> {
    @Override
    public void onMessage(ConsumerRecord<Object, Object> data) {
        log.info("{}", data.value());
        log.info("{}", data);
    }
}
