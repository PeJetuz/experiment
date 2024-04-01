package my.test.authorization.servicebus;

import my.test.authorization.domain.api.servicebus.LoginEventTransmitter;
import org.apache.kafka.clients.producer.Producer;

public class LoginEventTransmitterMock implements LoginEventTransmitter {

    public LoginEventTransmitterMock(String userName, Producer<String, String> kafkaProducer, String kafkaTopicName) {
    }

    @Override
    public void sendUserLogonEvent(String event) {

    }
}
