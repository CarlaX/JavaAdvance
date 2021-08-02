package com.fzw.activemqdemo;

import org.apache.activemq.artemis.api.core.TransportConfiguration;
import org.apache.activemq.artemis.api.jms.ActiveMQJMSClient;
import org.apache.activemq.artemis.api.jms.JMSFactoryType;
import org.apache.activemq.artemis.core.remoting.impl.netty.NettyConnectorFactory;
import org.apache.activemq.artemis.jms.client.*;

import javax.jms.*;
import java.util.Enumeration;

/**
 * @author fzw
 * @description queue
 **/
public class Test {

    public static void main(String[] args) throws JMSException, InterruptedException {
        TransportConfiguration transportConfiguration = new TransportConfiguration(NettyConnectorFactory.class.getName());

        ActiveMQConnectionFactory activeMQConnectionFactory = ActiveMQJMSClient.createConnectionFactoryWithoutHA(JMSFactoryType.CF, transportConfiguration);

        JMSContext jmsContext = activeMQConnectionFactory.createContext(Session.AUTO_ACKNOWLEDGE);

        Queue queue = jmsContext.createQueue("test_queue");

        JMSConsumer jmsConsumer = jmsContext.createConsumer(queue);
        JMSConsumer jmsConsumer_2 = jmsContext.createConsumer(queue);

        ConnectionMetaData metaData = jmsContext.getMetaData();

        Enumeration<?> jmsxPropertyNames = metaData.getJMSXPropertyNames();
        while (jmsxPropertyNames.hasMoreElements()) {
            Object o = jmsxPropertyNames.nextElement();
            System.out.println(o.getClass().getName());
            System.out.println(o);
        }

        String providerVersion = metaData.getProviderVersion();
        int providerMajorVersion = metaData.getProviderMajorVersion();
        int providerMinorVersion = metaData.getProviderMinorVersion();

        System.out.println(providerVersion + " : " + providerMajorVersion + " : " + providerMinorVersion);

        String jmsVersion = metaData.getJMSVersion();
        int jmsMajorVersion = metaData.getJMSMajorVersion();
        int jmsMinorVersion = metaData.getJMSMinorVersion();

        System.out.println(jmsVersion + " : " + jmsMajorVersion + " : " + jmsMinorVersion);

        boolean autoStart = jmsContext.getAutoStart();
        System.out.println("auto start : " + autoStart);


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
        Thread.sleep(5000L);
        jmsProducer.send(queue, textMessage);
//        String body = jmsConsumer.receiveBody(String.class);
//        System.out.println(body);
        Thread.currentThread().join(5000L);

    }
}
