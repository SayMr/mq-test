package com.cnten.mq.test08;

import java.net.Socket;
import java.util.Date;

import org.apache.activemq.transport.stomp.StompConnection;

public class TestProducer {
	public static void main(String[] args) {
        try {
            // ����StompЭ�������
            StompConnection con = new StompConnection();
            Socket so = new Socket("127.0.0.1", 61613);
            con.open(so);
            // ע�⣬Э��汾������1.2��Ҳ������1.1
            con.setVersion("1.2");
            // �û��������룬������ض�˵��
            con.connect("admin", "admin");

            // ���·���һ����Ϣ����Ҳ����ʹ�á����񡱷�ʽ��
            con.send("/test", "234543" + new Date().getTime());
        } catch(Exception e) {
            e.printStackTrace(System.out);
        }
    }
}
