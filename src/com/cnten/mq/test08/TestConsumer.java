package com.cnten.mq.test08;

import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Map;

import org.apache.activemq.transport.stomp.StompConnection;
import org.apache.activemq.transport.stomp.StompFrame;



public class TestConsumer {
	public static void main(String[] args) throws Exception {
        // ��������
        StompConnection con = new StompConnection();
        Socket so = new Socket("127.0.0.1", 61613);
        con.open(so);
        con.setVersion("1.2");
        con.connect("admin", "admin");

        String ack = "client";
        con.subscribe("/test", "client");
        // ������Ϣ��ʹ��ѭ�����У�
        for(;;) {
            StompFrame frame = null;
            try {
                // ע�⣬���û�н��յ���Ϣ��
                // ����������̻߳�ͣ�����ֱ�����εȴ���ʱ
                frame = con.receive();
            } catch(SocketTimeoutException e) {
                continue;
            }

            // ��ӡ���ν��յ�����Ϣ
            System.out.println("frame.getAction() = " + frame.getAction());
            Map<String, String> headers = frame.getHeaders();
            String meesage_id = headers.get("message-id");
            System.out.println("frame.getBody() = " + frame.getBody());
            System.out.println("frame.getCommandId() = " + frame.getCommandId());

            // ��ack��client��ǵ�����£�ȷ����Ϣ
            if("client".equals(ack)) {
                con.ack(meesage_id);
            }
        }
    }
}
