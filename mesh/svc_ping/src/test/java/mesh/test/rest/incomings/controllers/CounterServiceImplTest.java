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

package mesh.test.rest.incomings.controllers;

import java.util.concurrent.atomic.AtomicLong;
import mesh.test.rest.incomings.exceptions.MyException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Error testing for AuthenticationResource.ping.
 *
 * @since 1.0
 */
final class CounterServiceImplTest {

    /**
     * Object under test.
     */
    private final CounterServiceImpl subj = new CounterServiceImpl(0);

    @Test
    void canCreate() {
        Assertions.assertThat(new CounterServiceImpl(0)).isNotNull();
    }

    @Test
    void count() {
        Assertions.assertThat(this.subj.count()).isEqualTo(1L);
    }

    @Test
    void ping() {
        Assertions.assertThat(this.subj.ping()).isEqualTo(1L);
        Assertions.assertThat(this.subj.count()).isEqualTo(2L);
    }

    @Test
    void reset() {
        Assertions.assertThat(this.subj.ping()).isEqualTo(1L);
        Assertions.assertThat(this.subj.reset()).isEqualTo(2L);
        Assertions.assertThat(this.subj.count()).isEqualTo(1L);
    }

    @Test
    void pingError() {
        for (int cnt = 1; cnt < 11; cnt += 1) {
            Assertions.assertThat(this.subj.ping()).isEqualTo(cnt);
        }
        for (int cnt = 1; cnt < 5; cnt += 1) {
            Assertions.assertThatExceptionOfType(MyException.class)
                .isThrownBy(this.subj::ping)
                .isInstanceOf(MyException.class);
        }
        Assertions.assertThat(this.subj.ping()).isEqualTo(15);
    }

    @Test
    void pingDelayError() {
        final CounterServiceImpl srv = new CounterServiceImpl(
            new AtomicLong(1), () -> {
            throw new InterruptedException();
        });
        for (int cnt = 1; cnt < 11; cnt += 1) {
            Assertions.assertThat(srv.ping()).isEqualTo(cnt);
        }
        for (int cnt = 1; cnt < 5; cnt += 1) {
            Assertions.assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(srv::ping)
                .isInstanceOf(RuntimeException.class);
        }
        Assertions.assertThat(srv.ping()).isEqualTo(15);
    }
}
