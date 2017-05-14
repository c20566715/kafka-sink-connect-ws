package com.websockets.connectors;

import com.websockets.utils.MetaData;

public class KafkaConnector {
	private Connector connect = null;

	public Connector connect(MetaData metaData) {
		if (metaData.getSchemaUrl() != null) {

		} else {
			connect = new KafkaJsonConnect();
		}
		connect.connect();
		return connect;
	}

}
