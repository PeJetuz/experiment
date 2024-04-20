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

import java.util.Properties;
import my.test.authorization.domain.api.servicebus.LoginEventTransmitter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test cases for EventTransmitterFactoryImpl.
 *
 * @since 1.0
 */
final class EventTransmitterFactoryImplTest {

    @Test
    void canCreate() {
        final EventTransmitterFactoryImpl subj =
            new EventTransmitterFactoryImpl(this.generateKafkaProperties(), null);
        Assertions.assertThat(subj.createLoginEventTransmitter())
            .isInstanceOf(LoginEventTransmitter.class);
    }

    private Properties generateKafkaProperties() {
        final Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16_384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 335_544);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return props;
    }
}
