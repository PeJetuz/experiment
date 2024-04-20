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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import my.test.authorization.domain.api.AuthenticationPolicy;
import my.test.authorization.domain.api.CreationPolicy;
import my.test.authorization.domain.events.DomainEvent;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test for PolicyFactorySpy.
 *
 * @since 1.0
 */
final class PolicyFactorySpyTest {

    @Test
    void canCreateSpy() {
        final Map<String, Consumer<DomainEvent>> authemap = new HashMap<>();
        final Map<String, Consumer<DomainEvent>> cremap = new HashMap<>();
        final PolicyFactorySpy subj = new PolicyFactorySpy();
        Assertions.assertThat(subj.makeAuthenticatePolicy(null, authemap))
            .isInstanceOf(AuthenticationPolicy.Dummy.class);
        Assertions.assertThat(subj.makeCreationPolicy(null, cremap))
            .isInstanceOf(CreationPolicy.Dummy.class);
        Assertions.assertThat(subj.authEventMap()).isEqualTo(authemap);
        Assertions.assertThat(subj.crEventMap()).isEqualTo(cremap);
    }
}
