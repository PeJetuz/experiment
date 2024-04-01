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

package my.test.authorization.rules.impl;

import java.util.Map;
import java.util.function.Consumer;
import my.test.authorization.domain.api.AuthenticationPolicy;
import my.test.authorization.domain.api.CreationPolicy;
import my.test.authorization.domain.api.PolicyFactory;
import my.test.authorization.domain.api.UserInfo;
import my.test.authorization.domain.events.DomainEvent;
import my.test.authorization.rules.AuthenticateUserInteractor;
import my.test.authorization.rules.CreateUserInteractor;
import my.test.authorization.rules.InteractorFactory;

/**
 * Implementation of an authentication factory.
 *
 * @since 1.0
 */
public final class InteractorFactoryImpl implements InteractorFactory {

    /**
     * Factory of business rules.
     */
    private final PolicyFactory pfactory;

    public InteractorFactoryImpl(final PolicyFactory pfactory) {
        this.pfactory = pfactory;
    }

    @Override
    public CreateUserInteractor createNewUserInteractor(
        final String uname,
        final String password,
        final Map<String, Consumer<DomainEvent>> subscribers
    ) {
        final UserInfo user = new UserInfo(uname, password);
        final CreationPolicy policy =
            this.pfactory.makeCreationPolicy(user, subscribers);
        return new CreateUserInteractorImpl(policy);
    }

    @Override
    public AuthenticateUserInteractor createAuthenticateUserInteractor(
        final String uname,
        final String password,
        final Map<String, Consumer<DomainEvent>> subscribers
    ) {
        final UserInfo user = new UserInfo(uname, password);
        final AuthenticationPolicy policy =
            this.pfactory.makeAuthenticatePolicy(user, subscribers);
        return new AuthenticateUserInteractorImpl(policy);
    }
}
