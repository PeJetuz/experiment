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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Map;
import java.util.function.Consumer;
import my.test.authorization.domain.api.AuthenticationPolicy;
import my.test.authorization.domain.api.CreationPolicy;
import my.test.authorization.domain.api.PolicyFactory;
import my.test.authorization.domain.api.UserInfo;
import my.test.authorization.domain.events.DomainEvent;

/**
 * Spy class for testing.
 *
 * @since 1.0
 */
public final class PolicyFactorySpy implements PolicyFactory {

    /**
     * Dummy of AuthenticationPolicy.
     */
    private final AuthenticationPolicy authpolicy;

    /**
     * Dummy of CreationPolicy.
     */
    private final CreationPolicy crpolicy;

    /**
     * Remembers the authentication event map.
     */
    private Map<String, Consumer<DomainEvent>> authemap;

    /**
     * Remembers the creation new user event map.
     */
    private Map<String, Consumer<DomainEvent>> cremap;

    public PolicyFactorySpy() {
        this.authpolicy = new AuthenticationPolicy.Dummy();
        this.crpolicy = new CreationPolicy.Dummy();
    }

    @Override
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public AuthenticationPolicy makeAuthenticatePolicy(final UserInfo user,
        final Map<String, Consumer<DomainEvent>> emap) {
        this.authemap = emap;
        return this.authpolicy;
    }

    @Override
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public CreationPolicy makeCreationPolicy(final UserInfo user,
        final Map<String, Consumer<DomainEvent>> emap) {
        this.cremap = emap;
        return this.crpolicy;
    }

    /**
     * Authentication event map.
     * @return Authentication event map.
     */
    @SuppressFBWarnings("EI_EXPOSE_REP")
    public Map<String, Consumer<DomainEvent>> authEventMap() {
        return this.authemap;
    }

    /**
     * Creation new user event map.
     * @return Creation new user event map.
     */
    @SuppressFBWarnings("EI_EXPOSE_REP")
    public Map<String, Consumer<DomainEvent>> crEventMap() {
        return this.cremap;
    }
}
