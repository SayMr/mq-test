package com.cnten.mq.test08;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class JMSConsumer {
	/**
     * �����ǲ��Դ��룬����������쳣����
     * ���Ǵ���ɲ���������
     * @param args
     * @throws RuntimeException
     */
    public static void main (String[] args) throws Exception {
        // ����JMS-ActiveMQ������Ϣ
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        Session session = null;
        Destination sendQueue;
        Connection connection = null;

        //��������
        connection = connectionFactory.createQueueConnection();
        connection.start();

        //�����Ự(����Ϊ�Զ�ack)
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //����Queue����Ȼ������˾Ͳ����ظ�������
        sendQueue = session.createQueue("/test");
        //������Ϣ�����߶���
        MessageConsumer consumer = session.createConsumer(sendQueue);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message arg0) {
                // ���յ���Ϣ�󣬲���Ҫ�ٷ���ack�ˡ�
                System.out.println("Message = " + arg0);
            }
        });

        synchronized (JMSConsumer.class) {
            JMSConsumer.class.wait();
        }

        //�ر�
        consumer.close();
        connection.close();
    }
}
