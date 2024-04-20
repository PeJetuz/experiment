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
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Kafka integration test (multiple partition).
 *
 * @since 1.0
 */
@SuppressWarnings("PMD.TooManyMethods")
final class KafkaPairPartitionIT {

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

    /**
     * Messages.
     */
    private final Map<String, String> messages;

    KafkaPairPartitionIT() {
        this.topic = "testPairPartTopic_%d".formatted(new Random().nextLong());
        this.messages = new HashMap<>();
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
    void kafkaSinglePartitionRepeatReadTest() throws Exception {
        try (Consumer<String, String> fcons = this.createConsumer();
            Consumer<String, String> scons = this.createConsumer()
        ) {
            this.send(0, 10);
            final ConsumerRecords<String, String> fonerc = this.poll(fcons);
            fcons.commitSync();
            this.send(10, 20);
            final ConsumerRecords<String, String> sonerc = this.poll(scons);
            final ConsumerRecords<String, String> ftworc = this.poll(fcons);
            final ConsumerRecords<String, String> stworc = this.poll(scons);
            fcons.commitSync();
            scons.commitSync();
            Assertions.assertThat(fonerc.count()).isEqualTo(10);
            Assertions.assertThat(sonerc.count()).isZero();
            Assertions.assertThat(ftworc.count() + stworc.count()).isEqualTo(10);
            this.assertRead(fonerc);
            this.assertRead(ftworc);
            this.assertRead(stworc);
        }
    }

    private void assertRead(final ConsumerRecords<String, String> records) {
        records.forEach(
            r -> {
                Assertions.assertThat(this.messages).containsKey(r.key());
                final String value = "event %s".formatted(r.key());
                Assertions.assertThat(this.messages.remove(r.key())).isEqualTo(value);
            });
    }

    private void send(final int from, final int count)
        throws ExecutionException, InterruptedException {
        try (Producer<String, String> producer =
            new KafkaProducer<>(KafkaPairPartitionIT.produserProps())) {
            for (int cnt = from; cnt < count; ++cnt) {
                final String key = String.valueOf(cnt);
                final String value = "event %d".formatted(cnt);
                final ProducerRecord<String, String> prec =
                    new ProducerRecord<>(this.topic, key, value);
                final Future<RecordMetadata> result = producer.send(prec);
                result.get();
                this.messages.put(key, value);
            }
            producer.flush();
        }
    }

    private ConsumerRecords<String, String> poll(final Consumer<String, String> consumer) {
        return consumer.poll(Duration.ofMillis(KafkaPairPartitionIT.TIME_OUT_MS));
    }

    private Consumer<String, String> createConsumer() {
        final Consumer<String, String> consumer =
            new KafkaConsumer<>(KafkaPairPartitionIT.consumerProps());
        consumer.subscribe(Collections.singletonList(this.topic));
        return consumer;
    }

    private static Properties consumerProps() {
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaPairPartitionIT.BS_SRV);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "g1");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 5000);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()
        );
        return props;
    }

    private static Properties produserProps() {
        final Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaPairPartitionIT.BS_SRV);
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
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaPairPartitionIT.BS_SRV);
        props.put(AdminClientConfig.RETRIES_CONFIG, 3);
        try (Admin admin = Admin.create(props)) {
            final NewTopic ntopic = new NewTopic(this.topic, 2, (short) 1);
            final CreateTopicsResult result = admin.createTopics(Collections.singleton(ntopic));
            result.all().get();
        }
    }

    private void removeTopic() throws ExecutionException, InterruptedException {
        final Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaPairPartitionIT.BS_SRV);
        props.put(AdminClientConfig.RETRIES_CONFIG, 3);
        try (Admin admin = Admin.create(props)) {
            final DeleteTopicsResult result = admin.deleteTopics(Collections.singleton(this.topic));
            result.all().get();
        }
    }
}
