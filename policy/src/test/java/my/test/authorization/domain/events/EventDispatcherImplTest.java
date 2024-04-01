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

package my.test.authorization.domain.events;

import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test case for event implementation.
 *
 * @since 1.0
 */
final class EventDispatcherImplTest {

    @Test
    void fire() {
        final DomainEvent unf = new UserNotFoundEvent.UserNotFoundEventImpl();
        final EventStub unfconsfirst = new EventStub();
        final EventStub unfconssec = new EventStub();
        final DomainEvent upf = new UserPasswordFailedEvent.UserPasswordFailedEventImpl();
        final EventStub upfcons = new EventStub();
        final DomainEvent uas =
            new UserAuthenticationSuccessfulEvent
                .UserAuthenticationSuccessfulEventImpl(null, null, null
            );
        final EventStub uascons = new EventStub();
        final EventDispatcherImpl subj = new EventDispatcherImpl(
            Map.of(
                unf.eventName(), unfconsfirst::acceptEvent, upf.eventName(), upfcons::acceptEvent
            ),
            Map.of(
                unf.eventName(), unfconssec::acceptEvent, uas.eventName(), uascons::acceptEvent
            ));
        subj.fire(unf);
        subj.fire(upf);
        subj.fire(uas);
        subj.fire(new IpAuthenticationLimitExceededEvent.IpAuthenticationLimitExceededEventImpl());
        subj.fire(new IpAuthenticationSuccessfulEvent.IpAuthenticationSuccessfulEventImpl());
        subj.fire(new UserAlreadyExistsEvent.UserAlreadyExistsEventImpl());
        subj.fire(new EmptyNameEvent.EmptyNameEventImpl());
        subj.fire(new EmptyPasswordEvent.EmptyPasswordEventImpl());
        Assertions.assertThat(unf).isEqualTo(unfconsfirst.event()).isEqualTo(unfconssec.event());
        Assertions.assertThat(upf).isEqualTo(upfcons.event());
        Assertions.assertThat(uas).isEqualTo(uascons.event());
    }

    private final class EventStub {

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
