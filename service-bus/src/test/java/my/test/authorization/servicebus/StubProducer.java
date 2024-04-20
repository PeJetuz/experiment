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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
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

/**
 * Stub for kafka producer.
 *
 * @since 1.0
 */
@SuppressWarnings("PMD.TooManyMethods")
final class StubProducer implements Producer<String, String> {

    /**
     * Message.
     */
    private ProducerRecord<String, String> message;

    /**
     * Was flush called?
     */
    private boolean cflush;

    @Override
    public void initTransactions() {
        //do nothing
    }

    @Override
    public void beginTransaction() throws ProducerFencedException {
        //do nothing
    }

    @Override
    public void sendOffsetsToTransaction(final Map<TopicPartition, OffsetAndMetadata> offsets,
        final String id) throws ProducerFencedException {
        //do nothing
    }

    @Override
    public void sendOffsetsToTransaction(final Map<TopicPartition, OffsetAndMetadata> offsets,
        final ConsumerGroupMetadata group) throws ProducerFencedException {
        //do nothing
    }

    @Override
    public void commitTransaction() throws ProducerFencedException {
        //do nothing
    }

    @Override
    public void abortTransaction() throws ProducerFencedException {
        //do nothing
    }

    @Override
    public Future<RecordMetadata> send(final ProducerRecord<String, String> record) {
        this.message = record;
        return null;
    }

    @Override
    public Future<RecordMetadata> send(final ProducerRecord<String, String> record,
        final Callback callback
    ) {
        return null;
    }

    @Override
    public void flush() {
        this.cflush = true;
    }

    @Override
    public List<PartitionInfo> partitionsFor(final String topic) {
        return Collections.emptyList();
    }

    @Override
    public Map<MetricName, ? extends Metric> metrics() {
        return Collections.emptyMap();
    }

    @Override
    public void close() {
        //do nothing
    }

    @Override
    public void close(final Duration timeout) {
        //do nothing
    }

    public ProducerRecord<String, String> msg() {
        return this.message;
    }

    public boolean isCflush() {
        return this.cflush;
    }
}
