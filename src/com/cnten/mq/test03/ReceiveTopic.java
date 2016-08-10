package com.cnten.mq.test03;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ReceiveTopic implements Runnable{
	private String threadName;
	ReceiveTopic(String threadName) {
		this.threadName = threadName;
	}

	@Override
	public void run() {
		// ConnectionFactory�����ӹ�����JMS������������
		ConnectionFactory connectionFactory;
		// Connection��JMS�ͻ��˵�JMS Provider������
		Connection connection = null;
		// Session��һ�����ͻ������Ϣ���߳�
		Session session;
		// Destination����Ϣ��Ŀ�ĵ�;��Ϣ���͸�˭.
		Destination destination;
		// �����ߣ���Ϣ������
		MessageConsumer consumer;
		connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD, "tcp://127.0.0.1:61616");
		try {
			// ����ӹ����õ����Ӷ���
			connection = connectionFactory.createConnection();
			// ����
			connection.start();
			// ��ȡ��������,Ĭ���Զ�����������ͽ��ճɹ�����Ӧ
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			// ��ȡsessionע�����ֵFirstTopic��һ����������topic
			destination = session.createTopic("FirstTopic");
			consumer = session.createConsumer(destination);
			while (true) {
				// ���ý����߽�����Ϣ��ʱ�䣬Ϊ�˱��ڲ��ԣ������趨Ϊ100s
				TextMessage message = (TextMessage) consumer
						.receive(100 * 1000);
				if (null != message) {
					System.out.println("�߳�" + threadName + "�յ���Ϣ:" + message.getText());
					Thread.sleep(10000);
				} else {
					continue;
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

	public static void main(String[] args) {
		// ��������3���߳�������FirstTopic����Ϣ����queue�ķ�ʽ��һ�������̶߳����յ�ͬ������Ϣ
		ReceiveTopic receive1 = new ReceiveTopic("thread1");
		ReceiveTopic receive2 = new ReceiveTopic("thread2");
		ReceiveTopic receive3 = new ReceiveTopic("thread3");
		Thread thread1 = new Thread(receive1);
		Thread thread2 = new Thread(receive2);
		Thread thread3 = new Thread(receive3);
		thread1.start();
		thread2.start();
		thread3.start();
	}

}