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

public class ReceiverA  implements Runnable  {

	public static void main(String[] args) {
		ReceiverA receive1 = new ReceiverA();	
		Thread thread1 = new Thread(receive1);
		thread1.start();
	}
	
	@Override
	public void run() {
		// ConnectionFactory ：连接工厂，JMS 用它创建连接
				ConnectionFactory connectionFactory;
				// Connection ：JMS 客户端到JMS Provider 的连接
				Connection connection = null;
				// Session： 一个发送或接收消息的线程
				Session session;
				// Destination ：消息的目的地;消息发送给谁.
				Destination destination;
				// 消费者，消息接收者
				MessageConsumer consumer;
				connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
						ActiveMQConnection.DEFAULT_PASSWORD, "tcp://localhost:61616");
				try {
					// 构造从工厂得到连接对象
					connection = connectionFactory.createConnection();
					// 启动
					connection.start();
					// 获取操作连接
					session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
					// 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
					destination = session.createQueue("order_queue");
					consumer = session.createConsumer(destination,"JMSXGroupID='A'");
					consumer.setMessageListener(new MessageListener(){

						@Override
						public void onMessage(Message message) {
							TextMessage textMessage = (TextMessage) message;  
				             try {  
				                 System.out.println("A："+textMessage.getText());  
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