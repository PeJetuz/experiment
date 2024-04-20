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
import my.test.authorization.domain.events.DomainEvent;
import my.test.authorization.domain.events.UserAuthenticationSuccessfulEvent;
import my.test.authorization.domain.events.UserNotFoundEvent;
import my.test.authorization.domain.events.UserPasswordFailedEvent;
import my.test.authorization.store.UserStore.AuthInfoValue;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test fo for AuthenticatedUserImpl.
 *
 * @since 1.0
 */
final class AuthenticatedUserImplTest {

    /**
     * User name.
     */
    private static final String USER_NAME = "test name";

    /**
     * User password.
     */
    private static final String USER_PASSWORD = "test password";

    /**
     * Expected token.
     */
    private static final String EXPECTED_TOKEN = "ABCDEF";

    /**
     * Expired time.
     */
    private static final LocalDateTime EXPIRED_TOKEN = LocalDateTime.now().minusMinutes(11L);

    @Test
    void authenticateIncorrectUserPassword() {
        final DomainEvent event = new AuthenticatedUserImpl(
            new UserStore.Dummy(createAuthInfoValue(null)),
            createEmptyUserInfo()
        ).authenticate();
        Assertions.assertThat(event).isInstanceOf(UserPasswordFailedEvent.class);
    }

    @Test
    void authenticateUserNotFound() {
        final DomainEvent event = new AuthenticatedUserImpl(
            new UserStore.Dummy(UserStore.USER_NOF_FOUND),
            createEmptyUserInfo()
        ).authenticate();
        Assertions.assertThat(event).isInstanceOf(UserNotFoundEvent.class);
    }

    @Test
    void authenticateUserSuccess() {
        final LocalDateTime exptime = LocalDateTime.now();
        final DomainEvent event = new AuthenticatedUserImpl(
            new UserStore.Dummy(createAuthInfoValue(exptime)),
            createUserInfoWithPassword()
        ).authenticate();
        Assertions.assertThat(event).isInstanceOf(UserAuthenticationSuccessfulEvent.class);
        final UserAuthenticationSuccessfulEvent result = (UserAuthenticationSuccessfulEvent) event;
        Assertions.assertThat(result.userName()).isEqualTo(AuthenticatedUserImplTest.USER_NAME);
        Assertions.assertThat(result.token()).isEqualTo(AuthenticatedUserImplTest.EXPECTED_TOKEN);
        Assertions.assertThat(result.expiration()).isAfterOrEqualTo(exptime);
    }

    @Test
    void authenticateUserSuccessUpdateExpirationTimeAndToken() {
        final AuthInfoValue entity = createAuthInfoValue(AuthenticatedUserImplTest.EXPIRED_TOKEN);
        final DomainEvent event = new AuthenticatedUserImpl(
            new UserStore.Dummy(entity),
            createUserInfoWithPassword()
        ).authenticate();
        Assertions.assertThat(event).isInstanceOf(UserAuthenticationSuccessfulEvent.class);
        final UserAuthenticationSuccessfulEvent result = (UserAuthenticationSuccessfulEvent) event;
        final LocalDateTime now = LocalDateTime.now();
        Assertions.assertThat(result.userName()).isEqualTo(AuthenticatedUserImplTest.USER_NAME);
        Assertions.assertThat(result.token())
            .isNotEqualTo(AuthenticatedUserImplTest.EXPECTED_TOKEN);
        Assertions.assertThat(result.expiration())
            .isAfter(AuthenticatedUserImplTest.EXPIRED_TOKEN)
            .isBeforeOrEqualTo(now);
    }

    private static AuthInfoValue createAuthInfoValue(final LocalDateTime exptime) {
        return new AuthInfoValue(
            AuthenticatedUserImplTest.USER_NAME,
            AuthenticatedUserImplTest.USER_PASSWORD,
            exptime,
            AuthenticatedUserImplTest.EXPECTED_TOKEN
        );
    }

    private static UserInfo createEmptyUserInfo() {
        return new UserInfo(null, null);
    }

    private static UserInfo createUserInfoWithPassword() {
        return new UserInfo(null, AuthenticatedUserImplTest.USER_PASSWORD);
    }
}
