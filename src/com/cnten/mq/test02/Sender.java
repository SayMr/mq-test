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
		
		///�������ô���
		/*BrokerService broker = new BrokerService(); 
		// configure the broker
		try {
			broker.addConnector("tcp://localhost:61616");
			broker.start();
		} catch (Exception e1) {
			e1.printStackTrace();
		}*/
		 
		
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
		
		//��������ΪObjectMessage���͵���Ϣʱ��Ҫ�������ΰ���
		//((ActiveMQConnectionFactory)connectionFactory).setTrustAllPackages(true);
		((ActiveMQConnectionFactory)connectionFactory).setTrustedPackages(Arrays.asList("com.cnten.mq.test02"));
		
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

	public static void sendMessage(Session session, MessageProducer producer) throws Exception {
		for (int i = 1; i <= SEND_NUMBER; i++) {
			TextMessage message = session.createTextMessage("ActiveMq(test02) ���͵���Ϣ" + i);
			// ������Ϣ��Ŀ�ĵط�
			System.out.println("������Ϣ��" + "ActiveMq(test02) ���͵���Ϣ" + i);
			producer.send(message);
			
		}
		
		//��������ΪMapMessage����Ϣ
		MapMessage message2 = session.createMapMessage();
		message2.setString("name", "����");
		message2.setInt("age", 23);
		message2.setObject("hobby", Arrays.asList("��Ӿ","ͽ��"));
		System.out.println("������Ϣ ��name:"+message2.getString("name")+" age:"+message2.getInt("age")+ " hobby��"+message2.getObject("hobby"));
        producer.send(message2);
		
		//��������ΪObjectMessage����Ϣ
		ObjectMessage message3 = session.createObjectMessage();
		Person person = new Person("����",25);
		message3.setObject(person);
		System.out.println("������Ϣ��"+person);
		producer.send(message3);
		
		//��������ΪObjectMessage����Ϣ����Ϣ��ΪHashMap
		HashMap<String,String> map=new HashMap<String, String>();
        map.put("param1", "����");
        map.put("param2", "����");
        map.put("param3", "�Ա�");
        ObjectMessage message4=session.createObjectMessage(map);
        System.out.println("������Ϣ��"+map);
		producer.send(message4);
		
		//��������ΪObjectMessage����Ϣ����Ϣ��ΪList
		List<String> list = Arrays.asList("����","����","ƹ����");
		ObjectMessage message5 = session.createObjectMessage((Serializable)list);
		System.out.println("������Ϣ��"+list);
        producer.send(message5);
        
        
        //��������ΪObjectMessage����Ϣ����ϢΪHashMap��ListǶ��
        HashMap<String,List<String>> hm = new HashMap<String,List<String>>();
        List<String> list2 = Arrays.asList("����","����","ƹ����");
        hm.put("�ҵİ���", list2);
        ObjectMessage message6 = session.createObjectMessage(hm);
        System.out.println("������Ϣ��"+hm);
		producer.send(message6);
		
        //��������BytesMessage����Ϣ
        byte[] bs = "bytes message".getBytes();  
        BytesMessage message7 = session.createBytesMessage();  
        message7.writeBytes(bs);
        for(int i=0; i< 5; i++){ 
        	message7.setLongProperty("sendTime", System.currentTimeMillis());
        	producer.send(message7);
        }
	}
}
