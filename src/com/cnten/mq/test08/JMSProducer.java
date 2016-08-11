package com.cnten.mq.test08;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class JMSProducer {
	/**
     * �����ǲ��Դ��룬����������쳣����
     * ���Ǵ���ɲ���������
     * @param args
     * @throws RuntimeException
     */
    public static void main (String[] args) throws Exception {
        // ����JMS-ActiveMQ������Ϣ��Ĭ��ΪOpenwireЭ�飩
        ///ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        // ����JMS-ActiveMQ������Ϣ (ʹ��auto�ؼ����Զ�ƥ����ʵ�Э���ʽ)
        //ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61617");
    	//ʹ��BIO����IOģ��
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61608");

        Session session = null;
        Destination sendQueue;
        Connection connection = null;

        //��������
        connection = connectionFactory.createQueueConnection();
        connection.start();

        //�����Ự������һ�������������ԵĻỰ��
        session = connection.createSession(true, Session.SESSION_TRANSACTED);
        //����queue����Ȼ������˾Ͳ����ظ�������
        sendQueue = session.createQueue("/test");
        //������Ϣ�����߶���
        MessageProducer sender = session.createProducer(sendQueue);
        TextMessage outMessage = session.createTextMessage();
        outMessage.setText("���Ƿ��͵���Ϣ����");

        //���ͣ�JMS��֧������ģ�
        sender.send(outMessage);
        session.commit();

        //�ر�
        sender.close();
        connection.close();
    }
}
