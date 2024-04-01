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
import java.util.function.Consumer;
import my.test.authorization.domain.api.AuthenticationPolicy;
import my.test.authorization.domain.api.CreationPolicy;
import my.test.authorization.domain.api.PolicyFactory;
import my.test.authorization.domain.api.UserInfo;
import my.test.authorization.domain.api.servicebus.CreateNewUserEventTransmitter;
import my.test.authorization.domain.api.servicebus.LoginEventTransmitter;
import my.test.authorization.domain.api.servicebus.LoginEventTransmitterBuilder;
import my.test.authorization.domain.api.store.UserFactory;
import my.test.authorization.domain.events.DomainEvent;
import my.test.authorization.domain.events.EventDispatcherImpl;

/**
 * Implementation of user authentication interface.
 *
 * @since 1.0
 */
public final class PolicyFactoryImpl implements PolicyFactory {

    /**
     * User factory.
     */
    private final UserFactory ufactory;

    /**
     * Login event transmitter builder.
     */
    private final LoginEventTransmitterBuilder tbuilder;

    public PolicyFactoryImpl(final UserFactory ufactory,
        final LoginEventTransmitterBuilder tbuilder) {
        this.ufactory = ufactory;
        this.tbuilder = tbuilder;
    }

    @Override
    public CreationPolicy makeCreationPolicy(final UserInfo user,
        final Map<String, Consumer<DomainEvent>> emap) {
        final CreateNewUserEventTransmitter transmitter =
            this.tbuilder.createNewUserEventTransmitter();
        return new CreationPolicyImpl(
            new EventDispatcherImpl(emap, transmitter.subscribers()),
            this.ufactory.createNewUser(user)
        );
    }

    @Override
    public AuthenticationPolicy makeAuthenticatePolicy(final UserInfo user,
        final Map<String, Consumer<DomainEvent>> emap) {
        final LoginEventTransmitter transmitter = this.tbuilder.createLoginEventTransmitter();
        return new AuthenticationPolicyImpl(
            this.ufactory.createAuthenticatedUser(user),
            new EventDispatcherImpl(emap, transmitter.subscribers())
        );
    }
}
