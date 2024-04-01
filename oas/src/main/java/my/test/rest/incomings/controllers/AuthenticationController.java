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

import my.test.authorization.rules.AuthenticateUserInteractor;
import my.test.authorization.rules.CreateUserInteractor;
import my.test.authorization.rules.InteractorFactory;
import my.test.rest.incomings.controllers.api.AuthenticationApi;
import my.test.rest.incomings.controllers.api.dto.AuthInfo;
import my.test.rest.incomings.controllers.api.dto.Authentication;
import my.test.rest.incomings.controllers.api.dto.TokenPair;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Authentication controller.
 *
 * @since 1.0
 */
@Controller
@RequestMapping("/api")
public final class AuthenticationController implements AuthenticationApi {

    /**
     * Interactor factory.
     */
    private final InteractorFactory ifactory;

    public AuthenticationController(final InteractorFactory ifactory) {
        this.ifactory = ifactory;
    }

    @Override
    public ResponseEntity<Authentication> login(final AuthInfo ainfo) {
        final AuthenticationUserResponseModelImpl model = new AuthenticationUserResponseModelImpl();
        final AuthenticateUserInteractor interactor =
            this.ifactory.createAuthenticateUserInteractor(
                ainfo.getUserName(),
                ainfo.getPasswordHash(),
                model.events()
            );
        interactor.authenticate();
        return model.renderModel();
    }

    @Override
    public ResponseEntity<Authentication> create(final AuthInfo ainfo) {
        final CreationUserResponseModelImpl model = new CreationUserResponseModelImpl();
        final CreateUserInteractor interactor =
            this.ifactory.createNewUserInteractor(
                ainfo.getUserName(),
                ainfo.getPasswordHash(),
                model.events()
            );
        interactor.createNewUser();
        return model.renderModel();
    }

    @Override
    public ResponseEntity<Void> logout() {
        return AuthenticationApi.super.logout();
    }

    @Override
    public ResponseEntity<TokenPair> refreshTokens() {
        return AuthenticationApi.super.refreshTokens();
    }
}
