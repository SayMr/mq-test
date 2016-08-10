package com.cnten.mq.test06;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ReceiverB  implements Runnable  {

	public static void main(String[] args) {
		ReceiverB receive1 = new ReceiverB();	
		Thread thread1 = new Thread(receive1);
		thread1.start();
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
					destination = session.createQueue("order_queue");
					consumer = session.createConsumer(destination,"JMSXGroupID='B'");
					consumer.setMessageListener(new MessageListener(){

						@Override
						public void onMessage(Message message) {
							TextMessage textMessage = (TextMessage) message;  
				             try {  
				                 System.out.println("B��"+textMessage.getText());  
				             } catch (JMSException e) {  
				                 e.printStackTrace();  
				             }  
						}
						
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
	}
	
}