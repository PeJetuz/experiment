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
import my.test.authorization.domain.events.EmptyNameEvent;
import my.test.authorization.domain.events.EmptyPasswordEvent;
import my.test.authorization.domain.events.UserAlreadyExistsEvent;
import my.test.authorization.domain.events.UserCreatedSuccessfullyEvent;
import my.test.rest.incomings.controllers.api.dto.Authentication;
import my.test.rest.incomings.controllers.api.dto.Token;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Response models Create a new user.
 *
 * @since 1.0
 */
final class CreationUserResponseModelImpl {

    /**
     * Automatically generated OpenAPI response class.
     */
    private final Authentication auth;

    /**
     * Response model.
     */
    private ResponseEntity<Authentication> model;

    CreationUserResponseModelImpl() {
        this.auth = new Authentication().accessToken(new Token());
        this.model = ResponseEntity.ok(this.auth);
    }

    Map<String, Consumer<DomainEvent>> events() {
        return Map.of(
            EmptyNameEvent.class.getName(), this::emptyName,
            EmptyPasswordEvent.class.getName(), this::emptyPassword,
            UserCreatedSuccessfullyEvent.class.getName(), this::success,
            UserAlreadyExistsEvent.class.getName(), this::userExists
        );
    }

    ResponseEntity<Authentication> renderModel() {
        return this.model;
    }

    @SuppressWarnings("PMD.UnusedFormalParameter")
    private void emptyName(final DomainEvent event) {
        this.model = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @SuppressWarnings("PMD.UnusedFormalParameter")
    private void emptyPassword(final DomainEvent event) {
        this.model = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    private void success(final DomainEvent devent) {
        final UserCreatedSuccessfullyEvent event = (UserCreatedSuccessfullyEvent) devent;
        this.auth.username(event.userName());
        this.auth.getAccessToken().value(event.token());
        this.auth.getAccessToken().expirationDateTime(
            event.expiration().atZone(ZoneId.systemDefault()).toOffsetDateTime()
        );
    }

    @SuppressWarnings("PMD.UnusedFormalParameter")
    private void userExists(final DomainEvent event) {
        this.model = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
