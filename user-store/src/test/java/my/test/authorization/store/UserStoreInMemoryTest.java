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
import java.util.Random;
import my.test.authorization.store.UserStore.AuthInfoValue;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test fo for UserStoreInMemory.
 *
 * @since 1.0
 */
final class UserStoreInMemoryTest {

    /**
     * Random generator.
     */
    private final Random random = new Random();

    @Test
    void createUser() {
        final UserStore subj = new UserStoreInMemory();
        final AuthInfoValue empty = this.createEmptyAuthInfoValue();
        final AuthInfoValue result = subj.createUser(empty);
        Assertions.assertThat(result).isEqualTo(empty);
    }

    @Test
    void createUserAlreadyExists() {
        final UserStore subj = new UserStoreInMemory();
        final AuthInfoValue empty = this.createEmptyAuthInfoValue();
        subj.createUser(empty);
        final AuthInfoValue result = subj.createUser(empty);
        Assertions.assertThat(result).isEqualTo(UserStore.USER_EXISTS);
    }

    @Test
    void findUserByName() {
        final UserStore subj = new UserStoreInMemory();
        final AuthInfoValue rndvalue = this.createAuthInfoValue();
        final AuthInfoValue created = subj.createUser(rndvalue);
        final AuthInfoValue found = subj.findUserByName(rndvalue.name());
        Assertions.assertThat(created).isEqualTo(rndvalue);
        Assertions.assertThat(found).isEqualTo(rndvalue);
    }

    @Test
    void isEmptyUserExists() {
        final UserStore subj = new UserStoreInMemory();
        Assertions.assertThat(subj.findUserByName("")).isEqualTo(UserStore.USER_NOF_FOUND);
    }

    @Test
    void isGuestUserExists() {
        final UserStore subj = new UserStoreInMemory();
        Assertions.assertThat(subj.findUserByName(UserStore.GUEST))
            .isNotEqualTo(UserStore.USER_NOF_FOUND);
    }

    @Test
    void isVasyaUserExists() {
        final UserStore subj = new UserStoreInMemory();
        Assertions.assertThat(subj.findUserByName("Vasya")).isNotEqualTo(UserStore.USER_NOF_FOUND);
    }

    @Test
    void updateUserInfo() {
        final UserStore subj = new UserStoreInMemory();
        final AuthInfoValue rndvalue = this.createAuthInfoValue();
        final AuthInfoValue upvalue = this.createUpdatedAuthInfoValue(rndvalue);
        final AuthInfoValue crvalue = subj.createUser(rndvalue);
        subj.updateUserInfo(upvalue);
        final AuthInfoValue found = subj.findUserByName(rndvalue.name());
        Assertions.assertThat(crvalue).isEqualTo(rndvalue);
        Assertions.assertThat(found).isEqualTo(upvalue);
    }

    private AuthInfoValue createAuthInfoValue() {
        final String name = String.format("name %d", this.random.nextLong());
        final String password = String.format("password %d", this.random.nextLong());
        final String token = String.format("token %d", this.random.nextLong());
        final LocalDateTime exptime = LocalDateTime.now();
        return new AuthInfoValue(name, password, exptime, token);
    }

    private AuthInfoValue createEmptyAuthInfoValue() {
        final String name = String.format("name %d", this.random.nextLong());
        return new AuthInfoValue(name, null, null, null);
    }

    private AuthInfoValue createUpdatedAuthInfoValue(final AuthInfoValue entity) {
        return new AuthInfoValue(
            entity.name(),
            String.format("password %d", this.random.nextLong()),
            LocalDateTime.now().minusDays(10),
            String.format("token %d", this.random.nextLong())
        );
    }
}
