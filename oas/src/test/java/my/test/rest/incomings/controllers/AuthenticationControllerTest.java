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

import my.test.authorization.domain.impl.PolicyFactorySpy;
import my.test.authorization.rules.impl.InteractorFactoryImpl;
import my.test.rest.incomings.controllers.api.dto.AuthInfo;
import my.test.rest.incomings.controllers.api.dto.Authentication;
import my.test.rest.incomings.controllers.api.dto.TokenPair;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * AuthenticationController tests.
 *
 * @since 1.0
 */
final class AuthenticationControllerTest {

    /**
     * Controller under test.
     */
    private final AuthenticationController controller;

    AuthenticationControllerTest() {
        this.controller = new AuthenticationController(
            new InteractorFactoryImpl(new PolicyFactorySpy())
        );
    }

    @Test
    void checkLoginSuccess() {
        final ResponseEntity<Authentication> result = this.controller.login(new AuthInfo());
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(result.getBody()).isNotNull();
    }

    @Test
    void createUser() {
        final AuthInfo auth = new AuthInfo();
        final ResponseEntity<Authentication> result = this.controller.create(auth);
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(result.getBody()).isNotNull();
    }

    @Test
    void logout() {
        final ResponseEntity<Void> result = this.controller.logout();
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_IMPLEMENTED);
        Assertions.assertThat(result.getBody()).isNull();
    }

    @Test
    void refreshTokens() {
        final ResponseEntity<TokenPair> result = this.controller.refreshTokens();
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_IMPLEMENTED);
        Assertions.assertThat(result.getBody()).isNull();
    }
}
