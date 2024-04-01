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

import java.time.ZoneId;
import java.util.Map;
import java.util.function.Consumer;
import my.test.authorization.domain.events.DomainEvent;
import my.test.authorization.domain.events.IpAuthenticationLimitExceededEvent;
import my.test.authorization.domain.events.UserAuthenticationSuccessfulEvent;
import my.test.authorization.domain.events.UserNotFoundEvent;
import my.test.authorization.domain.events.UserPasswordFailedEvent;
import my.test.rest.incomings.controllers.api.dto.Authentication;
import my.test.rest.incomings.controllers.api.dto.Token;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * User authentication response model.
 *
 * @since 1.0
 */
final class AuthenticationUserResponseModelImpl {

    /**
     * Automatically generated OpenAPI response class.
     */
    private final Authentication auth;

    /**
     * Response model.
     */
    private ResponseEntity<Authentication> model;

    AuthenticationUserResponseModelImpl() {
        this.auth = new Authentication().accessToken(new Token());
        this.model = ResponseEntity.ok(this.auth);
    }

    ResponseEntity<Authentication> renderModel() {
        return this.model;
    }

    Map<String, Consumer<DomainEvent>> events() {
        return Map.of(
            UserAuthenticationSuccessfulEvent.class.getName(), this::success,
            UserNotFoundEvent.class.getName(), this::userNotFound,
            UserPasswordFailedEvent.class.getName(), this::passwordFailed,
            IpAuthenticationLimitExceededEvent.class.getName(), this::limitExceeded
        );
    }

    @SuppressWarnings("PMD.UnusedFormalParameter")
    private void limitExceeded(final DomainEvent devent) {
        this.model = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @SuppressWarnings("PMD.UnusedFormalParameter")
    private void userNotFound(final DomainEvent devent) {
        this.model = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @SuppressWarnings("PMD.UnusedFormalParameter")
    private void passwordFailed(final DomainEvent devent) {
        this.model = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    private void success(final DomainEvent devent) {
        final UserAuthenticationSuccessfulEvent event = (UserAuthenticationSuccessfulEvent) devent;
        this.auth.username(event.userName());
        this.auth.getAccessToken().value(event.token());
        this.auth.getAccessToken()
            .expirationDateTime(
                event.expiration().atZone(ZoneId.systemDefault()).toOffsetDateTime()
            );
    }
}
