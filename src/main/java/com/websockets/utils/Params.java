package com.websockets.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.java_websocket.WebSocket;

import com.websockets.connectors.KafkaConnector;

public class Params {

	public static Map<String, Map<String, Set<WebSocket>>> subscriber = new HashMap<String, Map<String, Set<WebSocket>>>();
	public static Map<String, Map<String, KafkaConnector>> connectGroupMap = new HashMap<String, Map<String, KafkaConnector>>();
	public static Map<WebSocket, String> subscriberMetaData = new HashMap<WebSocket, String>();
}
