package com.websockets.utils;

public class MetaData {
	private Boolean fromBeginning;
	private String messageType;
	private String consumerGroup;
	private String topic;

	public void setFromBeginning(Boolean fromBeginning) {
		this.fromBeginning = fromBeginning;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public void setConsumerGroup(String consumerGroup) {
		this.consumerGroup = consumerGroup;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public void setKafkaUrl(String kafkaUrl) {
		this.kafkaUrl = kafkaUrl;
	}

	public void setZookeeperUrl(String zookeeperUrl) {
		this.zookeeperUrl = zookeeperUrl;
	}

	public void setSchemaUrl(String schemaUrl) {
		this.schemaUrl = schemaUrl;
	}

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
