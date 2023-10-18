package my.test.rest.incomings.controllers;

import io.helidon.common.context.Context;
import io.helidon.common.http.Http.RequestMethod;
import io.helidon.common.http.Http.Version;
import io.helidon.common.http.Parameters;
import io.helidon.common.http.UriInfo;
import io.helidon.common.reactive.Single;
import io.helidon.media.common.MessageBodyReadableContent;
import io.helidon.tracing.SpanContext;
import io.helidon.tracing.Tracer;
import io.helidon.webserver.RequestHeaders;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.WebServer;
import java.net.URI;
import java.util.Optional;

public class ServerRequestFake implements ServerRequest {

    @Override
    public void next() {

    }

    @Override
    public void next(Throwable t) {

    }

    @Override
    public WebServer webServer() {
        return null;
    }

    @Override
    public Context context() {
        return null;
    }

    @Override
    public String localAddress() {
        return null;
    }

    @Override
    public int localPort() {
        return 0;
    }

    @Override
    public String remoteAddress() {
        return null;
    }

    @Override
    public int remotePort() {
        return 0;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public RequestHeaders headers() {
        return null;
    }

    @Override
    public MessageBodyReadableContent content() {
        return null;
    }

    @Override
    public long requestId() {
        return 0;
    }

    @Override
    public Optional<SpanContext> spanContext() {
        return Optional.empty();
    }

    @Override
    public Tracer tracer() {
        return null;
    }

    @Override
    public Single<Void> closeConnection() {
        return null;
    }

    @Override
    public UriInfo requestedUri() {
        return null;
    }

    @Override
    public RequestMethod method() {
        return null;
    }

    @Override
    public Version version() {
        return null;
    }

    @Override
    public URI uri() {
        return null;
    }

    @Override
    public String query() {
        return null;
    }

    @Override
    public Parameters queryParams() {
        return null;
    }

    @Override
    public Path path() {
        return null;
    }

    @Override
    public String fragment() {
        return null;
    }
}
