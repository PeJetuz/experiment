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

import my.test.authorization.domain.api.CreationPolicy;
import my.test.authorization.domain.api.store.NewUser;
import my.test.authorization.domain.events.DomainEvent;
import my.test.authorization.domain.events.EventDispatcher;

/**
 * Implementation of user authentication interface.
 *
 * @since 1.0
 */
final class CreationPolicyImpl implements CreationPolicy {

    /**
     * Event dispatcher.
     */
    private final EventDispatcher<DomainEvent> dispatcher;

    /**
     * New user.
     */
    private final NewUser user;

    CreationPolicyImpl(final EventDispatcher<DomainEvent> dispatcher, final NewUser user) {
        this.dispatcher = dispatcher;
        this.user = user;
    }

    @Override
    public void create() {
        this.dispatcher.fire(this.user.create());
    }
}
