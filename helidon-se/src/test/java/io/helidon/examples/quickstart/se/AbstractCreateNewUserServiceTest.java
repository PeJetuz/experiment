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
import java.util.Random;
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
abstract class AbstractCreateNewUserServiceTest {

    /**
     * JSON factory.
     */
    static final JsonBuilderFactory JSON_FACTORY = Json.createBuilderFactory(Map.of());

    /**
     * Http client.
     */
    private final Http1Client client;

    protected AbstractCreateNewUserServiceTest(final Http1Client client) {
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
    void testCreate() {
        final String name = String.format("Vasya234%d", new Random().nextLong());
        final ClientResponseTyped<JsonObject> response =
            this.client.put("/api/create")
                .submit(
                    JSON_FACTORY.createObjectBuilder()
                        .add("userName", name)
                        .add("passwordHash", "password234")
                        .build(),
                    JsonObject.class
                );
        MatcherAssert.assertThat(response.status(), Matchers.is(Status.OK_200));
        MatcherAssert.assertThat(
            response.entity().getString("username"),
            Matchers.is(name)
        );
    }

    @Test
    void testCreateEmptyPassword() {
        final ClientResponseTyped<JsonObject> response =
            this.client.put("/api/create")
                .submit(
                    JSON_FACTORY.createObjectBuilder()
                        .add("userName", "Vasya3456")
                        .build(),
                    JsonObject.class
                );
        MatcherAssert.assertThat(response.status(), Matchers.is(Status.FORBIDDEN_403));
        MatcherAssert.assertThat(
            response.entity().getString("message"),
            Matchers.is("Invalid user password")
        );
    }

    @Test
    void testCreateEmptyName() {
        final ClientResponseTyped<JsonObject> response =
            this.client.put("/api/create")
                .submit(
                    JSON_FACTORY.createObjectBuilder()
                        .add("userName", "")
                        .add("passwordHash", "password")
                        .build(),
                    JsonObject.class
                );
        MatcherAssert.assertThat(response.status(), Matchers.is(Status.FORBIDDEN_403));
        MatcherAssert.assertThat(
            response.entity().getString("message"),
            Matchers.is("Invalid user name")
        );
    }

    @Test
    void testCreateUserAlreadyExists() {
        final ClientResponseTyped<JsonObject> response =
            this.client.put("/api/create")
                .submit(
                    JSON_FACTORY.createObjectBuilder()
                        .add("userName", "Vasya")
                        .add("passwordHash", "password")
                        .build(),
                    JsonObject.class
                );
        MatcherAssert.assertThat(response.status(), Matchers.is(Status.FORBIDDEN_403));
        MatcherAssert.assertThat(
            response.entity().getString("message"),
            Matchers.is("User already exists")
        );
    }
}
