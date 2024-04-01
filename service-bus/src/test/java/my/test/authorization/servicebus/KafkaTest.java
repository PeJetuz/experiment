package my.test.authorization.servicebus;

import com.github.dockerjava.api.command.CreateContainerCmd;
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
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@Testcontainers
public class KafkaTest {

    private static final long TIME_OUT_MS = 10000;
//    static Network network = Network.newNetwork();
//    @Container
//    GenericContainer kafka = new GenericContainer(DockerImageName.parse("bitnami/kafka:3.6.1"))
//            .withEnv(createKafkaEnvironment())
//            .withCreateContainerCmdModifier(cmd -> ((CreateContainerCmd)cmd).withHostName("kafka-server"))
//            .withExposedPorts(9092, 9093, 9094, 29094)
//            .withAccessToHost(true)
//            .withNetwork(network)
//            ;

    private static Map<String, String> createKafkaEnvironment() {
        Map<String, String> env = new HashMap<>();
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
    public void kafkaTest() throws Exception {
        String topic = "kafkaTest";
        Properties kafkaProperties = generateKafkaProperties();
        try(Producer<String, String> producer = new KafkaProducer<>(kafkaProperties);
            Consumer<String, String> consumer = new KafkaConsumer(kafkaProperties)) {
            consumer.subscribe(List.of(topic));
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(TIME_OUT_MS));
            if (records.count() > 0) {
                consumer.commitSync();
                System.out.println("Error, kafka has not readed data, count = " + records.count());
            }

            String key = UUID.randomUUID().toString();
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key, "first event");
            Future<RecordMetadata> result = producer.send(producerRecord);
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

            records = consumer.poll(Duration.ofMillis(TIME_OUT_MS));
            consumer.commitSync();
            assertEquals(1, records.count());
            records.forEach(r -> {
                assertEquals(key, r.key());
                assertEquals("first event", r.value());
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
        Properties props = new Properties();
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
