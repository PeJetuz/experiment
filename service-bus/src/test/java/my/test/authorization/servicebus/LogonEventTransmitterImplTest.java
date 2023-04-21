package my.test.authorization.servicebus;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;

public class LogonEventTransmitterImplTest {

    private Producer producer = Mockito.mock(Producer.class);

    @Test
    public void sendUserLoginEvent() {
        LogonEventTransmitterImpl subj = buildLogonEventTransmitter();

        subj.sendUserLogonEvent(null);

        verify(producer).send(isA(ProducerRecord.class));
    }

    private LogonEventTransmitterImpl buildLogonEventTransmitter() {
        Random random = ThreadLocalRandom.current();
        String topicName = "" + random.nextLong();
        return new LogonEventTransmitterImpl(null, producer, topicName);
    }
}
