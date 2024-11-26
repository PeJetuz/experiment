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

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.Messages;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.client.api.SubscriptionInitialPosition;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Pulsar integration test (single partition).
 *
 * @since 1.0
 */
final class PulsarSingleMessageIT {

    /**
     * Pulsar bootstrap server.
     */
    private static final String BS_SRV = "pulsar://localhost:6650";

    /**
     * Namespace.
     */
    //private static final String NS = "pulsar-test";

    /**
     * Tenant.
     */
    //private static final String TENANT = "test";

    /**
     * Topic name.
     */
    private static final String TOPIC = "PulsarSingleMessageIT-topic%d".formatted(
        new Random().nextInt()
    );

    @Test
    void oneToOne() throws Exception {
        final String topic = "%s%d".formatted(PulsarSingleMessageIT.TOPIC, 1);
        try (PulsarClient client = PulsarClient.builder()
            .serviceUrl(PulsarSingleMessageIT.BS_SRV)
            .build()
        ) {
            this.send(client, topic, 0, 10);
            final Messages<String> msgs =
                PulsarSingleMessageIT.readMessages(client, topic, "first-test-subscription");
            PulsarSingleMessageIT.assertRead(msgs, 0, 10);
        }
    }

    @Test
    void skipPartialMessages() throws Exception {
        final String topic = PulsarSingleMessageIT.TOPIC;
        final String subname = "skipPartialMessages-test-subscription";
        try (PulsarClient client = PulsarClient.builder()
            .serviceUrl(PulsarSingleMessageIT.BS_SRV)
            .build()
        ) {
            this.send(client, PulsarSingleMessageIT.TOPIC, 0, 10);
            PulsarSingleMessageIT.checkMsg(client, topic, subname, 0, 3, 0);
            PulsarSingleMessageIT.checkMsg(client, topic, subname, 3, 6, 4);
            PulsarSingleMessageIT.checkMsg(
                client,
                topic,
                "skipPartialMessages-test-subscription-1",
                0,
                10,
                0
            );
            PulsarSingleMessageIT.checkMsg(client, topic, subname, 3, 4, 3);
            PulsarSingleMessageIT.checkMsg(client, topic, subname, 6, 10, 4);
            PulsarSingleMessageIT.checkMsg(
                client,
                topic,
                "skipPartialMessages-test-subscription-2",
                0,
                10,
                0
            );
            try (Consumer<String> cons = client.newConsumer(Schema.STRING)
                .topic(topic)
                .subscriptionInitialPosition(SubscriptionInitialPosition.Latest)
                .subscriptionName("skipPartialMessages-test-subscription-3")
                .subscribe()
            ) {
                this.send(client, topic, 10, 13);
                PulsarSingleMessageIT.validateMsg(cons, 10, 13, 10);
            }
        }
    }

    private static void assertRead(
        final Messages<String> msgs,
        final int from,
        final int size
    ) {
        Assertions.assertThat(msgs.size()).isEqualTo(size);
        final AtomicInteger count = new AtomicInteger(from);
        msgs.forEach(
            r -> {
                final String key = count.toString();
                final String value = "event %d".formatted(count.intValue());
                count.incrementAndGet();
                Assertions.assertThat(r.getKey()).isEqualTo(key);
                Assertions.assertThat(r.getValue()).isEqualTo(value);
            });
    }

    // @checkstyle ParameterNumberCheck (8 lines)
    private static void checkMsg(
        final PulsarClient client,
        final String topic,
        final String subname,
        final int from,
        final int until,
        final int ack
    ) throws PulsarClientException {
        try (Consumer<String> cons = PulsarSingleMessageIT.consumer(client, topic, subname)) {
            PulsarSingleMessageIT.validateMsg(cons, from, until, ack);
        }
    }

    // @checkstyle ParameterNumberCheck (6 lines)
    private static void validateMsg(
        final Consumer<String> cons,
        final int from,
        final int until,
        final int ack
    ) throws PulsarClientException {
        for (int cnt = from; cnt < until; cnt += 1) {
            final Message<String> msg = cons.receive();
            if (cnt >= ack) {
                cons.acknowledge(msg);
            }
            final String value = "event %d".formatted(cnt);
            Assertions.assertThat(msg.getKey()).isEqualTo(String.valueOf(cnt));
            Assertions.assertThat(msg.getValue()).isEqualTo(value);
        }
    }

    // @checkstyle ParameterNumberCheck (5 lines)
    private void send(
        final PulsarClient client,
        final String topic,
        final int from,
        final int until
    ) throws PulsarClientException {
        try (Producer<String> producer =
            client.newProducer(Schema.STRING).topic(topic).create()) {
            for (int cnt = from; cnt < until; ++cnt) {
                producer
                    .newMessage()
                    .key(String.valueOf(cnt))
                    .value("event %d".formatted(cnt))
                    .send();
            }
            producer.flush();
        }
    }

    private static Messages<String> readMessages(
        final PulsarClient client,
        final String topic,
        final String subname
    ) throws PulsarClientException {
        try (Consumer<String> consumer =
            PulsarSingleMessageIT.consumer(client, topic, subname)) {
            final Messages<String> msg = consumer.batchReceive();
            consumer.acknowledge(msg);
            return msg;
        }
    }

    private static Consumer<String> consumer(
        final PulsarClient client,
        final String topic,
        final String subname
    ) throws PulsarClientException {
        return client.newConsumer(Schema.STRING)
            .topic(topic)
            .subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
            .subscriptionName(subname)
            .subscribe();
    }
}
