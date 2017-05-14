package com.websockets.connectors;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.java_websocket.WebSocket;

import com.websockets.utils.MetaData;
import com.websockets.utils.Params;

public class KafkaJsonConnect implements Connector {

	private MetaData metaDataLocal = null;
	private Random consumerPicker = new Random();

	private StreamsConfig streamConfig = new StreamsConfig(
			getProperties(metaDataLocal.getKafkaUrl(), metaDataLocal.getZookeeperUrl(),
					metaDataLocal.getConsumerGroup(), metaDataLocal.getFromBeginning(), metaDataLocal.getSchemaUrl()));

	private KafkaStreams kafkaStreams = null;

	public void onMessageReceivedFromTopic(Object message, MetaData metaData) {
		WebSocket subscriber = new ArrayList<>(
				Params.subscriber.get(metaData.getConsumerGroup()).get(metaData.getTopic()))
						.get(consumerPicker.nextInt(Params.subscriber.get(metaData.getConsumerGroup()).size()));

		subscriber.send(message.toString());
	}

	@Override
	public void connect() {

		KStreamBuilder kStreamBuilder = new KStreamBuilder();
		KStream<String, String> kstream = kStreamBuilder.stream(Serdes.String(), Serdes.String(),
				metaDataLocal.getTopic());
		kstream.mapValues(new ValueMapper<String, String>() {
			@Override
			public String apply(String value) {
				onMessageReceivedFromTopic(value, metaDataLocal);
				return null;
			}
		});

		kafkaStreams = new KafkaStreams(kStreamBuilder, streamConfig);
		kafkaStreams.start();

	}

	@Override
	public void close() {
		if(kafkaStreams !=null)
			kafkaStreams.close();
	}

	public Properties getProperties(String kafkaUrl, String zookeeper, String consumerGroup, Boolean fromBeginning,
			String schemaUrl) {
		Properties props = new Properties();
		String processorJob = UUID.randomUUID().toString();

		props.put(StreamsConfig.CLIENT_ID_CONFIG, processorJob);
		props.put("group.id", consumerGroup);
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, processorJob);
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl);
		props.put(StreamsConfig.ZOOKEEPER_CONNECT_CONFIG, zookeeper);
		props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, "1");

		if (fromBeginning) {
			props.put("auto.offset.reset", "earliest");
		} else {
			props.put("auto.offset.reset", "latest");
		}

		return props;
	}

}
