package io.helidon.examples.quickstart.se;

import io.helidon.media.jsonb.JsonbSupport;
import io.helidon.media.jsonp.JsonpSupport;
import io.helidon.metrics.MetricsSupport;
import io.helidon.health.HealthSupport;
import io.helidon.health.checks.HealthChecks;
import io.helidon.common.LogConfig;
import io.helidon.common.reactive.Single;
import io.helidon.config.Config;
import io.helidon.openapi.OpenAPISupport;
import io.helidon.webserver.Routing;
import io.helidon.webserver.WebServer;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import my.test.authorization.domain.api.servicebus.LoginEventTransmitter;
import my.test.authorization.domain.impl.PolicyFactoryImpl;
import my.test.authorization.rules.impl.AuthFactoryImpl;
import my.test.authorization.rules.impl.ResponseFactoryImpl;
import my.test.authorization.store.UserMockFactoryImpl;
import my.test.rest.incomings.controllers.impl.AuthenticationServiceImpl;

/**
 * The application main class.
 */
public final class Main {

    private static final Jsonb JSONB = JsonbBuilder.create();

    /**
     * Cannot be instantiated.
     */
    private Main() {
    }

    /**
     * Application main entry point.
     *
     * @param args command line arguments.
     */
    public static void main(final String[] args) {
        startServer();
    }

    /**
     * Start the server.
     *
     * @return the created {@link WebServer} instance
     */
    static Single<WebServer> startServer() {
        // load logging configuration
        LogConfig.configureRuntime();

        // By default this will pick up application.yaml from the classpath
        Config config = Config.create();

        WebServer server = WebServer.builder(createRouting(config))
                .config(config.get("server"))
                .addMediaSupport(JsonpSupport.create())
                .addMediaSupport(JsonbSupport.create(JSONB))
                //.addMediaSupport(JacksonSupport.create(JsonProvider.objectMapper()))
                .build();

        Single<WebServer> webserver = server.start();

        // Try to start the server. If successful, print some info and arrange to
        // print a message at shutdown. If unsuccessful, print the exception.
        webserver.forSingle(ws -> {
                    System.out.println("WEB server is up! http://localhost:" + ws.port() + "/greet");
                    ws.whenShutdown().thenRun(() -> System.out.println("WEB server is DOWN. Good bye!"));
                })
                .exceptionallyAccept(t -> {
                    System.err.println("Startup failed: " + t.getMessage());
                    t.printStackTrace(System.err);
                });

        return webserver;
    }

    /**
     * Creates new {@link Routing}.
     *
     * @param config configuration of this server
     * @return routing configured with JSON support, a health check, and a service
     */
    private static Routing createRouting(Config config) {

        SimpleGreetService simpleGreetService = new SimpleGreetService(config);
        GreetService greetService = new GreetService(config);

        HealthSupport health = HealthSupport.builder()
                .addLiveness(HealthChecks.healthChecks()) // Adds a convenient set of checks
                .build();

        Routing.Builder builder = Routing.builder()
                .register(OpenAPISupport.create(config.get(OpenAPISupport.Builder.CONFIG_KEY)))
                .register(MetricsSupport.create()) // Metrics at "/metrics"
                .register(health) // Health at "/health"
                .register("/simple-greet", simpleGreetService)
                .register("/greet", greetService)
                .register("/api", new AuthenticationServiceImpl(
                        new AuthFactoryImpl(new PolicyFactoryImpl(new UserMockFactoryImpl(),
                                new my.test.authorization.domain.api.servicebus.LoginEventTransmitterBuilder.Stub(new LoginEventTransmitter.Dummy()))),
                        new ResponseFactoryImpl(JSONB)));

        return builder.build();
    }
}
