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

package io.helidon.examples.quickstart.se;

import io.helidon.http.Status;
import io.helidon.webclient.api.ClientResponseTyped;
import io.helidon.webclient.http1.Http1Client;
import io.helidon.webserver.http.HttpRouting;
import io.helidon.webserver.testing.junit5.SetUpRoute;
import jakarta.json.Json;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObject;
import java.util.Map;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Base test class.
 *
 * @since 1.0
 */
@SuppressWarnings({
    "PMD.AbstractClassWithoutAbstractMethod",
    "PMD.JUnitTestClassShouldBeFinal",
    "PMD.JUnitAssertionsShouldIncludeMessage"
})
abstract class AbstractLoginServiceTest {

    /**
     * JSON factory.
     */
    static final JsonBuilderFactory JSON_FACTORY = Json.createBuilderFactory(Map.of());

    /**
     * User name.
     */
    static final String USER = "Vasya";

    /**
     * Http client.
     */
    private final Http1Client client;

    protected AbstractLoginServiceTest(final Http1Client client) {
        this.client = client;
    }

    @SetUpRoute
    static void routing(final HttpRouting.Builder builder) {
        Main.routing(builder);
    }

    @Test
    void testAuthLogout() {
        final ClientResponseTyped<JsonObject> response =
            this.client.get("/api/logout").request(JsonObject.class);
        MatcherAssert.assertThat(response.status(), Matchers.is(Status.NOT_IMPLEMENTED_501));
    }

    @Test
    void testRefreshTokensLogout() {
        final ClientResponseTyped<JsonObject> response =
            this.client.get("/api/tokens/refresh").request(JsonObject.class);
        MatcherAssert.assertThat(response.status(), Matchers.is(Status.NOT_IMPLEMENTED_501));
    }

    @Test
    void testLogin() {
        final ClientResponseTyped<JsonObject> response =
            this.client.post("/api/login")
                .submit(
                    JSON_FACTORY.createObjectBuilder()
                        .add("userName", AbstractLoginServiceTest.USER)
                        .add("passwordHash", "password")
                        .build(),
                    JsonObject.class
                );
        MatcherAssert.assertThat(response.status(), Matchers.is(Status.OK_200));
        MatcherAssert.assertThat(
            response.entity().getString("username"),
            Matchers.is(AbstractLoginServiceTest.USER)
        );
    }

    @Test
    void testLoginUserNotFound() {
        final ClientResponseTyped<JsonObject> response =
            this.client.post("/api/login")
                .submit(
                    JSON_FACTORY.createObjectBuilder()
                        .add("userName", "Vasya123")
                        .add("passwordHash", "password")
                        .build(),
                    JsonObject.class
                );
        MatcherAssert.assertThat(response.status(), Matchers.is(Status.UNAUTHORIZED_401));
        MatcherAssert.assertThat(
            response.entity().getString("message"),
            Matchers.is("User not found")
        );
    }

    @Test
    void testLoginInvalidPassword() {
        final ClientResponseTyped<JsonObject> response =
            this.client.post("/api/login")
                .submit(
                    JSON_FACTORY.createObjectBuilder()
                        .add("userName", AbstractLoginServiceTest.USER)
                        .build(),
                    JsonObject.class
                );
        MatcherAssert.assertThat(response.status(), Matchers.is(Status.UNAUTHORIZED_401));
        MatcherAssert.assertThat(
            response.entity().getString("message"),
            Matchers.is("Invalid password")
        );
    }

    @Test
    void testLoginLimitExceeded() {
        final ClientResponseTyped<JsonObject> response =
            this.client.post("/api/login")
                .submit(
                    JSON_FACTORY.createObjectBuilder()
                        .add("userName", AbstractLoginServiceTest.USER)
                        .add("password", AbstractLoginServiceTest.USER)
                        .build(),
                    JsonObject.class
                );
        MatcherAssert.assertThat(response.status(), Matchers.is(Status.UNAUTHORIZED_401));
        MatcherAssert.assertThat(
            response.entity().getString("message"),
            Matchers.is("Invalid password")
        );
    }
}
