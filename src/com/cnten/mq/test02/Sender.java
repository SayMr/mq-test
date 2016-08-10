package com.cnten.mq.test02;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Sender {
	private static final int SEND_NUMBER = 5;

	public static void main(String[] args) {
		
		///创建内置代理
		/*BrokerService broker = new BrokerService(); 
		// configure the broker
		try {
			broker.addConnector("tcp://localhost:61616");
			broker.start();
		} catch (Exception e1) {
			e1.printStackTrace();
		}*/
		 
		
		// ConnectionFactory ：连接工厂，JMS 用它创建连接
		ConnectionFactory connectionFactory;
		// Connection ：JMS 客户端到JMS Provider 的连接
		Connection connection = null;
		// Session： 一个发送或接收消息的线程
		Session session;
		// Destination ：消息的目的地;消息发送给谁.
		Destination destination;
		// MessageProducer：消息发送者
		MessageProducer producer;
		// TextMessage message;
		// 构造ConnectionFactory实例对象，此处采用ActiveMq的实现jar
		connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD, "tcp://localhost:61616");
		
		//发送类型为ObjectMessage类型的消息时需要设置信任包名
		//((ActiveMQConnectionFactory)connectionFactory).setTrustAllPackages(true);
		((ActiveMQConnectionFactory)connectionFactory).setTrustedPackages(Arrays.asList("com.cnten.mq.test02"));
		
		try {
			// 构造从工厂得到连接对象
			connection = connectionFactory.createConnection();
			// 启动
			connection.start();
			// 获取操作连接
			session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
			// 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
			destination = session.createQueue("FirstQueue");
			// 得到消息生成者【发送者】
			producer = session.createProducer(destination);
			// 设置不持久化，此处学习，实际根据项目决定
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			// 构造消息，此处写死，项目就是参数，或者方法获取
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

	public static void sendMessage(Session session, MessageProducer producer) throws Exception {
		for (int i = 1; i <= SEND_NUMBER; i++) {
			TextMessage message = session.createTextMessage("ActiveMq(test02) 发送的消息" + i);
			// 发送消息到目的地方
			System.out.println("发送消息：" + "ActiveMq(test02) 发送的消息" + i);
			producer.send(message);
			
		}
		
		//发送类型为MapMessage的消息
		MapMessage message2 = session.createMapMessage();
		message2.setString("name", "李明");
		message2.setInt("age", 23);
		message2.setObject("hobby", Arrays.asList("游泳","徒步"));
		System.out.println("发送消息 ：name:"+message2.getString("name")+" age:"+message2.getInt("age")+ " hobby："+message2.getObject("hobby"));
        producer.send(message2);
		
		//发送类型为ObjectMessage的消息
		ObjectMessage message3 = session.createObjectMessage();
		Person person = new Person("张三",25);
		message3.setObject(person);
		System.out.println("发送消息："+person);
		producer.send(message3);
		
		//发送类型为ObjectMessage的消息，消息体为HashMap
		HashMap<String,String> map=new HashMap<String, String>();
        map.put("param1", "姓名");
        map.put("param2", "年龄");
        map.put("param3", "性别");
        ObjectMessage message4=session.createObjectMessage(map);
        System.out.println("发送消息："+map);
		producer.send(message4);
		
		//发送类型为ObjectMessage的消息，消息体为List
		List<String> list = Arrays.asList("篮球","足球","乒乓球");
		ObjectMessage message5 = session.createObjectMessage((Serializable)list);
		System.out.println("发送消息："+list);
        producer.send(message5);
        
        
        //发送类型为ObjectMessage的消息，消息为HashMap和List嵌套
        HashMap<String,List<String>> hm = new HashMap<String,List<String>>();
        List<String> list2 = Arrays.asList("篮球","足球","乒乓球");
        hm.put("我的爱好", list2);
        ObjectMessage message6 = session.createObjectMessage(hm);
        System.out.println("发送消息："+hm);
		producer.send(message6);
		
        //发送类型BytesMessage的消息
        byte[] bs = "bytes message".getBytes();  
        BytesMessage message7 = session.createBytesMessage();  
        message7.writeBytes(bs);
        for(int i=0; i< 5; i++){ 
        	message7.setLongProperty("sendTime", System.currentTimeMillis());
        	producer.send(message7);
        }
	}
}
