package com.fzw.kafkaclusterdemo.config;

import com.fzw.kafkaclusterdemo.listener.TestListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;

/**
 * @author fzw
 * @description
 * @date 2021-08-19
 **/
@Configuration
public class KafkaConfig {

    @Bean
    public KafkaMessageListenerContainer<Object, Object> testContainer(ConsumerFactory<Object, Object> consumerFactory) {
        ContainerProperties containerProperties = new ContainerProperties("testNeon");
        TestListener testListener = new TestListener();
        containerProperties.setMessageListener(testListener);
        containerProperties.setGroupId("test");
        return new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
    }
}
