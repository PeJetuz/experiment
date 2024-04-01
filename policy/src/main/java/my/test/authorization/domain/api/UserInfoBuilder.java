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

package my.test.authorization.domain.api;

import java.time.LocalDateTime;

/**
 * Interface for returning user information.
 *
 * @since 1.0
 */
public interface UserInfoBuilder {
    void writeUserName(String name);

    void writeToken(String token);

    void writeTokenExpirationDate(LocalDateTime expiration);

    final class UserInfoBuilderSpy implements UserInfoBuilder {

        /**
         * Remembers the last value of the writeUserName call.
         */
        private String name;

        /**
         * Remembers the last value of the writeToken call.
         */
        private String token;

        /**
         * Remembers the last value of the writeTokenExpirationDate call.
         */
        private LocalDateTime exp;

        @Override
        public void writeUserName(final String source) {
            this.name = source;
        }

        @Override
        public void writeToken(final String source) {
            this.token = source;
        }

        @Override
        public void writeTokenExpirationDate(final LocalDateTime expiration) {
            this.exp = expiration;
        }

        public String name() {
            return this.name;
        }

        public String token() {
            return this.token;
        }

        public LocalDateTime expirationDateTime() {
            return this.exp;
        }
    }
}
