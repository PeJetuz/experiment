package my.test.authorization.servicebus;

import java.util.Properties;
import my.test.authorization.domain.api.servicebus.LogonEventTransmitter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogonEventTransmitterBuilderImplTest {

    @Test
    public void createLoginEventTransmitter() {
        LogonEventTransmitterBuilderImpl subj = new LogonEventTransmitterBuilderImpl(generateKafkaProperties(), null);
        assertTrue(subj.createLoginEventTransmitter(null) instanceof LogonEventTransmitter);
    }

    private Properties generateKafkaProperties() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 335544);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return props;
    }
}
