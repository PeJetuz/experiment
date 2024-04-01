package my.test.authorization.servicebus;

import my.test.authorization.domain.api.servicebus.LoginEventTransmitter;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class LoginEventTransmitterImpl implements LoginEventTransmitter {

    private final String userName;
    private final Producer<String, String> kafkaProducer;
    private final String kafkaTopicName;

    public LoginEventTransmitterImpl(String userName, Producer<String, String> kafkaProducer, String kafkaTopicName) {
        this.userName = userName;
        this.kafkaProducer = kafkaProducer;
        this.kafkaTopicName = kafkaTopicName;
    }

    @Override
    public void sendUserLogonEvent(String event) {
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(kafkaTopicName, userName);
        kafkaProducer.send(producerRecord);
    }
}
