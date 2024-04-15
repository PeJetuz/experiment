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
import io.helidon.webclient.http1.Http1ClientResponse;
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
abstract class AbstractMainTest {

    /**
     * JSON factory.
     */
    static final JsonBuilderFactory JSON_FACTORY = Json.createBuilderFactory(Map.of());

    /**
     * Http client.
     */
    private final Http1Client client;

    protected AbstractMainTest(final Http1Client client) {
        this.client = client;
    }

    @SetUpRoute
    static void routing(final HttpRouting.Builder builder) {
        Main.routing(builder);
    }

    @Test
    void testGreeting() {
        final ClientResponseTyped<JsonObject> response =
            this.client.get("/greet").request(JsonObject.class);
        MatcherAssert.assertThat(response.status(), Matchers.is(Status.OK_200));
        MatcherAssert.assertThat(
            response.entity().getString("message"),
            Matchers.is("Hello World!")
        );
    }

    @Test
    void testGreetingJoe() {
        final ClientResponseTyped<JsonObject> response =
            this.client.get("/greet/Joe").request(JsonObject.class);
        MatcherAssert.assertThat(response.status(), Matchers.is(Status.OK_200));
        MatcherAssert.assertThat(
            response.entity().getString("message"),
            Matchers.is("Hello Joe!")
        );
    }

    @Test
    void testUpdate() {
        final ClientResponseTyped<JsonObject> response =
            this.client.put("/greet/greeting")
                .submit(
                    JSON_FACTORY.createObjectBuilder().add("greeting", "Howdy").build(),
                    JsonObject.class
                );
        MatcherAssert.assertThat(response.status(), Matchers.is(Status.NO_CONTENT_204));
    }

    @Test
    void testUpdateFailed() {
        final ClientResponseTyped<JsonObject> response =
            this.client.put("/greet/greeting")
                .submit(
                    JSON_FACTORY.createObjectBuilder().add("greeting!", "Howdy1").build(),
                    JsonObject.class
                );
        MatcherAssert.assertThat(response.status(), Matchers.is(Status.BAD_REQUEST_400));
        MatcherAssert.assertThat(
            response.entity().getString("error"),
            Matchers.is("No greeting provided")
        );
    }

    @Test
    void testMetricsObserver() {
        try (Http1ClientResponse response = this.client.get("/observe/metrics").request()) {
            MatcherAssert.assertThat(response.status(), Matchers.is(Status.OK_200));
        }
    }

    @Test
    void testSimpleGreet() {
        final ClientResponseTyped<String> response =
            this.client.get("/simple-greet").request(String.class);
        MatcherAssert.assertThat(response.status(), Matchers.is(Status.OK_200));
        MatcherAssert.assertThat(response.entity(), Matchers.is("Hello World!"));
    }
}
