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

package my.test.authorization.store;

import java.time.LocalDateTime;
import my.test.authorization.store.UserStore.AuthInfoValue;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test fo for AuthInfoValue.
 *
 * @since 1.0
 */
final class AuthInfoValueTest {

    @Test
    void updateLastRefreshDateTimeTest() throws InterruptedException {
        final LocalDateTime exptime = LocalDateTime.now();
        final LocalDateTime wtime = exptime.plusNanos(1L);
        final AuthInfoValue expected = new AuthInfoValue("name", "password", exptime, "token");
        final AuthInfoValue actual = expected.updateLastRefreshDateTime(wtime);
        Assertions.assertThat(actual).isNotEqualTo(expected);
        Assertions.assertThat(actual.name()).isEqualTo(expected.name());
        Assertions.assertThat(actual.password()).isEqualTo(expected.password());
        Assertions.assertThat(actual.token()).isEqualTo(expected.token());
        Assertions.assertThat(expected.lastTime()).isEqualTo(exptime);
        Assertions.assertThat(actual.lastTime()).isEqualTo(wtime);
    }

    @Test
    void updateLastRefreshDateTimeAndTokenTest() throws InterruptedException {
        final LocalDateTime exptime = LocalDateTime.now();
        final LocalDateTime wtime = exptime.plusNanos(1L);
        final String exptoken = "token";
        final String wtoken = "new token";
        final AuthInfoValue expected = new AuthInfoValue("name", "password", exptime, exptoken);
        final AuthInfoValue actual = expected.updateLastRefreshDateTimeAndToken(wtime, wtoken);
        Assertions.assertThat(actual).isNotEqualTo(expected);
        Assertions.assertThat(actual.name()).isEqualTo(expected.name());
        Assertions.assertThat(actual.password()).isEqualTo(expected.password());
        Assertions.assertThat(actual.token()).isEqualTo(wtoken);
        Assertions.assertThat(expected.lastTime()).isEqualTo(exptime);
        Assertions.assertThat(actual.lastTime()).isEqualTo(wtime);
    }
}
