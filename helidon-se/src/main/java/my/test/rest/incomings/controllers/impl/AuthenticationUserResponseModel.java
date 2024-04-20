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
import my.test.authorization.domain.events.UserAuthenticationSuccessfulEvent;
import my.test.authorization.domain.events.UserNotFoundEvent;
import my.test.authorization.domain.events.UserPasswordFailedEvent;
import my.test.rest.incomings.controllers.api.dto.Authentication;
import my.test.rest.incomings.controllers.api.dto.Token;

/**
 * User authentication response model.
 *
 * @since 1.0
 */
public final class AuthenticationUserResponseModel {

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
    public AuthenticationUserResponseModel(final ServerResponse response, final Jsonb jsonb) {
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
            UserAuthenticationSuccessfulEvent.class.getName(), this::success,
            UserNotFoundEvent.class.getName(), this::userNotFound,
            UserPasswordFailedEvent.class.getName(), this::passwordFailed
        );
    }

    void send() {
        this.act.run();
    }

    @SuppressWarnings("PMD.UnusedFormalParameter")
    private void userNotFound(final DomainEvent devent) {
        this.act = () -> {
            final String entity = this.jsonb.toJson(new ErrorMessage("User not found"));
            this.response.status(Status.UNAUTHORIZED_401).send(entity);
        };
    }

    @SuppressWarnings("PMD.UnusedFormalParameter")
    private void passwordFailed(final DomainEvent devent) {
        this.act = () -> {
            final String entity = this.jsonb.toJson(new ErrorMessage("Invalid password"));
            this.response.status(Status.UNAUTHORIZED_401).send(entity);
        };
    }

    private void success(final DomainEvent devent) {
        final UserAuthenticationSuccessfulEvent event = (UserAuthenticationSuccessfulEvent) devent;
        this.model.setUsername(event.userName());
        this.model.getAccessToken().setValue(event.token());
        this.model.getAccessToken().setExpirationDateTime(
            event.expiration().atZone(ZoneId.systemDefault()).toOffsetDateTime()
        );
    }
}
