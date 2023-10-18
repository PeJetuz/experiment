package my.test.rest.incomings.controllers.api;

import io.helidon.common.http.Http.RequestMethod;
import io.helidon.webserver.Handler;
import io.helidon.webserver.HttpRoute;
import io.helidon.webserver.PathMatcher;
import io.helidon.webserver.Routing;
import io.helidon.webserver.Routing.Rules;
import io.helidon.webserver.Service;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.WebTracingConfig;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import my.test.rest.incomings.controllers.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuthenticationServiceTest {

    @Test
    public void update() {
        RulesSpy rules = new RulesSpy();
        AuthenticationServiceImpl subj = new AuthenticationServiceImpl(null, null);

        subj.update(rules);

        assertEquals("/create", rules.putPattern);
        assertEquals("/login", rules.postPattern);
        assertNotNull(rules.getPattern.get("/logout"));
        assertNotNull(rules.getPattern.get("/tokens/refresh"));
    }

    private class RulesSpy implements Routing.Rules {

        Map<String, Handler[]> getPattern = new HashMap<>();
        String putPattern;
        String postPattern;

        @Override
        public Rules register(WebTracingConfig webTracingConfig) {
            return null;
        }

        @Override
        public Rules register(Service... services) {
            return null;
        }

        @Override
        public Rules register(Supplier<? extends Service>... serviceBuilders) {
            return null;
        }

        @Override
        public Rules register(String pathPattern, Service... services) {
            return null;
        }

        @Override
        public Rules register(String pathPattern, Supplier<? extends Service>... serviceBuilders) {
            return null;
        }

        @Override
        public Rules get(Handler... requestHandlers) {
            return null;
        }

        @Override
        public Rules get(String getPattern, Handler... requestHandlers) {
            this.getPattern.put(getPattern, requestHandlers);
            return null;
        }

        @Override
        public Rules route(HttpRoute route) {
            return null;
        }

        @Override
        public Rules get(PathMatcher pathMatcher, Handler... requestHandlers) {
            return null;
        }

        @Override
        public Rules put(Handler... requestHandlers) {
            return null;
        }

        @Override
        public Rules put(String putPattern, Handler... requestHandlers) {
            this.putPattern = putPattern;
            return null;
        }

        @Override
        public Rules put(PathMatcher pathMatcher, Handler... requestHandlers) {
            return null;
        }

        @Override
        public Rules post(Handler... requestHandlers) {
            return null;
        }

        @Override
        public Rules post(String postPattern, Handler... requestHandlers) {
            this.postPattern = postPattern;
            return null;
        }

        @Override
        public Rules post(PathMatcher pathMatcher, Handler... requestHandlers) {
            return null;
        }

        @Override
        public Rules patch(Handler... requestHandlers) {
            return null;
        }

        @Override
        public Rules patch(String pathPattern, Handler... requestHandlers) {
            return null;
        }

        @Override
        public Rules patch(PathMatcher pathMatcher, Handler... requestHandlers) {
            return null;
        }

        @Override
        public Rules delete(Handler... requestHandlers) {
            return null;
        }

        @Override
        public Rules delete(String pathPattern, Handler... requestHandlers) {
            return null;
        }

        @Override
        public Rules delete(PathMatcher pathMatcher, Handler... requestHandlers) {
            return null;
        }

        @Override
        public Rules options(Handler... requestHandlers) {
            return null;
        }

        @Override
        public Rules options(String pathPattern, Handler... requestHandlers) {
            return null;
        }

        @Override
        public Rules options(PathMatcher pathMatcher, Handler... requestHandlers) {
            return null;
        }

        @Override
        public Rules head(Handler... requestHandlers) {
            return null;
        }

        @Override
        public Rules head(String pathPattern, Handler... requestHandlers) {
            return null;
        }

        @Override
        public Rules head(PathMatcher pathMatcher, Handler... requestHandlers) {
            return null;
        }

        @Override
        public Rules trace(Handler... requestHandlers) {
            return null;
        }

        @Override
        public Rules trace(String pathPattern, Handler... requestHandlers) {
            return null;
        }

        @Override
        public Rules trace(PathMatcher pathMatcher, Handler... requestHandlers) {
            return null;
        }

        @Override
        public Rules any(Handler... requestHandlers) {
            return null;
        }

        @Override
        public Rules any(String pathPattern, Handler... requestHandlers) {
            return null;
        }

        @Override
        public Rules any(PathMatcher pathMatcher, Handler... requestHandlers) {
            return null;
        }

        @Override
        public Rules anyOf(Iterable<RequestMethod> methods, Handler... requestHandlers) {
            return null;
        }

        @Override
        public Rules anyOf(Iterable<RequestMethod> methods, String pathPattern, Handler... requestHandlers) {
            return null;
        }

        @Override
        public Rules anyOf(Iterable<RequestMethod> methods, PathMatcher pathMatcher, Handler... requestHandlers) {
            return null;
        }

        @Override
        public Rules onNewWebServer(Consumer<WebServer> webServerConsumer) {
            return null;
        }
    }
}
