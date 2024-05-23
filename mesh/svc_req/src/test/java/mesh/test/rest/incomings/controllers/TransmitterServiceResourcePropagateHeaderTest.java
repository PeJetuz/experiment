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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.helidon.microprofile.config.ConfigCdiExtension;
import io.helidon.microprofile.server.JaxRsCdiExtension;
import io.helidon.microprofile.server.ServerCdiExtension;
import io.helidon.microprofile.telemetry.TelemetryCdiExtension;
import io.helidon.microprofile.testing.junit5.AddBean;
import io.helidon.microprofile.testing.junit5.AddExtension;
import io.helidon.microprofile.testing.junit5.DisableDiscovery;
import io.helidon.microprofile.testing.junit5.HelidonTest;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.baggage.propagation.W3CBaggagePropagator;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import mesh.test.rest.incomings.controllers.api.ApiException;
import mesh.test.rest.incomings.controllers.api.ApiExceptionMapper;
import mesh.test.rest.incomings.controllers.api.PingApi;
import mesh.test.rest.incomings.controllers.api.ResponseStub;
import mesh.test.rest.incomings.exceptions.MyExceptionMapper;
import org.assertj.core.api.Assertions;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.glassfish.jersey.ext.cdi1x.internal.CdiComponentProvider;
import org.junit.jupiter.api.Test;

/**
 * Test propagation tracing headers.
 *
 * @since 1.0
 */
@SuppressWarnings({
    "PMD.JUnitAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.JUnitTestClassShouldBeFinal",
    "PMD.UnnecessaryAnnotationValueElement"
})
@HelidonTest
@DisableDiscovery
@AddBean(value = TransmitterServiceResourcePropagateHeaderTest.PingApiImpl.class)
@AddBean(value = TransmitterServiceResource.class)
@AddBean(value = MyExceptionMapper.class)
@AddBean(value = ApiExceptionMapper.class)
@AddBean(value = TransmitterServiceResourcePropagateHeaderTest.PingStub.class)
@AddBean(value = TransmitterServiceResourcePropagateHeaderTest.PingHdrApiStub.class)
@AddExtension(ServerCdiExtension.class)
@AddExtension(JaxRsCdiExtension.class)
@AddExtension(CdiComponentProvider.class)
@AddExtension(TelemetryCdiExtension.class)
@AddExtension(ConfigCdiExtension.class)
public class TransmitterServiceResourcePropagateHeaderTest {

    /**
     * Target.
     */
    @Inject
    private WebTarget target;

    @SuppressWarnings("PMD.AvoidAccessToStaticMembersViaThis")
    @Test
    void ping() throws JsonProcessingException {
        final OpenTelemetry telemetry = OpenTelemetrySdk.builder()
            .setPropagators(
                ContextPropagators.create(
                    TextMapPropagator.composite(
                        W3CTraceContextPropagator.getInstance(),
                        W3CBaggagePropagator.getInstance()
                    )
                )
            ).buildAndRegisterGlobal();
        final Tracer tracer = telemetry.getTracer(
            "instrumentation-scope-name",
            "instrumentation-scope-version"
        );
        final Span span = tracer.spanBuilder("test").startSpan();
        final MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>(
            TransmitterServiceResourcePropagateHeaderTest.headers(
                span.getSpanContext().getTraceId(),
                span.getSpanContext().getSpanId()
            )
        );
        final String resp = this.target
            .path("api/callping")
            .request()
            .headers(headers)
            .get(String.class);
        span.end();
        final MultivaluedMap<String, Object> result = new ObjectMapper().readValue(
            resp,
            new TypeReference<MultivaluedHashMap<String, Object>>() {
            }
        );
        headers.remove("x-ot-span-context");
        headers.remove("traceparent");
        result.remove("Accept");
        result.remove("User-Agent");
        result.remove("Connection");
        result.remove("Host");
        result.remove("traceparent");
        Assertions.assertThat(result).containsExactlyInAnyOrderEntriesOf(headers);
    }

    private static MultivaluedMap<String, Object> headers(final String trace, final String span) {
        final String trc = String.format("00-%s-%s-01", trace, span);
        final MultivaluedMap<String, Object> hdr = new MultivaluedHashMap<>();
        hdr.add("x-request-id", "9da8bce3-bb8a-9175-b4a1-9582ce062072");
        hdr.add("x-b3-traceid", "x-b3-traceid");
        hdr.add("x-b3-spanid", "x-b3-spanid");
        hdr.add("x-b3-parentspanid", "x-b3-parentspanid");
        hdr.add("x-b3-sampled", "x-b3-sampled");
        hdr.add("x-b3-flags", "x-b3-flags");
        hdr.add("x-ot-span-context", "x-ot-span-context");
        hdr.add("x-datadog-trace-id", "x-datadog-trace-id");
        hdr.add("x-datadog-parent-id", "x-datadog-parent-id");
        hdr.add("x-datadog-sampling-priority", "x-datadog-sampling-priority");
        hdr.add("traceparent", trc);
        hdr.add("x-cloud-trace-context", "x-cloud-trace-context");
        hdr.add("grpc-trace-bin", "grpc-trace-bin");
        hdr.add("sw8", "sw8");
        hdr.add("jwt", "jwt");
        return hdr;
    }

    /**
     * Implementation of a Ping service that return headers as json.
     *
     * @since 1.0
     * @checkstyle DesignForExtensionCheck (20 lines)
     */
    @Path("/test")
    public static class PingStub {
        @GET
        @Path("/ping")
        @Produces("application/json")
        public String ping(@Context final HttpHeaders headers) throws JsonProcessingException {
            final Map<String, String> hdrs = new HashMap<>();
            headers.getRequestHeaders().forEach((key, value) -> hdrs.put(key, value.getFirst()));
            return new ObjectMapper().writeValueAsString(headers.getRequestHeaders());
        }
    }

    /**
     * Implementation @RestClient.
     *
     * @since 1.0
     * @checkstyle DesignForExtensionCheck (50 lines)
     */
    @RestClient
    public static class PingApiImpl implements PingApi {

        /**
         * Rest client proxy.
         */
        private final PingApi proxy;

        /**
         * Ctor.
         * this.proxy = RestClientBuilder.newBuilder()
         *     .baseUri(URI.create("http://localhost:8282/test")) // for docker
         *     .build(PingApi.class);
         *
         * @param srvcdiext CDI container.
         */
        @Inject
        public PingApiImpl(final ServerCdiExtension srvcdiext) {
            this.proxy = RestClientBuilder.newBuilder()
                .baseUri(
                    URI.create(
                        String.format(
                            "http://localhost:%d/test",
                            srvcdiext.port()
                        )
                    )
                ).build(PingApi.class);
        }

        @Override
        public String count() throws ApiException, ProcessingException {
            return this.proxy.count();
        }

        @Override
        public String ping() throws ApiException, ProcessingException {
            return this.proxy.ping();
        }

        @Override
        public String reset() throws ApiException, ProcessingException {
            return this.proxy.reset();
        }
    }

    /**
     * Print headers implementation @RestClient.
     *
     * @since 1.0
     * @checkstyle DesignForExtensionCheck (10 lines)
     */
    @RestClient
    public static class PingHdrApiStub implements PingHdrApi {

        @Override
        public String pinghdr() throws ApiException, ProcessingException {
            throw new ApiException(new ResponseStub());
        }
    }
}
