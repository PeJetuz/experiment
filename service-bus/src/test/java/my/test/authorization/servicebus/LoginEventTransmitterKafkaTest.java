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
import my.test.authorization.domain.events.IpAuthenticationLimitExceededEvent;
import my.test.authorization.domain.events.UserAuthenticationSuccessfulEvent;
import my.test.authorization.domain.events.UserNotFoundEvent;
import my.test.authorization.domain.events.UserPasswordFailedEvent;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test for LoginEventTransmitterKafka.
 *
 * @since 1.0
 */
final class LoginEventTransmitterKafkaTest {

    /**
     * Stub of producer.
     */
    private final StubProducer producer = new StubProducer();

    /**
     * Event map.
     */
    private final Map<String, Consumer<DomainEvent>> subscribers =
        new EventTransmitterFactoryImpl(this.producer, "topic")
            .createLoginEventTransmitter()
            .subscribers();

    @Test
    void sendLoginSuccessEvent() {
        this.subscribers.get(UserAuthenticationSuccessfulEvent.class.getName()).accept(
            new UserAuthenticationSuccessfulEvent.UserAuthenticationSuccessfulEventImpl(
                "name", "token", null
            )
        );
        Assertions.assertThat(this.producer.msg().topic()).isEqualTo("topic");
        Assertions.assertThat(this.producer.msg().key()).isEqualTo("name");
        Assertions.assertThat(this.producer.msg().value())
            .isEqualTo(UserAuthenticationSuccessfulEvent.class.getName());
        Assertions.assertThat(this.producer.isCflush()).isTrue();
    }

    @Test
    void sendUserNotFoundEvent() {
        this.subscribers.get(UserNotFoundEvent.class.getName()).accept(
            new UserNotFoundEvent.UserNotFoundEventImpl()
        );
        Assertions.assertThat(this.producer.isCflush()).isFalse();
    }

    @Test
    void sendPasswordFailedEvent() {
        this.subscribers.get(UserPasswordFailedEvent.class.getName()).accept(
            new UserPasswordFailedEvent.UserPasswordFailedEventImpl()
        );
        Assertions.assertThat(this.producer.isCflush()).isFalse();
    }

    @Test
    void sendLimitExceededEvent() {
        this.subscribers.get(IpAuthenticationLimitExceededEvent.class.getName()).accept(
            new IpAuthenticationLimitExceededEvent.IpAuthenticationLimitExceededEventImpl()
        );
        Assertions.assertThat(this.producer.isCflush()).isFalse();
    }
}
