package com.fzw.activemqdemo;

import org.apache.activemq.artemis.api.core.TransportConfiguration;
import org.apache.activemq.artemis.api.jms.ActiveMQJMSClient;
import org.apache.activemq.artemis.api.jms.JMSFactoryType;
import org.apache.activemq.artemis.core.remoting.impl.netty.NettyConnectorFactory;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author fzw
 * @description topic
 **/
public class Test2 {

    public static void main(String[] args) throws JMSException, InterruptedException {
        TransportConfiguration transportConfiguration = new TransportConfiguration(NettyConnectorFactory.class.getName());

        ActiveMQConnectionFactory activeMQConnectionFactory = ActiveMQJMSClient.createConnectionFactoryWithoutHA(JMSFactoryType.CF, transportConfiguration);

        JMSContext jmsContext = activeMQConnectionFactory.createContext(Session.AUTO_ACKNOWLEDGE);

        Topic topic = jmsContext.createTopic("test_topic");

        JMSConsumer jmsConsumer = jmsContext.createConsumer(topic);
        JMSConsumer jmsConsumer_2 = jmsContext.createConsumer(topic);

        MessageListener messageListener = new MessageListener() {
            @Override
            public void onMessage(Message message) {
                String body = null;
                try {
                    String jmsMessageID = message.getJMSMessageID();
                    System.out.println("listener jmsMessageID : " + jmsMessageID);
                    body = message.getBody(String.class);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
                System.out.println(body);
            }
        };

        jmsConsumer.setMessageListener(messageListener);
        jmsConsumer_2.setMessageListener(messageListener);

        JMSProducer jmsProducer = jmsContext.createProducer();

        TextMessage textMessage = jmsContext.createTextMessage("你好，世界");
        String jmsMessageID = textMessage.getJMSMessageID();
        System.out.println("before jmsMessageID : " + jmsMessageID);
        Thread.sleep(1000L);
        jmsProducer.send(topic, textMessage);

        Thread.currentThread().join(5000L);
//        String body = jmsConsumer.receiveBody(String.class);
//        System.out.println(body);
    }
}
