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
import my.test.authorization.domain.api.servicebus.LoginEventTransmitter;
import my.test.authorization.domain.events.DomainEvent;
import my.test.authorization.domain.events.IpAuthenticationLimitExceededEvent;
import my.test.authorization.domain.events.UserAuthenticationSuccessfulEvent;
import my.test.authorization.domain.events.UserNotFoundEvent;
import my.test.authorization.domain.events.UserPasswordFailedEvent;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * Implementation of the system bus via kafka.
 *
 * @since 1.0
 */
public final class LoginEventTransmitterKafka implements LoginEventTransmitter {

    /**
     * Kafka producer.
     */
    private final Producer<String, String> producer;

    /**
     * Topic.
     */
    private final String topic;

    public LoginEventTransmitterKafka(final Producer<String, String> producer, final String topic) {
        this.producer = producer;
        this.topic = topic;
    }

    @Override
    public Map<String, Consumer<DomainEvent>> subscribers() {
        return Map.of(
            UserAuthenticationSuccessfulEvent.class.getName(), this::success,
            UserNotFoundEvent.class.getName(), this::userNotFound,
            UserPasswordFailedEvent.class.getName(), this::passwordFailed,
            IpAuthenticationLimitExceededEvent.class.getName(), this::limitExceeded
        );
    }

    private void send(final String user, final String event) {
        final ProducerRecord<String, String> msg = new ProducerRecord<>(this.topic, user, event);
        this.producer.send(msg);
        this.producer.flush();
    }

    /**
     * At the development stage.
     *
     * @param devent Event.
     * @checkstyle NonStaticMethodCheck (5 lines)
     */
    @SuppressWarnings("PMD.UnusedFormalParameter")
    private void limitExceeded(final DomainEvent devent) {
        // do nothing
    }

    /**
     * At the development stage.
     *
     * @param devent Event.
     * @checkstyle NonStaticMethodCheck (5 lines)
     */
    @SuppressWarnings("PMD.UnusedFormalParameter")
    private void passwordFailed(final DomainEvent devent) {
        // do nothing
    }

    private void success(final DomainEvent devent) {
        final UserAuthenticationSuccessfulEvent event = (UserAuthenticationSuccessfulEvent) devent;
        this.send(event.userName(), event.eventName());
    }

    /**
     * At the development stage.
     *
     * @param devent Event.
     * @checkstyle NonStaticMethodCheck (5 lines)
     */
    @SuppressWarnings("PMD.UnusedFormalParameter")
    private void userNotFound(final DomainEvent devent) {
        // do nothing
    }
}
