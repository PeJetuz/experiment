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

package my.test.authorization.domain.impl;

import java.util.Map;
import my.test.authorization.domain.api.CreationPolicy;
import my.test.authorization.domain.api.UserInfo;
import my.test.authorization.domain.api.servicebus.EventTransmitterFactory;
import my.test.authorization.domain.api.store.NewUser;
import my.test.authorization.domain.api.store.UserFactory;
import my.test.authorization.domain.events.DomainEvent;
import my.test.authorization.domain.events.UserCreatedSuccessfullyEvent;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test for CreationPolicyImpl.
 *
 * @since 1.0
 */
final class CreationPolicyImplTest {

    @Test
    void canCreateDummy() {
        final CreationPolicy.Dummy dummy = new CreationPolicy.Dummy();
        dummy.create();
    }

    @Test
    void successCreateNewUser() {
        final UserCreatedSuccessfullyEvent event =
            new UserCreatedSuccessfullyEvent.UserCreatedSuccessfullyEventImpl(null, null, null);
        final EventConsumer consumer = new EventConsumer();
        final CreationPolicy subj = new PolicyFactoryImpl(
            new UserFactory.UserFactorySpy(
                null,
                new NewUser.Stub(event)
            ),
            new EventTransmitterFactory.Stub()
        ).makeCreationPolicy(
            createEmptyInfo(),
            Map.of(
                UserCreatedSuccessfullyEvent.class.getName(),
                consumer::acceptEvent
        ));
        subj.create();
        Assertions.assertThat(subj).isInstanceOf(CreationPolicyImpl.class);
        Assertions.assertThat(consumer.event()).isEqualTo(event);
    }

    private static UserInfo createEmptyInfo() {
        return new UserInfo(null, null);
    }

    private final class EventConsumer {
        /**
         * Remembers the event.
         */
        private DomainEvent evnt;

        @SuppressWarnings("PMD.UnusedPrivateMethod")
        private void acceptEvent(final DomainEvent evn) {
            this.evnt = evn;
        }

        private DomainEvent event() {
            return this.evnt;
        }
    }
}
