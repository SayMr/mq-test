package com.cnten.mq.test08;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class JMSProducer {
	/**
     * 由于是测试代码，这里忽略了异常处理。
     * 正是代码可不能这样做
     * @param args
     * @throws RuntimeException
     */
    public static void main (String[] args) throws Exception {
        // 定义JMS-ActiveMQ连接信息（默认为Openwire协议）
        ///ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        // 定义JMS-ActiveMQ连接信息 (使用auto关键字自动匹配合适的协议格式)
        //ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61617");
    	//使用BIO网络IO模型
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61608");

        Session session = null;
        Destination sendQueue;
        Connection connection = null;

        //进行连接
        connection = connectionFactory.createQueueConnection();
        connection.start();

        //建立会话（设置一个带有事务特性的会话）
        session = connection.createSession(true, Session.SESSION_TRANSACTED);
        //建立queue（当然如果有了就不会重复建立）
        sendQueue = session.createQueue("/test");
        //建立消息发送者对象
        MessageProducer sender = session.createProducer(sendQueue);
        TextMessage outMessage = session.createTextMessage();
        outMessage.setText("这是发送的消息内容");

        //发送（JMS是支持事务的）
        sender.send(outMessage);
        session.commit();

        //关闭
        sender.close();
        connection.close();
    }
}
