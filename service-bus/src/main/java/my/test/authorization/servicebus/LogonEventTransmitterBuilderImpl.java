package my.test.authorization.servicebus;

import java.util.Properties;
import my.test.authorization.domain.api.servicebus.LogonEventTransmitter;
import my.test.authorization.domain.api.servicebus.LogonEventTransmitterBuilder;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

public class LogonEventTransmitterBuilderImpl implements LogonEventTransmitterBuilder {

    private final Producer kafkaProducer;
    private final String topicName;

    public LogonEventTransmitterBuilderImpl(Properties kafkaConfig, String topicName) {
        this.kafkaProducer = new KafkaProducer(kafkaConfig);
        this.topicName = topicName;
    }

    @Override
    public LogonEventTransmitter createLoginEventTransmitter(String userName) {
        return new LogonEventTransmitterImpl(userName, kafkaProducer, topicName);
    }
}
