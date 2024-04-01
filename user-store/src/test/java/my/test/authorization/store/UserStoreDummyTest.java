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

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test fo for UserStore.Dummy.
 *
 * @since 1.0
 */
final class UserStoreDummyTest {

    @Test
    void findUser() {
        final UserStore.Dummy store = new UserStore.Dummy(UserStore.USER_NOF_FOUND);
        Assertions.assertThat(store.findUserByName(null)).isEqualTo(UserStore.USER_NOF_FOUND);
    }

    @Test
    void createUser() {
        final UserStore.Dummy store = new UserStore.Dummy(UserStore.USER_EXISTS);
        Assertions.assertThat(store.createUser(null)).isEqualTo(UserStore.USER_EXISTS);
    }

    @Test
    void update() {
        final UserStore.Dummy store = new UserStore.Dummy(UserStore.USER_NOF_FOUND);
        store.updateUserInfo(null);
    }
}
