package com.cnten.mq.test05;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Receiver  implements Runnable  {
	
	private String threadName;
	
	Receiver(String threadName) {
		this.threadName = threadName;
	}
	public static void main(String[] args) {
		// ��������3���߳�������FirstTopic����Ϣ����queue�ķ�ʽ��һ�������̶߳����յ�ͬ������Ϣ
		Receiver receive1 = new Receiver("thread1");
		Receiver receive2 = new Receiver("thread2");
		Receiver receive3 = new Receiver("thread3");	
		Thread thread1 = new Thread(receive1);
		Thread thread2 = new Thread(receive2);
		Thread thread3 = new Thread(receive3);
		thread1.start();
		thread2.start();
		thread3.start();
		
		
		Receiver receive4 = new Receiver("thread4");
		Receiver receive5 = new Receiver("thread5");
		Receiver receive6 = new Receiver("thread6");
		
		Thread thread4= new Thread(receive4);
		Thread thread5 = new Thread(receive5);
		Thread thread6 = new Thread(receive6);
		thread4.start();
		thread5.start();
		thread6.start();
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
				// �����ߣ���Ϣ������
				MessageConsumer consumer;
				connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
						ActiveMQConnection.DEFAULT_PASSWORD, "tcp://localhost:61616");
				try {
					// ����ӹ����õ����Ӷ���
					connection = connectionFactory.createConnection();
					// ����
					connection.start();
					// ��ȡ��������
					session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
					// ��ȡsessionע�����ֵxingbo.xu-queue��һ����������queue��������ActiveMq��console����
					destination = session.createQueue("FirstQueue");
					consumer = session.createConsumer(destination);
					while (true) {
						// ���ý����߽�����Ϣ��ʱ�䣬Ϊ�˱��ڲ��ԣ�����˭��Ϊ100s
						TextMessage message = (TextMessage) consumer.receive(500000);
						if (null != message) {
							System.out.println("�����߳�" + threadName + "�յ�" + message.getText());
						} else {
							break;
						}
					}
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