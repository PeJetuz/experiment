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

package my.test.authorization.servicebus;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.Future;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Test kafka.
 *
 * @since 1.0
 * @checkstyle AvoidStaticImportCheck (200 lines)
 * @checkstyle LineLengthCheck (500 lines)
 * @checkstyle MethodsOrderCheck (200 lines)
 * @checkstyle WhitespaceAfterCheck (200 lines)
 * @checkstyle MethodBodyCommentsCheck (200 lines)
 * @checkstyle CommentsIndentationCheck (200 lines)
 */
@Disabled
//@Testcontainers
final class KafkaTest {

    /**
     * Timeout.
     */
    private static final long TIME_OUT_MS = 10_000;
//    static Network network = Network.newNetwork();
//    @Container
//    GenericContainer kafka = new GenericContainer(DockerImageName.parse("bitnami/kafka:3.6.1"))
//            .withEnv(createKafkaEnvironment())
//            .withCreateContainerCmdModifier(cmd -> ((CreateContainerCmd)cmd)
//            .withHostName("kafka-server"))
//            .withExposedPorts(9092, 9093, 9094, 29094)
//            .withAccessToHost(true)
//            .withNetwork(network)
//            ;

    /**
     * Method.
     *
     * @return Map.
     * @checkstyle LineLengthCheck (200 lines)
     */
    private static Map<String, String> createKafkaEnvironment() {
        final Map<String, String> env = new HashMap<>();
        env.put("KAFKA_CFG_NODE_ID", "0");
        env.put("KAFKA_CFG_PROCESS_ROLES", "controller,broker");
        env.put("KAFKA_CFG_LISTENERS", "PLAINTEXT://:9092,CONTROLLER://0.0.0.0:9093,EXTERNAL://0.0.0.0:9094,EXTERNAL2://0.0.0.0:29094");
        env.put("KAFKA_CFG_ADVERTISED_LISTENERS", "PLAINTEXT://kafka-server:9092,EXTERNAL://kafka-server:9094,EXTERNAL2://192.168.3.3:29094");
        env.put("KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP", "CONTROLLER:PLAINTEXT,EXTERNAL:PLAINTEXT,EXTERNAL2:PLAINTEXT,PLAINTEXT:PLAINTEXT");
        env.put("KAFKA_CFG_CONTROLLER_QUORUM_VOTERS", "0@kafka-server:9093");
        env.put("KAFKA_CFG_CONTROLLER_LISTENER_NAMES", "CONTROLLER");
        return env;
    }

    @Test
    void kafkaTest() throws Exception {
        final String topic = "kafkaTest";
        final Properties kprops = this.generateKafkaProperties();
        try (Producer<String, String> producer = new KafkaProducer<>(kprops);
            Consumer<String, String> consumer = new KafkaConsumer(kprops)) {
            consumer.subscribe(List.of(topic));
            ConsumerRecords<String, String> records =
                consumer.poll(Duration.ofMillis(KafkaTest.TIME_OUT_MS));
            if (records.count() > 0) {
                consumer.commitSync();
                System.out.println("Error, kafka has not readed data, count = " + records.count());
            }
            final String key = UUID.randomUUID().toString();
            final ProducerRecord<String, String> prec =
                new ProducerRecord<>(topic, key, "first event");
            final Future<RecordMetadata> result = producer.send(prec);
            result.get();
            //RecordMetadata metadata = result.get();
//kafka-console-producer.sh --producer.config /opt/bitnami/kafka/config/producer.properties --bootstrap-server 127.0.0.1:9092 --topic test
//kafka-console-producer.sh --broker-list localhost:9092 --topic kafkaTest --property "parse.key=true" --property "key.separator=:"
//kafka-console-producer.sh --broker-list kafka-server:9094 --topic kafkaTest --property "parse.key=true" --property "key.separator=:"
//kafka-console-consumer.sh --bootstrap-server kafka-server:9094 --topic kafkaTest --from-beginning --property "print.key=true"
//kafka-topics.sh --create --bootstrap-server localhost:9092 --topic kafkaTest
//kafka-topics.sh --describe --bootstrap-server localhost:9092 --topic kafkaTest
//docker run -d --name kafka-server --hostname kafka-server --network app-tier -e KAFKA_CFG_NODE_ID=0 -e KAFKA_CFG_PROCESS_ROLES=controller,broker -e KAFKA_CFG_LISTENERS=PLAINTEXT://:9092,CONTROLLER://0.0.0.0:9093,EXTERNAL://0.0.0.0:9094,EXTERNAL2://0.0.0.0:29094 -e KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://kafka-server:9092,EXTERNAL://kafka-server:9094,EXTERNAL2://192.168.3.3:29094 -e KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,EXTERNAL:PLAINTEXT,EXTERNAL2:PLAINTEXT,PLAINTEXT:PLAINTEXT -e KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=0@kafka-server:9093 -e KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER -p 9094:9094 -p 29094:29094 -p 9093:9093 bitnami/kafka:3.6.1
//docker run -d --name kafka-server_test --hostname kafka-server_test --network app-tier -e KAFKA_CFG_NODE_ID=0 -e KAFKA_CFG_PROCESS_ROLES=controller,broker -e KAFKA_CFG_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093 -e KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT -e KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=0@kafka-server_test:9093 -e KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER bitnami/kafka:3.6.1
//docker run -d --name kafka-server_test2 --hostname kafka-server_test2 --network ide -e KAFKA_CFG_NODE_ID=0 -e KAFKA_CFG_PROCESS_ROLES=controller,broker -e KAFKA_CFG_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093 -e KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT -e KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=0@kafka-server_test2:9093 -e KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER bitnami/kafka:3.6.1
//docker run -ti --name=ubuntu --network app-tier ubuntu:23.10
            records = consumer.poll(Duration.ofMillis(KafkaTest.TIME_OUT_MS));
            consumer.commitSync();
            Assertions.assertThat(records.count()).isEqualTo(1);
            records.forEach(r -> {
                Assertions.assertThat(key).isEqualTo(r.key());
                Assertions.assertThat("first event").isEqualTo(r.value());
            });
        }
    }

    /**
     * port 9092 for localhost, EXTERNAL 9094 for local network, EXTERNAL2 29094 for external address 192.168.3.3
     * docker run -d --name kafka-server --hostname kafka-server --network app-tier -e KAFKA_CFG_NODE_ID=0 -e KAFKA_CFG_PROCESS_ROLES=controller,broker -e KAFKA_CFG_LISTENERS=PLAINTEXT://:9092,CONTROLLER://0.0.0.0:9093,EXTERNAL://0.0.0.0:9094,EXTERNAL2://0.0.0.0:29094 -e KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://kafka-server:9092,EXTERNAL://kafka-server:9094,EXTERNAL2://192.168.3.3:29094 -e KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,EXTERNAL:PLAINTEXT,EXTERNAL2:PLAINTEXT,PLAINTEXT:PLAINTEXT -e KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=0@kafka-server:9093 -e KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER -p 9094:9094 -p 29094:29094 -p 9093:9093 bitnami/kafka:3.6.1
     * https://github.com/bitnami/containers/blob/main/bitnami/kafka/README.md
     * @return
     */
    private Properties generateKafkaProperties() {
        final Properties props = new Properties();
        //props.put("bootstrap.servers", "kafka-server:9094"); //for github
        props.put("bootstrap.servers", "localhost:29094"); //for developer local station
        //props.put("bootstrap.servers", kafka.getHost() + ":9094");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("acks", "all");
        //props.put("retries", 0);
        props.put("delivery.timeout.ms", 1000);
        props.put("request.timeout.ms", 500);
//        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
//        props.put("buffer.memory", 335544);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return props;
    }
}
