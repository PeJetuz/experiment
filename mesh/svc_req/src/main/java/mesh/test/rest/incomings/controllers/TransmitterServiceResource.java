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

package mesh.test.rest.incomings.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.helidon.tracing.Span;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import mesh.test.rest.incomings.controllers.api.ApiException;
import mesh.test.rest.incomings.controllers.api.PingApi;
import mesh.test.rest.incomings.controllers.api.TransmitterService;
import mesh.test.rest.incomings.exceptions.MyException;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 * Transmitter.
 * Helidon does not support final classes.
 *
 * @since 1.0
 * @checkstyle DesignForExtensionCheck (60 lines)
 */
@Path("/api")
public class TransmitterServiceResource implements TransmitterService {

    /**
     * Log.
     */
    private static final System.Logger LOG =
        System.getLogger(TransmitterServiceResource.class.getName());

    /**
     * Ping counter.
     */
    private final PingApi ping;

    /**
     * Ping counter with header printing.
     */
    private final PingHdrApi pinghdr;

    @Inject
    public TransmitterServiceResource(
        @RestClient final PingApi png,
        @RestClient final PingHdrApi pnghdr
    ) {
        this.ping = png;
        this.pinghdr = pnghdr;
    }

    @Override
    @WithSpan("req_callcount")
    public String callcount() {
        traceMethod("callcount");
        try {
            return this.ping.count();
        } catch (final ApiException aex) {
            traceError("callcount", aex);
            throw new MyException(aex.getResponse(), aex);
        }
    }

    @Override
    @WithSpan("req_callping")
    public String callping() {
        traceMethod("callping");
        try {
            return this.ping.ping();
        } catch (final ApiException aex) {
            traceError("callping", aex);
            throw new MyException(aex.getResponse(), aex);
        }
    }

    @GET
    @Path("/callpinghdr")
    @Produces("application/json")
    @WithSpan("req_callpinghdr")
    public String callpinghdr(@Context final HttpHeaders headers) throws JsonProcessingException {
        traceMethod("callpinghdr");
        LOG.log(
            System.Logger.Level.INFO,
            new ObjectMapper().writeValueAsString(headers.getRequestHeaders())
        );
        try {
            return this.pinghdr.pinghdr();
        } catch (final ApiException aex) {
            traceError("callpinghdr", aex);
            throw new MyException(aex.getResponse(), aex);
        }
    }

    private static void traceMethod(final String method) {
        final Span span = Span.current().get();
        LOG.log(
            System.Logger.Level.INFO,
            String.format(
                "%s [trace: %s, span: %s]",
                method,
                span.context().traceId(),
                span.context().spanId()
            )
        );
    }

    private static void traceError(final String method, final ApiException aex) {
        final Span span = Span.current().get();
        LOG.log(
            System.Logger.Level.ERROR,
            String.format(
                "%s [trace: %s, span: %s]:%n%s",
                method,
                span.context().traceId(),
                span.context().spanId(),
                aex.getMessage()
            )
        );
    }
}
