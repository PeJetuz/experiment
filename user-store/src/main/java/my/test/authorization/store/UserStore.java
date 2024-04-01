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

/**
 * User store API.
 *
 * @since 1.0
 */
public interface UserStore {

    /**
     * Guest name.
     */
    String GUEST = "Guest";

    /**
     * Guest password.
     */
    String GUEST_PASSWORD = "password";

    /**
     * User not found constant.
     */
    AuthInfoValue USER_NOF_FOUND = new AuthInfoValue(null, null, null, null);

    /**
     * User already exists constant.
     */
    AuthInfoValue USER_EXISTS = new AuthInfoValue(null, null, null, null);

    AuthInfoValue findUserByName(String name);

    void updateUserInfo(AuthInfoValue entity);

    AuthInfoValue createUser(AuthInfoValue entity);

    record AuthInfoValue(String name, String password, LocalDateTime lastTime, String token) {

        public AuthInfoValue updateLastRefreshDateTime(final LocalDateTime lasttime) {
            return new AuthInfoValue(this.name, this.password, lasttime, this.token);
        }

        public AuthInfoValue updateLastRefreshDateTimeAndToken(
            final LocalDateTime lasttime,
            final String tkn
        ) {
            return new AuthInfoValue(this.name, this.password, lasttime, tkn);
        }
    }

    record Dummy(AuthInfoValue entity) implements UserStore {

        @Override
        public AuthInfoValue findUserByName(final String name) {
            return this.entity;
        }

        @Override
        public void updateUserInfo(final AuthInfoValue ent) {
            // do nothing
        }

        @Override
        public AuthInfoValue createUser(final AuthInfoValue ent) {
            return this.entity;
        }
    }
}
