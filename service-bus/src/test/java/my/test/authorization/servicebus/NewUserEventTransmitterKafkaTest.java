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

import java.util.Map;
import java.util.function.Consumer;
import my.test.authorization.domain.events.DomainEvent;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test for CreateNewUserEventTransmitter.
 *
 * @since 1.0
 */
final class NewUserEventTransmitterKafkaTest {
    /**
     * Stub of producer.
     */
    private final StubProducer producer = new StubProducer();

    /**
     * Event map.
     */
    private final Map<String, Consumer<DomainEvent>> subscribers =
        new EventTransmitterFactoryImpl(this.producer, "topic")
            .createNewUserEventTransmitter()
            .subscribers();

    @Test
    void sendNewUserEvent() {
        Assertions.assertThat(this.subscribers).isNotNull();
    }
}
