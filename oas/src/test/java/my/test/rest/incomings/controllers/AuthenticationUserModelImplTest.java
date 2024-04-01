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

package my.test.rest.incomings.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import my.test.authorization.domain.events.IpAuthenticationLimitExceededEvent;
import my.test.authorization.domain.events.UserAuthenticationSuccessfulEvent;
import my.test.authorization.domain.events.UserNotFoundEvent;
import my.test.authorization.domain.events.UserPasswordFailedEvent;
import my.test.rest.incomings.controllers.api.dto.Authentication;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * AuthenticationUserResponseModelImpl tests.
 *
 * @since 1.0
 */
final class AuthenticationUserModelImplTest {

    /**
     * Model for testing.
     */
    private final AuthenticationUserResponseModelImpl subj;

    AuthenticationUserModelImplTest() {
        this.subj = new AuthenticationUserResponseModelImpl();
    }

    @Test
    void canCreate() {
        Assertions.assertThat(this.subj.renderModel().getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void invalidUserPassword() {
        this.subj.events().get(UserPasswordFailedEvent.class.getName()).accept(
            new UserPasswordFailedEvent.UserPasswordFailedEventImpl()
        );
        Assertions.assertThat(this.subj.renderModel().getStatusCode())
            .isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void limitExceeded() {
        this.subj.events().get(IpAuthenticationLimitExceededEvent.class.getName()).accept(
            new IpAuthenticationLimitExceededEvent.IpAuthenticationLimitExceededEventImpl()
        );
        Assertions.assertThat(this.subj.renderModel().getStatusCode())
            .isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void loginSuccess() {
        final String user = "username";
        final String token = "token";
        final LocalDateTime expiration = LocalDateTime.now();
        this.subj.events().get(UserAuthenticationSuccessfulEvent.class.getName()).accept(
            new UserAuthenticationSuccessfulEvent.UserAuthenticationSuccessfulEventImpl(
                user, token, expiration
            )
        );
        final ResponseEntity<Authentication> response = this.subj.renderModel();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getUsername()).isEqualTo(user);
        Assertions.assertThat(response.getBody().getAccessToken().getValue()).isEqualTo(token);
        Assertions.assertThat(response.getBody().getAccessToken().getExpirationDateTime())
            .isEqualTo(
                expiration.atZone(ZoneId.systemDefault()).toOffsetDateTime()
            );
    }

    @Test
    void userNotFound() {
        this.subj.events().get(UserNotFoundEvent.class.getName()).accept(
            new UserNotFoundEvent.UserNotFoundEventImpl()
        );
        Assertions.assertThat(this.subj.renderModel().getStatusCode())
            .isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
