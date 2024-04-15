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

import io.helidon.config.Config;
import io.helidon.http.Status;
import io.helidon.webserver.http.HttpRules;
import io.helidon.webserver.http.HttpService;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;
import jakarta.json.Json;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObject;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A simple service to greet you. Examples:
 * <p>
 * Get default greeting message:
 * {@code curl -X GET http://localhost:8080/greet}
 * <p>
 * Get greeting message for Joe:
 * {@code curl -X GET http://localhost:8080/greet/Joe}
 * <p>
 * Change greeting
 * {@code curl -X PUT -H "Content-Type: application/json" -d '{"greeting" : "Howdy"}' http://localhost:8080/greet/greeting}
 * <p>
 * The message is returned as a JSON object
 *
 * @since 1.0
 */
class GreetService implements HttpService {

    /**
     * JSON builder factory.
     */
    private static final JsonBuilderFactory JSON =
        Json.createBuilderFactory(Collections.emptyMap());

    /**
     * The config value for the key {@code greeting}.
     */
    private final AtomicReference<String> greeting;

    GreetService() {
        this(Config.global().get("app"));
    }

    @SuppressWarnings("PMD.ConstructorOnlyInitializesOrCallOtherConstructors")
    GreetService(final Config config) {
        this.greeting = new AtomicReference<>();
        this.greeting.set(config.get("greeting").asString().orElse("Ciao"));
    }

    /**
     * A service registers itself by updating the routing rules.
     *
     * @param rules The routing rules.
     * @checkstyle NoJavadocForOverriddenMethodsCheck (10 lines)
     */
    @Override
    public void routing(final HttpRules rules) {
        rules
            .get("/", this::getDefaultMessageHandler)
            .get("/{name}", this::getMessageHandler)
            .put("/greeting", this::updateGreetingHandler);
    }

    /**
     * Return a worldly greeting message.
     *
     * @param request The server request.
     * @param response The server response.
     */
    @SuppressWarnings("PMD.UnusedFormalParameter")
    private void getDefaultMessageHandler(
        final ServerRequest request,
        final ServerResponse response
    ) {
        this.sendResponse(response, "World");
    }

    /**
     * Return a greeting message using the name that was provided.
     *
     * @param request The server request.
     * @param response The server response.
     */
    private void getMessageHandler(
        final ServerRequest request,
        final ServerResponse response
    ) {
        final String name = request.path().pathParameters().get("name");
        this.sendResponse(response, name);
    }

    private void sendResponse(final ServerResponse response, final String name) {
        final String msg = String.format("%s %s!", this.greeting.get(), name);
        final JsonObject retobj = JSON.createObjectBuilder()
            .add("message", msg)
            .build();
        response.send(retobj);
    }

    private void updateGreetingFromJson(final JsonObject jobj, final ServerResponse response) {
        if (!jobj.containsKey("greeting")) {
            final JsonObject error = JSON.createObjectBuilder()
                .add("error", "No greeting provided")
                .build();
            response.status(Status.BAD_REQUEST_400)
                .send(error);
            return;
        }
        this.greeting.set(jobj.getString("greeting"));
        response.status(Status.NO_CONTENT_204).send();
    }

    /**
     * Set the greeting to use in future messages.
     *
     * @param request The server request.
     * @param response The server response.
     */
    private void updateGreetingHandler(final ServerRequest request,
        final ServerResponse response
    ) {
        this.updateGreetingFromJson(request.content().as(JsonObject.class), response);
    }
}
