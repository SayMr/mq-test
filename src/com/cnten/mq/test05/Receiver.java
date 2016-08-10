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
		// 这里启动3个线程来监听FirstTopic的消息，与queue的方式不一样三个线程都能收到同样的消息
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
					destination = session.createQueue("FirstQueue");
					consumer = session.createConsumer(destination);
					while (true) {
						// 设置接收者接收消息的时间，为了便于测试，这里谁定为100s
						TextMessage message = (TextMessage) consumer.receive(500000);
						if (null != message) {
							System.out.println("接收线程" + threadName + "收到" + message.getText());
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