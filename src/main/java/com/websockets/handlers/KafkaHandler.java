package com.websockets.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.websockets.connectors.KafkaConnector;
import com.websockets.utils.MetaData;
import com.websockets.utils.Params;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class KafkaHandler extends WebSocketServer {

	public KafkaHandler(int port) {
		super(new InetSocketAddress(port));
	}

	public KafkaHandler(InetSocketAddress address) {
		super(address);
	}

	private ObjectMapper mapper = new ObjectMapper();

	@Override
	public void onOpen(WebSocket subscriber, ClientHandshake handshake) {

	}

	@Override
	public void onClose(WebSocket subscriber, int code, String reason, boolean remote) {
		String topicConsumerGroup = Params.subscriberMetaData.get(subscriber);
		String topic = topicConsumerGroup.split("-")[0];
		String consumerGroup = topicConsumerGroup.split("-")[1];
		Params.subscriber.get(consumerGroup).get(topic).remove(subscriber);
		subscriber.close();
	}

	@Override
	public void onMessage(WebSocket subscriber, String message) {
		MetaData metaData = null;
		try {
			metaData = mapper.readValue(message, MetaData.class);
			String consumerGroup = metaData.getConsumerGroup();
			String topic = metaData.getTopic();

			if (!Params.subscriber.containsKey(consumerGroup)) {
				Params.subscriber.put(consumerGroup, new HashMap<String, Set<WebSocket>>());

				Params.connectGroupMap.put(consumerGroup, new HashMap<String, KafkaConnector>());
			}

			if (!Params.subscriber.get(consumerGroup).containsKey(topic)) {
				Params.subscriber.get(consumerGroup).put(topic, new HashSet<WebSocket>());

				KafkaConnector kafkaConnect = new KafkaConnector();
				kafkaConnect.connect(metaData);

				Params.connectGroupMap.get(consumerGroup).put(topic, kafkaConnect);

			} else {
				Params.connectGroupMap.get(consumerGroup).get(topic).connect(metaData);
			}

			Set<WebSocket> subscribers = Params.subscriber.get(consumerGroup).get(topic);
			subscribers.add(subscriber);

			Params.subscriber.get(consumerGroup).put(topic, subscribers);
			Params.subscriberMetaData.put(subscriber, topic + "-" + consumerGroup);

			System.out.println("Number of Subscribers : " + Params.subscriber.get(consumerGroup).get(topic).size());
		} catch (Exception e) {
		}

	}

	@Override
	public void onError(WebSocket conn, Exception ex) {

	}

}
