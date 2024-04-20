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

package my.test.rest.incomings.controllers.impl;

import io.helidon.http.Status;
import io.helidon.webserver.http.ServerResponse;
import jakarta.json.bind.Jsonb;
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

/**
 * Response models Create a new user.
 *
 * @since 1.0
 */
public final class CreationUserResponseModel {

    /**
     * Authentication response model.
     */
    private final Authentication model;

    /**
     * Server response.
     */
    private final ServerResponse response;

    /**
     * JSON converter.
     */
    private final Jsonb jsonb;

    /**
     * Action.
     */
    private Runnable act;

    @SuppressWarnings("PMD.ConstructorOnlyInitializesOrCallOtherConstructors")
    public CreationUserResponseModel(final ServerResponse response, final Jsonb jsonb) {
        this.response = response;
        this.jsonb = jsonb;
        this.model = new Authentication();
        this.model.setAccessToken(new Token());
        this.act = () -> {
            final String entity = this.jsonb.toJson(this.model);
            this.response.status(Status.OK_200).send(entity);
        };
    }

    Map<String, Consumer<DomainEvent>> events() {
        return Map.of(
            EmptyNameEvent.class.getName(), this::emptyName,
            EmptyPasswordEvent.class.getName(), this::emptyPassword,
            UserCreatedSuccessfullyEvent.class.getName(), this::success,
            UserAlreadyExistsEvent.class.getName(), this::userExists
        );
    }

    void send() {
        this.act.run();
    }

    @SuppressWarnings("PMD.UnusedFormalParameter")
    private void emptyName(final DomainEvent event) {
        this.act = () -> {
            final String entity = this.jsonb.toJson(new ErrorMessage("Invalid user name"));
            this.response.status(Status.FORBIDDEN_403).send(entity);
        };
    }

    @SuppressWarnings("PMD.UnusedFormalParameter")
    private void emptyPassword(final DomainEvent event) {
        this.act = () -> {
            final String entity = this.jsonb.toJson(new ErrorMessage("Invalid user password"));
            this.response.status(Status.FORBIDDEN_403).send(entity);
        };
    }

    private void success(final DomainEvent devent) {
        final UserCreatedSuccessfullyEvent event = (UserCreatedSuccessfullyEvent) devent;
        this.model.setUsername(event.userName());
        this.model.getAccessToken().setValue(event.token());
        this.model.getAccessToken().setExpirationDateTime(
            event.expiration().atZone(ZoneId.systemDefault()).toOffsetDateTime()
        );
    }

    @SuppressWarnings("PMD.UnusedFormalParameter")
    private void userExists(final DomainEvent event) {
        this.act = () -> {
            final String entity = this.jsonb.toJson(new ErrorMessage("User already exists"));
            this.response.status(Status.FORBIDDEN_403).send(entity);
        };
    }
}
