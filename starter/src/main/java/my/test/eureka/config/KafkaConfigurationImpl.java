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

package my.test.eureka.config;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Implementation a kafka configuration provider.
 *
 * @since 1.0
 */
@Component
@SuppressWarnings("PMD.FinalFieldCouldBeStatic")
public final class KafkaConfigurationImpl implements KafkaConfiguration {

    /**
     * Server address.
     */
    @Value("${kafka.bootstrap.servers}")
    private final String servers = "localhost:9092";

    /**
     * Acknowledgements flag.
     */
    @Value("${kafka.acks}")
    private final String acks = "all";

    /**
     * Number of retries.
     */
    @Value("${kafka.retries}")
    private final String retries = "0";

    /**
     * Message batch size.
     */
    @Value("${kafka.batch.size}")
    private final String bsize = "16384";

    /**
     * Waiting between sending packets.
     */
    @Value("${kafka.linger.ms}")
    private final String linger = "1";

    /**
     * Buffer size.
     */
    @Value("${kafka.buffer.memory}")
    private final String buffer = "409600";

    /**
     * Key serializer.
     */
    @Value("${kafka.key.serializer}")
    private final String kserializer = "org.apache.kafka.common.serialization.StringSerializer";

    /**
     * Value serializer.
     */
    @Value("${kafka.value.serializer}")
    private final String vserializer = "org.apache.kafka.common.serialization.StringSerializer";

    /**
     * Topic.
     */
    @Value("${authentication.logon.topic.name}")
    private final String topic = "logon";

    @Override
    public Properties kafkaProperties() {
        final Properties kprop = new Properties();
        kprop.put("bootstrap.servers", this.servers);
        kprop.put("acks", this.acks);
        kprop.put("retries", Integer.valueOf(this.retries));
        kprop.put("batch.size", Integer.valueOf(this.bsize));
        kprop.put("linger.ms", Long.valueOf(this.linger));
        kprop.put("buffer.memory", Long.valueOf(this.buffer));
        kprop.put("key.serializer", this.kserializer);
        kprop.put("value.serializer", this.vserializer);
        return kprop;
    }

    @Override
    public String topicName() {
        return this.topic;
    }
}
