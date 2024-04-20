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
import my.test.authorization.domain.api.UserInfo;
import my.test.authorization.domain.api.store.NewUser;
import my.test.authorization.domain.events.DomainEvent;
import my.test.authorization.domain.events.UserAlreadyExistsEvent;
import my.test.authorization.domain.events.UserCreatedSuccessfullyEvent;
import my.test.authorization.store.UserStore.AuthInfoValue;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test fo for NewUserImpl.
 *
 * @since 1.0
 */
final class NewUserImplTest {

    @Test
    void alreadyExists() {
        final NewUser subj = new NewUserImpl(
            new UserStore.Dummy(UserStore.USER_EXISTS),
            createEmptyUserInfo()
        );
        final DomainEvent event = subj.create();
        Assertions.assertThat(event).isInstanceOf(UserAlreadyExistsEvent.class);
    }

    @Test
    void success() {
        final String name = "name";
        final String token = "token";
        final LocalDateTime exptime = LocalDateTime.now();
        final AuthInfoValue entity = new AuthInfoValue(name, "password", exptime, token);
        final NewUser subj = new NewUserImpl(
            new UserStore.Dummy(entity),
            createEmptyUserInfo()
        );
        final DomainEvent event = subj.create();
        Assertions.assertThat(event).isInstanceOf(UserCreatedSuccessfullyEvent.class);
        final UserCreatedSuccessfullyEvent result = (UserCreatedSuccessfullyEvent) event;
        Assertions.assertThat(result.userName()).isEqualTo(name);
        Assertions.assertThat(result.token()).isEqualTo(token);
        Assertions.assertThat(result.expiration()).isEqualTo(exptime);
    }

    private static UserInfo createEmptyUserInfo() {
        return new UserInfo("name", "pass");
    }
}
