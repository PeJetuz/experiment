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
import io.helidon.logging.common.LogConfig;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.http.HttpRouting;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import my.test.authorization.domain.api.servicebus.EventTransmitterFactory;
import my.test.authorization.domain.impl.PolicyFactoryImpl;
import my.test.authorization.rules.impl.InteractorFactoryImpl;
import my.test.authorization.store.UserMockFactoryImpl;
import my.test.rest.incomings.controllers.impl.AuthenticationServiceImpl;

/**
 * The application main class.
 *
 * @since 1.0
 */
public final class Main {

    /**
     * JSONB builder.
     */
    private static final Jsonb JSONB = JsonbBuilder.create();

    /**
     * Cannot be instantiated.
     */
    private Main() {
        //do nothing
    }

    /**
     * Application main entry point.
     *
     * @param args Command line arguments.
     */
    @SuppressWarnings("PMD.SystemPrintln")
    public static void main(final String[] args) {
        LogConfig.configureRuntime();
        final Config config = Config.create();
        Config.global(config);
        final WebServer server = WebServer.builder()
            .config(config.get("server"))
            .routing(Main::routing)
            .build()
            .start();
        System.out.println(
            String.format("WEB server is up! http://localhost:%d/simple-greet", server.port())
        );
    }

    /**
     * Updates HTTP Routing.
     *
     * @param routing Routing builder.
     */
    static void routing(final HttpRouting.Builder routing) {
        routing
            .register("/greet", new GreetService())
            .register(
                "/api",
                new AuthenticationServiceImpl(
                    new InteractorFactoryImpl(
                        new PolicyFactoryImpl(
                            new UserMockFactoryImpl(),
                            new EventTransmitterFactory.Stub()
                        )
                    ),
                    Main.JSONB
                )
            )
            .get("/simple-greet", (req, res) -> res.send("Hello World!"));
    }
}
