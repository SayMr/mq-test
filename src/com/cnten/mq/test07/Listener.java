package com.cnten.mq.test07;

import java.util.List;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.util.ByteSequence;

public class Listener implements MessageListener {

	public void onMessage(Message message) {  
        try { 
        	if(message instanceof TextMessage){
        		TextMessage txtMsg = (TextMessage)message;
        		System.out.println("test02---收到消息" + txtMsg.getText());
        	}
        	if(message instanceof MapMessage) {
        		MapMessage map = (MapMessage)message;
        		String name = map.getString("name");
        		Integer age = map.getInt("age");
        		List list = (List) map.getObject("hobby");
        		System.out.println("test02---收到消息 (MapMessage类型)"+"name:"+name+" age:"+age+ " hobby:"+list);
        	}
        	if(message instanceof ObjectMessage) {
        		ObjectMessage objMsg = (ObjectMessage)message;
        		Object obj = objMsg.getObject();
        		System.out.println("收到的消息："+obj);
        	}
        	
        	if(message instanceof ActiveMQBytesMessage) {
        		ActiveMQBytesMessage bytesMsg = (ActiveMQBytesMessage)message;
        		 long sendTime = bytesMsg.getLongProperty("sendTime");  
        		 System.out.println("sendtime:" + sendTime);  
        		 ByteSequence bs = bytesMsg.getMessage().getContent();  
        		 System.out.println("bytes data:" + new String(bs.getData()));  
        	}
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  

}
