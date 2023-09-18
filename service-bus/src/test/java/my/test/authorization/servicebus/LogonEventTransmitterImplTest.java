package my.test.authorization.servicebus;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.kafka.clients.consumer.ConsumerGroupMetadata;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LogonEventTransmitterImplTest {

    @Test
    public void sendUserLoginEvent() {
        FakeProducer producer = new FakeProducer();
        LogonEventTransmitterImpl subj = buildLogonEventTransmitter(producer);

        subj.sendUserLogonEvent(null);

        assertNotNull(producer.producerRecord);
    }

    private LogonEventTransmitterImpl buildLogonEventTransmitter(FakeProducer producer) {
        Random random = ThreadLocalRandom.current();
        String topicName = "" + random.nextLong();
        return new LogonEventTransmitterImpl(null, producer, topicName);
    }

    final class FakeProducer implements Producer<String, String> {

        ProducerRecord<String, String> producerRecord;

        @Override
        public void initTransactions() {

        }

        @Override
        public void beginTransaction() throws ProducerFencedException {

        }

        @Override
        public void sendOffsetsToTransaction(Map<TopicPartition, OffsetAndMetadata> offsets,
                java.lang.String consumerGroupId) throws ProducerFencedException {

        }

        @Override
        public void sendOffsetsToTransaction(Map<TopicPartition, OffsetAndMetadata> offsets,
                ConsumerGroupMetadata groupMetadata) throws ProducerFencedException {

        }

        @Override
        public void commitTransaction() throws ProducerFencedException {

        }

        @Override
        public void abortTransaction() throws ProducerFencedException {

        }

        @Override
        public Future<RecordMetadata> send(ProducerRecord<String, String> record) {
            producerRecord = record;
            return null;
        }

        @Override
        public Future<RecordMetadata> send(ProducerRecord<String, String> record, Callback callback) {
            return null;
        }

        @Override
        public void flush() {

        }

        @Override
        public List<PartitionInfo> partitionsFor(java.lang.String topic) {
            return null;
        }

        @Override
        public Map<MetricName, ? extends Metric> metrics() {
            return null;
        }

        @Override
        public void close() {

        }

        @Override
        public void close(Duration timeout) {

        }
    }
}
