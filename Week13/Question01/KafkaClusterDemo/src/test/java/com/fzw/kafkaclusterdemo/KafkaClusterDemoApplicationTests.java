package com.fzw.kafkaclusterdemo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@SpringBootTest
public class KafkaClusterDemoApplicationTests {

    @Autowired
    private KafkaAdmin kafkaAdmin;

    @Autowired
    private KafkaTemplate<Object, Object> kafkaTemplate;

    @Test
    public void sendToTopic() throws InterruptedException {
        log.info("{},{}", this.kafkaAdmin == null, this.kafkaTemplate == null);
        log.info("{}", this.kafkaTemplate.getDefaultTopic());
        this.kafkaTemplate.send("testNeon", "hello world");
        Thread.sleep(30000);
    }

}
