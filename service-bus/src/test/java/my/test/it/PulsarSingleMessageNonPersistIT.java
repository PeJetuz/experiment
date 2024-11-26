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

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Messages;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.client.api.SubscriptionInitialPosition;
import org.apache.pulsar.common.policies.data.TenantInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pulsar integration test (single partition).
 * Use non-persistent and non-partitioned topic
 *
 * @since 1.0
 */
final class PulsarSingleMessageNonPersistIT {

    /**
     * Pulsar bootstrap server.
     */
    private static final String BS_SRV = "pulsar://localhost:6650";

    /**
     * Pulsar bootstrap server.
     */
    private static final String BS_ADMIN = "http://localhost:8585";

    /**
     * Namespace.
     */
    private static final String NS = "pulsar-test";

    /**
     * Tenant.
     */
    private static final String TENANT = "PulsarSingleMessageNonPersistIT";

    /**
     * Tenant + Namespace.
     */
    private static final String TENENT_NS =
        "%s/%s"
            .formatted(
                PulsarSingleMessageNonPersistIT.TENANT,
                PulsarSingleMessageNonPersistIT.NS
            );

    /**
     * Topic name.
     */
    private final String topic;

    PulsarSingleMessageNonPersistIT() {
        this.topic = "non-persistent://%s/%s/test-topic_%d"
            .formatted(
                PulsarSingleMessageNonPersistIT.TENANT,
                PulsarSingleMessageNonPersistIT.NS,
                new Random().nextLong()
            );
    }

    /**
     * Test initialization.
     *
     * @throws PulsarClientException Error creating topic.
     * @throws PulsarAdminException Error obtaining administrator rights.
     */
    @BeforeEach
    void init() throws PulsarClientException, PulsarAdminException {
        this.createTopic();
    }

    /**
     * Post test cleaning.
     *
     * @throws PulsarClientException Error deleting topic.
     * @throws PulsarAdminException Error obtaining administrator rights.
     */
    @AfterEach
    void cleanup() throws PulsarClientException, PulsarAdminException {
        this.removeTopic();
    }

    @Test
    void oneToOne() throws Exception {
        try (PulsarClient client = PulsarClient.builder()
            .serviceUrl(PulsarSingleMessageNonPersistIT.BS_SRV)
            .build();
            Consumer<String> cons = client.newConsumer(Schema.STRING)
                .topic(this.topic)
                .subscriptionName("first-test-subscription")
                .subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
                .subscribe()) {
            this.send(client, this.topic, 0, 10);
            final Messages<String> msgs = cons.batchReceive();
            cons.acknowledge(msgs);
            PulsarSingleMessageNonPersistIT.assertRead(msgs, 0, 10);
        }
    }

    private static void assertRead(
        final Messages<String> msgs,
        final int from,
        final int size) {
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

    // @checkstyle ParameterNumberCheck (5 lines)
    private void send(
        final PulsarClient client,
        final String topc,
        final int from,
        final int until
    ) throws PulsarClientException {
        try (Producer<String> producer =
            client.newProducer(Schema.STRING).topic(topc).create()) {
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

    private void createTopic() throws PulsarClientException, PulsarAdminException {
        try (PulsarAdmin admin = PulsarSingleMessageNonPersistIT.admin()) {
            admin.brokers().updateDynamicConfiguration("forceDeleteNamespaceAllowed", "true");
            admin.brokers().updateDynamicConfiguration("forceDeleteTenantAllowed", "true");
            if (!admin.tenants().getTenants().contains(PulsarSingleMessageNonPersistIT.TENANT)) {
                admin.tenants().createTenant(
                    PulsarSingleMessageNonPersistIT.TENANT,
                    TenantInfo
                        .builder()
                        .adminRoles(Set.of("tester"))
                        .allowedClusters(new HashSet<>(admin.clusters().getClusters()))
                        .build()
                );
            }
            if (!admin
                .namespaces()
                .getNamespaces(PulsarSingleMessageNonPersistIT.TENANT)
                .contains(PulsarSingleMessageNonPersistIT.TENENT_NS)) {
                admin.namespaces().createNamespace(PulsarSingleMessageNonPersistIT.TENENT_NS);
            }
            admin.topics().createNonPartitionedTopic(this.topic);
        }
    }

    private void removeTopic() throws PulsarClientException, PulsarAdminException {
        try (PulsarAdmin admin = PulsarSingleMessageNonPersistIT.admin()) {
            admin.topics().delete(this.topic, true);
            if (!admin
                .namespaces()
                .getNamespaces(PulsarSingleMessageNonPersistIT.TENANT)
                .isEmpty()) {
                admin
                    .namespaces()
                    .deleteNamespace(
                        PulsarSingleMessageNonPersistIT.TENENT_NS,
                        true
                    );
            }
            if (!admin.tenants().getTenants().contains(PulsarSingleMessageNonPersistIT.TENANT)) {
                admin.tenants().deleteTenant(PulsarSingleMessageNonPersistIT.TENANT);
            }
        }
    }

    private static PulsarAdmin admin() throws PulsarClientException {
        return PulsarAdmin.builder()
            .serviceHttpUrl(PulsarSingleMessageNonPersistIT.BS_ADMIN)
            .tlsTrustCertsFilePath(null)
            .allowTlsInsecureConnection(false)
            .build();
    }
}
