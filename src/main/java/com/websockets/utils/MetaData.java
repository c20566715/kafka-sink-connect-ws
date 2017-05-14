package com.websockets.utils;

public class MetaData {
	private Boolean fromBeginning;
	private String messageType;
	private String consumerGroup;
	private String topic;
	private String kafkaUrl;
	private String zookeeperUrl;
	private String schemaUrl;

	public Boolean getFromBeginning() {
		return fromBeginning;
	}

	public String getMessageType() {
		return messageType;
	}

	public String getConsumerGroup() {
		return consumerGroup;
	}

	public String getTopic() {
		return topic;
	}

	public String getKafkaUrl() {
		return kafkaUrl;
	}

	public String getZookeeperUrl() {
		return zookeeperUrl;
	}

	public String getSchemaUrl() {
		return schemaUrl;
	}
}
