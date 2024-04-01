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
import my.test.authorization.domain.api.servicebus.CreateNewUserEventTransmitter;
import my.test.authorization.domain.api.servicebus.LoginEventTransmitter;
import my.test.authorization.domain.api.servicebus.LoginEventTransmitterBuilder;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

/**
 * Implementation of LoginEventTransmitterBuilder for kafka.
 */
public final class LoginEventTransmitterBuilderImpl implements LoginEventTransmitterBuilder {

    /**
     * Kafka producer.
     */
    private final Producer<String, String> producer;

    /**
     * Topic name.
     */
    private final String topic;

    public LoginEventTransmitterBuilderImpl(final Properties config, final String topic) {
        this.producer = new KafkaProducer<>(config);
        this.topic = topic;
    }

    @Override
    public LoginEventTransmitter createLoginEventTransmitter(final String user) {
        return new LoginEventTransmitterImpl(user, this.producer, this.topic);
    }

    @Override
    public CreateNewUserEventTransmitter createNewUserEventTransmitter(final String user) {
        return new CreateNewUserEventTransmitter.Dummy();
    }
}
