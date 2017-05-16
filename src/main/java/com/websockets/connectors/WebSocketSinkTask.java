package com.websockets.connectors;

import com.websockets.handlers.KafkaHandler;
import com.websockets.utils.MetaData;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;

public class WebSocketSinkTask extends SinkTask {

    private Logger log = Logger.getLogger(WebSocketSinkTask.class.getName());
    private KafkaHandler kafkaHandler;

    private KafkaJsonConnect kafkaJsonConnect;

    @Override
    public String version() {
        return null;
    }

    @Override
    public void start(Map<String, String> props) {
        try {
            MetaData metaData = new MetaData();
            metaData.setTopic();

            kafkaJsonConnect = new KafkaJsonConnect();
            int port = 9093;
            kafkaHandler = new KafkaHandler(port);
            kafkaHandler.start();
            log.info("WebSocket server started on port: {}" + kafkaHandler.getPort());
        } catch (Exception e) {
            System.err.println("Could not start server " + e);
        }

    }

    @Override
    public void put(Collection<SinkRecord> records) {
        kafkaJsonConnect.onMessageReceivedFromTopic();
    }

    @Override
    public void flush(Map<TopicPartition, OffsetAndMetadata> offsets) {

    }

    @Override
    public void stop() {
        if (kafkaHandler != null) {
            try {
                kafkaHandler.stop();
            } catch (IOException e) {
                System.err.println("Could not stop server " + e);
            } catch (InterruptedException e) {
                System.err.println("Could not stop server " + e);
            }
        }
    }
}
