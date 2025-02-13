package io.helidon.examples.quickstart.se;

import io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializer;
import io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializerConfig;
import io.confluent.kafka.serializers.protobuf.KafkaProtobufSerializer;
import io.helidon.config.Config;
import io.helidon.messaging.Channel;
import io.helidon.messaging.Emitter;
import io.helidon.messaging.Messaging;
import io.helidon.messaging.connectors.kafka.KafkaConfigBuilder;
import io.helidon.messaging.connectors.kafka.KafkaConnector;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import my.test.protobuf.TestOuter;
import my.test.protobuf.TestOuterTwo;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@Disabled
final class KafkaProtoTest {

    private final Config config = Config.create();
    private final String kafkaServer = config.get("app.kafka.bootstrap.servers").asString().get();
    private final String topic = config.get("app.kafka.topic").asString().get();
    private final String compression = config.get("app.kafka.compression").asString().orElse("none");
    private final String schemaRegistryUrl = config.get("app.kafka.schema.registry.url").asString().get();

    //kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic messaging-test-topic-snappy-compressed --from-beginning
    @Test
    void test() throws InterruptedException {

        // Prepare channel for connecting processor -> kafka connector with specific subscriber configuration,
        // channel -> connector mapping is automatic when using KafkaConnector.configBuilder()
        Channel<TestOuter.Message> toKafka =
            this.<TestOuter.Message>createToKafkaChannel(KafkaProtobufSerializerTestMsg.class);

        // Prepare channel for connecting emitter -> processor
        Channel<TestOuter.Message> toProcessor = Channel.create();

        // Prepare Kafka connector, can be used by any channel
        KafkaConnector kafkaConnector = KafkaConnector.create();

        // Prepare emitter for manual publishing to channel
        final Emitter<TestOuter.Message> emitter = Emitter.create(toProcessor);

        // Transforming to upper-case before sending to kafka
        Messaging sendMessaging = this.<TestOuter.Message>createSendMessaging(
            emitter,
            toProcessor,
            m -> TestOuter.Message.newBuilder(m)
                .setTitle(m.getTitle().toUpperCase())
                .build(),
            toKafka,
            kafkaConnector
        );

        // Prepare channel for connecting kafka connector with specific publisher configuration -> listener,
        // channel -> connector mapping is automatic when using KafkaConnector.configBuilder()
        Channel<TestOuter.Message> fromKafka = this.<TestOuter.Message>createFromKafkaChannel(
            TestOuter.Message.class.getName(),
            KafkaProtobufDeserializerTestMsg.class
        );

        List<TestOuter.Message> loaded = new CopyOnWriteArrayList<>();
        final Lock lock = new ReentrantLock();
        final Condition readed  = lock.newCondition();
        Messaging readMessaging = Messaging.builder()
            .connector(kafkaConnector)
            .listener(fromKafka, payload -> {
                System.out.println("Kafka says: " + payload);
                // Send message received from Kafka over websocket
                loaded.add(payload);
                try {
                    if (lock.tryLock(1, TimeUnit.SECONDS)) {
                        readed.signal();
                        System.out.println("Load: Current thread signal " + Thread.currentThread().getName() +
                            ", time = " + LocalDateTime.now());
                    }
                } catch (InterruptedException e) {
                    System.err.println("Error: reading: try locking failed!");
                } finally {
                    lock.unlock();
                }
            })
            .build()
            .start();

        int num = new Random().nextInt();
        System.out.println("Num = " + num);
        emitter.send(TestOuter.Message.newBuilder()
            .setTitle("test1")
            .setSomeNumber(num)
            .addMesssage("test one msg one")
            .addMesssage("test one msg two")
            .build()
        );

        waitLock(lock, readed);
        sendMessaging.stop();
        readMessaging.stop();

        MatcherAssert.assertThat(
            "Message not readed",
            loaded.size(),
            Matchers.equalTo(1)
        );

        Channel<TestOuterTwo.Message2> toKafkaTwo =
            this.<TestOuterTwo.Message2>createToKafkaChannel(KafkaProtobufSerializerTestMsgTwo.class);

        // Prepare channel for connecting emitter -> processor
        Channel<TestOuterTwo.Message2> toProcessorTwo = Channel.create();

        // Prepare Kafka connector, can be used by any channel
        KafkaConnector kafkaConnectorTwo = KafkaConnector.create();

        // Prepare emitter for manual publishing to channel
        Emitter<TestOuterTwo.Message2> emitterTwo = Emitter.create(toProcessorTwo);

        // Transforming to upper-case before sending to kafka
        Messaging sendMessagingTwo = this.<TestOuterTwo.Message2>createSendMessaging(
            emitterTwo,
            toProcessorTwo,
            m -> m,
            toKafkaTwo,
            kafkaConnector
        );

        final AtomicBoolean succs = new AtomicBoolean(false);
        final List<Throwable> exceptions = new ArrayList<>(1);
        Message<TestOuterTwo.Message2> msg = Message.of(TestOuterTwo.Message2.newBuilder()
                .setTitle("testTwo")
                .setSomeNumber(num + 1)
                .addMesssage("test two msg one")
                .addMesssage("test two msg two")
                .build())
            .withAck(() -> {
                succs.set(true);
                return CompletableFuture.completedFuture(null);
            })
            .withNack(exc -> {
                exceptions.add(exc);
                return CompletableFuture.completedFuture(null);
            });
        ErrorSubscriber subscriber = new ErrorSubscriber(lock, readed);
        emitterTwo.subscribe(subscriber);
        emitterTwo.send(msg);

        waitLock(lock, readed);
        waitLock(lock, readed);
        waitLock(lock, readed);
        sendMessagingTwo.stop();

        MatcherAssert.assertThat(
            "Message not readed",
            loaded.size(),
            Matchers.equalTo(1)
        );

    }

    public static final class ErrorSubscriber implements Subscriber<Message<TestOuterTwo.Message2>> {
        private Subscription subscription;
        final List<TestOuterTwo.Message2> next;
        final List<Throwable> errors;
        final List<Boolean> completed;
        final Lock lock;
        final Condition readed;

        public ErrorSubscriber(final Lock lock, final Condition readed) {
            this.next = new ArrayList<>(1);
            this.errors = new ArrayList<>(1);
            this.completed = new ArrayList<>(1);
            this.lock = lock;
            this.readed = readed;
        }

        @Override
        public void onSubscribe(Subscription subscription) {
            this.subscription = subscription;
        }

        @Override
        public void onNext(Message<TestOuterTwo.Message2> message) {
            this.next.add(message.getPayload());
        }

        @Override
        public void onError(Throwable exception) {
            this.errors.add(exception);
            try {
                if (lock.tryLock(2, TimeUnit.SECONDS)) {
                    readed.signal();
                    System.out.println("ErrorSubscriber.onError: Current thread after signal " + Thread.currentThread().getName() +
                        ", time = " + LocalDateTime.now());
                }
            } catch (InterruptedException e) {
                System.err.println("Error: ErrorSubscriber.onError: try locking failed!");
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void onComplete() {
            this.completed.add(true);
        }
    }

    private static void waitLock(Lock lock, Condition readed) {
        System.out.println("Current thread await " + Thread.currentThread().getName() +
            ", time = " + LocalDateTime.now());
        try {
            if (lock.tryLock(20, TimeUnit.SECONDS)) {

                readed.await(20, TimeUnit.SECONDS);
                System.out.println("Current thread after await " + Thread.currentThread().getName() +
                    ", time = " + LocalDateTime.now());
            }
        } catch (InterruptedException e) {
            System.err.println("Error: try locking failed!");
        } finally {
            lock.unlock();
        }
    }

    private<T> Channel createFromKafkaChannel(final String desClassName,
                                              final Class<? extends Deserializer<T>> deserializer
    ) {
        return Channel.<T>builder()
            .name("from-kafka")
            .publisherConfig(
                KafkaConnector.configBuilder()
                    .bootstrapServers(kafkaServer)
                    .property("schema.registry.url", schemaRegistryUrl)
                    //                   .property("derive.type", "true")
                    .property(KafkaProtobufDeserializerConfig.SPECIFIC_PROTOBUF_VALUE_TYPE,
                        desClassName)
                    .groupId("test-group-id")
                    .topic(topic)
                    .autoOffsetReset(KafkaConfigBuilder.AutoOffsetReset.LATEST)
                    .enableAutoCommit(true)
                    .keyDeserializer(StringDeserializer.class)
                    .valueDeserializer(deserializer)
                    .compressionType(compression)
                    .build()
            )
            .build();
    }

    private <T> Messaging createSendMessaging(Emitter<T> emitter,
                                             Channel<T> toProcessor,
                                             Function<? super T, ? extends T> func,
                                             Channel<T> toKafka, KafkaConnector kafkaConnector) {
        return Messaging.builder()
            .emitter(emitter)
            // Processor connect two channels together
            .processor(toProcessor, toKafka, func)
            .connector(kafkaConnector)
            .build()
            .start();
    }

    private <T> Channel createToKafkaChannel(final Class<? extends Serializer<?>> serializer) {
        return Channel.<T>builder()
            .subscriberConfig(KafkaConnector.configBuilder()
                .property("schema.registry.url", schemaRegistryUrl)
                .bootstrapServers(kafkaServer)
                .topic(topic)
                .compressionType(compression)
                .keySerializer(StringSerializer.class)
                .valueSerializer(serializer)
                .build())
            .build();
    }

    public static final class KafkaProtobufSerializerTestMsg extends KafkaProtobufSerializer<TestOuter.Message> {
    }
    public static final class KafkaProtobufDeserializerTestMsg extends KafkaProtobufDeserializer<TestOuter.Message> {
    }
    public static final class KafkaProtobufSerializerTestMsgTwo extends KafkaProtobufSerializer<TestOuterTwo.Message2> {
    }
    public static final class KafkaProtobufDeserializerTestMsgTwo extends KafkaProtobufDeserializer<TestOuterTwo.Message2> {
    }
}
