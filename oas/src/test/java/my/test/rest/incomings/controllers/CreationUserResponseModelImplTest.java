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
import my.test.authorization.domain.events.EmptyNameEvent;
import my.test.authorization.domain.events.EmptyPasswordEvent;
import my.test.authorization.domain.events.UserAlreadyExistsEvent;
import my.test.authorization.domain.events.UserCreatedSuccessfullyEvent;
import my.test.rest.incomings.controllers.api.dto.Authentication;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * CreationUserResponseModelImpl tests.
 *
 * @since 1.0
 */
final class CreationUserResponseModelImplTest {

    /**
     * Model.
     */
    private final CreationUserResponseModelImpl subj;

    CreationUserResponseModelImplTest() {
        this.subj = new CreationUserResponseModelImpl();
    }

    @Test
    void canCreate() {
        Assertions.assertThat(this.subj.renderModel().getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void emptyName() {
        this.subj.events().get(EmptyNameEvent.class.getName()).accept(
            new EmptyNameEvent.EmptyNameEventImpl()
        );
        Assertions.assertThat(this.subj.renderModel().getStatusCode())
            .isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void emptyPassword() {
        this.subj.events().get(EmptyPasswordEvent.class.getName()).accept(
            new EmptyPasswordEvent.EmptyPasswordEventImpl()
        );
        Assertions.assertThat(this.subj.renderModel().getStatusCode())
            .isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void success() {
        final String user = "username";
        final String token = "token";
        final LocalDateTime expiration = LocalDateTime.now();
        this.subj.events().get(UserCreatedSuccessfullyEvent.class.getName()).accept(
            new UserCreatedSuccessfullyEvent.UserCreatedSuccessfullyEventImpl(
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
    void userExists() {
        this.subj.events().get(UserAlreadyExistsEvent.class.getName()).accept(
            new UserAlreadyExistsEvent.UserAlreadyExistsEventImpl()
        );
        Assertions.assertThat(this.subj.renderModel().getStatusCode())
            .isEqualTo(HttpStatus.FORBIDDEN);
    }
}
