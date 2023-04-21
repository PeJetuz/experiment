package my.test.authorization.servicebus;

import my.test.authorization.domain.api.servicebus.LogonEventTransmitter;
import org.apache.kafka.clients.producer.Producer;

public class LogonEventTransmitterMock implements LogonEventTransmitter {

    public LogonEventTransmitterMock(String userName, Producer<String, String> kafkaProducer, String kafkaTopicName) {
    }

    @Override
    public void sendUserLogonEvent(String eventId) {

    }
}
