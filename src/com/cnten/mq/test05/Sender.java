package com.cnten.mq.test05;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Sender implements Runnable {
	private static final int SEND_NUMBER = 5;
	
	private String threadName;
	
	Sender(String threadName) {
		this.threadName = threadName;
	}

	public static void main(String[] args) {
		Sender sender1 = new Sender("thread1");
		Sender sender2 = new Sender("thread2");
		Sender sender3 = new Sender("thread3");	
		Thread thread1 = new Thread(sender1);
		Thread thread2 = new Thread(sender2);
		Thread thread3 = new Thread(sender3);
		thread1.start();
		thread2.start();
		thread3.start();
	}

	public void sendMessage(Session session, MessageProducer producer) throws Exception {
		for (int i = 1; i <= SEND_NUMBER; i++) {
			TextMessage message = session.createTextMessage("�����߳�" + threadName +"���͵���Ϣ" + i);
			// ������Ϣ��Ŀ�ĵط�
			System.out.println("�����߳�" + threadName +"������Ϣ��" + i);
			producer.send(message);
		}
	}

	@Override
	public void run() {
		// ConnectionFactory �����ӹ�����JMS ������������
		ConnectionFactory connectionFactory;
		// Connection ��JMS �ͻ��˵�JMS Provider ������
		Connection connection = null;
		// Session�� һ�����ͻ������Ϣ���߳�
		Session session;
		// Destination ����Ϣ��Ŀ�ĵ�;��Ϣ���͸�˭.
		Destination destination;
		// MessageProducer����Ϣ������
		MessageProducer producer;
		// TextMessage message;
		// ����ConnectionFactoryʵ�����󣬴˴�����ActiveMq��ʵ��jar
		connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD, "tcp://localhost:61616");
		try {
			// ����ӹ����õ����Ӷ���
			connection = connectionFactory.createConnection();
			// ����
			connection.start();
			// ��ȡ��������
			session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
			// ��ȡsessionע�����ֵxingbo.xu-queue��һ����������queue��������ActiveMq��console����
			destination = session.createQueue("FirstQueue");
			// �õ���Ϣ�����ߡ������ߡ�
			producer = session.createProducer(destination);
			// ���ò��־û����˴�ѧϰ��ʵ�ʸ�����Ŀ����
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			// ������Ϣ���˴�д������Ŀ���ǲ��������߷�����ȡ
			sendMessage(session, producer);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != connection)
					connection.close();
			} catch (Throwable ignore) {
			}
		}
	}
	
}
