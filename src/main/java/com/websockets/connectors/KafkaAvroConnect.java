package com.websockets.connectors;

import com.websockets.utils.MetaData;
import com.websockets.utils.Params;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.java_websocket.WebSocket;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

public class KafkaAvroConnect implements  Connector {

    private MetaData metaData;

    private KafkaStreams kafkaStreams;
    private Random consumerPicker = new Random();


    StreamsConfig streamConfig = new StreamsConfig(getProperties(metaData.getKafkaUrl(),
                                                        metaData.getZookeeperUrl(),
                                                        metaData.getConsumerGroup(),
                                                        metaData.getFromBeginning(),
                                                        metaData.getSchemaUrl()));

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
                metaData.getTopic());
        kstream.mapValues(new ValueMapper<String, String>() {
            @Override
            public String apply(String value) {
                onMessageReceivedFromTopic(value, metaData);
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
        props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaUrl);
        //props.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		//props.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, classOf[GenericAvroSerde]);

        if (fromBeginning) {
            props.put("auto.offset.reset", "earliest");
        } else {
            props.put("auto.offset.reset", "latest");
        }

        return props;
    }
}
