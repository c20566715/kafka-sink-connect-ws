package com.websockets.sink;

import org.java_websocket.WebSocketImpl;

import com.websockets.handlers.KafkaHandler;

public class Main {

	public static void main(String[] args) {
		try {
			WebSocketImpl.DEBUG = true;
			int port = 9093; // 843 flash policy port
			try {
				port = Integer.parseInt(args[0]);
			} catch (Exception ex) {
			}
			KafkaHandler s = new KafkaHandler(port);
			s.start();
			System.out.println("KafkaProducerHandler started on port: " + s.getPort());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
