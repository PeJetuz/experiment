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
import io.helidon.webserver.http.HttpRules;
import io.helidon.webserver.http.HttpService;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;
import jakarta.json.bind.Jsonb;
import my.test.authorization.rules.AuthenticateUserInteractor;
import my.test.authorization.rules.CreateUserInteractor;
import my.test.authorization.rules.InteractorFactory;
import my.test.rest.incomings.controllers.api.dto.AuthInfo;

/**
 * Implementation of the authentication API.
 *
 * @since 1.0
 */
public final class AuthenticationServiceImpl implements HttpService {

    /**
     * Interactor factory.
     */
    private final InteractorFactory ifactory;

    /**
     * JSON converter.
     */
    private final Jsonb jsonb;

    public AuthenticationServiceImpl(final InteractorFactory ifactory, final Jsonb jsonb) {
        this.ifactory = ifactory;
        this.jsonb = jsonb;
    }

    @Override
    public void routing(final HttpRules rules) {
        rules
            .put("/create", this::create)
            .post("/login", this::login)
            .get("/logout", AuthenticationServiceImpl::logout)
            .get("/tokens/refresh", AuthenticationServiceImpl::refreshTokens);
    }

    private void create(final ServerRequest request, final ServerResponse response) {
        final AuthInfo ainfo = request.content().as(AuthInfo.class);
        final CreationUserResponseModel model = new CreationUserResponseModel(response, this.jsonb);
        final CreateUserInteractor interactor =
            this.ifactory.createNewUserInteractor(
                ainfo.getUserName(),
                ainfo.getPasswordHash(),
                model.events()
            );
        interactor.createNewUser();
        model.send();
    }

    private void login(final ServerRequest request, final ServerResponse response) {
        final AuthInfo ainfo = request.content().as(AuthInfo.class);
        final AuthenticationUserResponseModel model =
            new AuthenticationUserResponseModel(response, this.jsonb);
        final AuthenticateUserInteractor interactor =
            this.ifactory.createAuthenticateUserInteractor(
                ainfo.getUserName(),
                ainfo.getPasswordHash(),
                model.events()
            );
        interactor.authenticate();
        model.send();
    }

    @SuppressWarnings("PMD.UnusedFormalParameter")
    private static void logout(final ServerRequest request, final ServerResponse response) {
        response.status(Status.NOT_IMPLEMENTED_501).send();
    }

    @SuppressWarnings("PMD.UnusedFormalParameter")
    private static void refreshTokens(final ServerRequest request, final ServerResponse response) {
        response.status(Status.NOT_IMPLEMENTED_501).send();
    }
}
