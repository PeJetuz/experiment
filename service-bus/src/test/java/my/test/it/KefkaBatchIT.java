/*
 * MIT License
 *
 * Copyright (c) 2023 Vladimir Shapkin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package my.test.it;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Kafka integration test (batch polling).
 *
 * @since 1.0
 */
@SuppressWarnings("PMD.TooManyMethods")
final class KefkaBatchIT {

    /**
     * Timeout operation.
     */
    private static final long TIME_OUT_MS = 10_000;

    /**
     * Kafka bootstrap server.
     */
    private static final String BS_SRV = "localhost:9094";

    /**
     * Topic name.
     */
    private final String topic;

    KefkaBatchIT() {
        this.topic = "testTopic_%d".formatted(new Random().nextLong());
    }

    @BeforeEach
    void init() throws ExecutionException, InterruptedException {
        this.createTopic();
    }

    @AfterEach
    void cleanup() throws ExecutionException, InterruptedException {
        this.removeTopic();
    }

    @Test
    void kafkaBatchReadTest() throws Exception {
        try (Consumer<String, String> consumer = this.createConsumer("g1")) {
            this.send(0, 10);
            final ConsumerRecords<String, String> fread = this.poll(consumer);
            consumer.commitSync();
            final ConsumerRecords<String, String> sread = this.poll(consumer);
            this.commitWoTwo(consumer, sread);
            this.send(10, 20);
            consumer.enforceRebalance();
            final ConsumerRecords<String, String> tread = this.poll(consumer);
            consumer.seekToBeginning(consumer.assignment());
            final ConsumerRecords<String, String> ffread = this.poll(consumer);
            consumer.commitSync();
            KefkaBatchIT.assertRead(fread, 0, 5);
            KefkaBatchIT.assertRead(sread, 5, 5);
            KefkaBatchIT.assertRead(tread, 7, 5);
            KefkaBatchIT.assertRead(ffread, 0, 5);
        }
    }

    private void commitWoTwo(
        final Consumer<String, String> consumer,
        final ConsumerRecords<String, String> records
    ) {
        final Map<TopicPartition, OffsetAndMetadata> commits = new HashMap<>();
        final AtomicInteger count = new AtomicInteger(records.count());
        records.forEach(
            r -> {
                if (count.intValue() > 3) {
                    commits.put(
                        new TopicPartition(this.topic, r.partition()),
                        new OffsetAndMetadata(r.offset() + 1, null)
                    );
                }
                count.decrementAndGet();
            });
        consumer.commitSync(commits);
    }

    private static void assertRead(
        final ConsumerRecords<String, String> records,
        final int from,
        final int size
    ) {
        Assertions.assertThat(records.count()).isEqualTo(size);
        final AtomicInteger count = new AtomicInteger(from);
        records.forEach(
            r -> {
                final String key = count.toString();
                final String value = "event %d".formatted(count.intValue());
                count.incrementAndGet();
                Assertions.assertThat(r.key()).isEqualTo(key);
                Assertions.assertThat(r.value()).isEqualTo(value);
            });
    }

    private void send(final int from, final int count)
        throws ExecutionException, InterruptedException {
        try (Producer<String, String> producer =
            new KafkaProducer<>(KefkaBatchIT.produserProps())
        ) {
            for (int cnt = from; cnt < count; ++cnt) {
                final ProducerRecord<String, String> prec =
                    new ProducerRecord<>(
                        this.topic, String.valueOf(cnt), "event %d".formatted(cnt)
                    );
                final Future<RecordMetadata> result = producer.send(prec);
                result.get();
            }
            producer.flush();
        }
    }

    private ConsumerRecords<String, String> poll(final Consumer<String, String> consumer) {
        return consumer.poll(Duration.ofMillis(KefkaBatchIT.TIME_OUT_MS));
    }

    private Consumer<String, String> createConsumer(final String group) {
        final Consumer<String, String> consumer =
            new KafkaConsumer<>(KefkaBatchIT.consumerProps(group));
        consumer.subscribe(Collections.singletonList(this.topic));
        return consumer;
    }

    private static Properties consumerProps(final String group) {
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KefkaBatchIT.BS_SRV);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, group);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 5000);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 5);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()
        );
        return props;
    }

    private static Properties produserProps() {
        final Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KefkaBatchIT.BS_SRV);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 3);
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 1000);
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 500);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return props;
    }

    private void createTopic() throws ExecutionException, InterruptedException {
        final Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, KefkaBatchIT.BS_SRV);
        props.put(AdminClientConfig.RETRIES_CONFIG, 3);
        try (Admin admin = Admin.create(props)) {
            final NewTopic ntopic = new NewTopic(this.topic, 1, (short) 1);
            final CreateTopicsResult result = admin.createTopics(Collections.singleton(ntopic));
            result.all().get();
        }
    }

    private void removeTopic() throws ExecutionException, InterruptedException {
        final Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, KefkaBatchIT.BS_SRV);
        props.put(AdminClientConfig.RETRIES_CONFIG, 3);
        try (Admin admin = Admin.create(props)) {
            final DeleteTopicsResult result = admin.deleteTopics(Collections.singleton(this.topic));
            result.all().get();
        }
    }
}
