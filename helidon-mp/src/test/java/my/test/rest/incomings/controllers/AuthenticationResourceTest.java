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

import io.helidon.microprofile.testing.junit5.HelidonTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test AuthenticationResource.
 *
 * @since 1.0
 */
@SuppressWarnings({
    "PMD.JUnitAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.JUnitTestClassShouldBeFinal"
})
@HelidonTest
public class AuthenticationResourceTest {

    /**
     * Target.
     */
    @Inject
    private WebTarget target;

    @Test
    void canCreate() {
        try (Response r = this.target
            .path("api/create")
            .request()
            .put(
                Entity.entity(
                    "{\"userName\": \"Vasya23\",\"passwordHash\": \"123\"}",
                    MediaType.APPLICATION_JSON
                )
            )) {
            MatcherAssert.assertThat(r.getStatus(), Matchers.is(204));
        }
    }

    @Test
    void canLogin() {
        try (Response r = this.target
            .path("api/login")
            .request()
            .post(
                Entity.entity(
                    "{\"userName\": \"Vasya23\",\"passwordHash\": \"123\"}",
                    MediaType.APPLICATION_JSON
                )
            )) {
            MatcherAssert.assertThat(r.getStatus(), Matchers.is(204));
        }
    }

    @Test
    void canLogout() {
        try (Response r = this.target
            .path("api/logout")
            .request()
            .get(Response.class)) {
            MatcherAssert.assertThat(r.getStatus(), Matchers.is(204));
        }
    }

    @Test
    void canRefreshTokens() {
        try (Response r = this.target
            .path("api/tokens/refresh")
            .request()
            .get(Response.class)) {
            MatcherAssert.assertThat(r.getStatus(), Matchers.is(204));
        }
    }
}
