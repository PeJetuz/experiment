package my.test.eureka.config;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KafkaConfigurationImpl implements KafkaConfiguration {

    @Value("${kafka.bootstrap.servers}")
    private String serversUri = "localhost:9092";
    @Value("${kafka.acks}")
    private String acks = "all";
    @Value("${kafka.retries}")
    private String retries = "0";
    @Value("${kafka.batch.size}")
    private String batchSize = "16384";
    @Value("${kafka.linger.ms}")
    private String lingerMs = "1";
    @Value("${kafka.buffer.memory}")
    private String bufferMemory = "409600";
    @Value("${kafka.key.serializer}")
    private String keySerializer = "org.apache.kafka.common.serialization.StringSerializer";
    @Value("${kafka.value.serializer}")
    private String valueSerializer = "org.apache.kafka.common.serialization.StringSerializer";
    @Value("${authentication.logon.topic.name}")
    private String logonTopicName = "logon";

    public Properties generateKafkaProperties() {
        Properties kafkaProperties = new Properties();
        kafkaProperties.put("bootstrap.servers", serversUri);

        //Set acknowledgements for producer requests.
        kafkaProperties.put("acks", acks);

        //If the request fails, the producer can automatically retry,
        kafkaProperties.put("retries", Integer.valueOf(retries));

        //Specify buffer size in config
        kafkaProperties.put("batch.size", Integer.valueOf(batchSize));

        //Reduce the no of requests less than 0
        kafkaProperties.put("linger.ms", Long.valueOf(lingerMs));

        //The buffer.memory controls the total amount of memory available to the producer for buffering.
        kafkaProperties.put("buffer.memory", Long.valueOf(bufferMemory));

        kafkaProperties.put("key.serializer", keySerializer);

        kafkaProperties.put("value.serializer", valueSerializer);
        return kafkaProperties;
    }

    @Override
    public String getLogonTopicName() {
        return logonTopicName;
    }
}
