package com.websockets.connectors;


import com.websockets.config.Configs;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.sink.SinkConnector;
import org.apache.zookeeper.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class WebSocketSinkConnector extends SinkConnector {

    private static final Logger log = LoggerFactory.getLogger(WebSocketSinkConnector.class);

    private Map<String, String> config;

    @Override
    public String version() {
        return Version.getVersion();
    }

    @Override
    public void start(Map<String, String> props) {
        config = props;
        //log.info("Configs " + new Gson().toJson(props));
        Configs.getInstance().setProperties(props);
        // create configs in Task
    }

    @Override
    public Class<? extends Task> taskClass() {
        return WebSocketSinkTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {

        final List<Map<String, String>> configs = new LinkedList<>();

        Map<String, String> taskProps = new HashMap<>();
        taskProps.putAll(config);
        for (int i = 0; i < maxTasks; i++) {
            configs.add(taskProps);
        }
        return configs;
    }

    @Override
    public void stop() {

    }

    @Override
    public ConfigDef config() {
        return null;
    }
}
