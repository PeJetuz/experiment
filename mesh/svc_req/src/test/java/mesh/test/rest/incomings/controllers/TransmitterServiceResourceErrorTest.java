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

import io.helidon.microprofile.config.ConfigCdiExtension;
import io.helidon.microprofile.server.JaxRsCdiExtension;
import io.helidon.microprofile.server.ServerCdiExtension;
import io.helidon.microprofile.telemetry.TelemetryCdiExtension;
import io.helidon.microprofile.testing.junit5.AddBean;
import io.helidon.microprofile.testing.junit5.AddExtension;
import io.helidon.microprofile.testing.junit5.DisableDiscovery;
import io.helidon.microprofile.testing.junit5.HelidonTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import mesh.test.rest.incomings.controllers.api.ApiException;
import mesh.test.rest.incomings.controllers.api.ApiExceptionMapper;
import mesh.test.rest.incomings.controllers.api.PingApi;
import mesh.test.rest.incomings.controllers.api.ResponseStub;
import mesh.test.rest.incomings.exceptions.MyExceptionMapper;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.glassfish.jersey.ext.cdi1x.internal.CdiComponentProvider;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test TransmitterServiceResource.
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
@AddBean(value = TransmitterServiceResourceErrorTest.PingApiStub.class)
@AddBean(value = TransmitterServiceResourceErrorTest.PingHdrApiStub.class)
@AddBean(value = TransmitterServiceResource.class)
@AddBean(value = MyExceptionMapper.class)
@AddBean(value = ApiExceptionMapper.class)
@AddExtension(ServerCdiExtension.class)
@AddExtension(JaxRsCdiExtension.class)
@AddExtension(CdiComponentProvider.class)
@AddExtension(TelemetryCdiExtension.class)
@AddExtension(ConfigCdiExtension.class)
public class TransmitterServiceResourceErrorTest {

    /**
     * Target.
     */
    @Inject
    private WebTarget target;

    @Test
    void pingError() {
        try (Response r = this.target
            .path("api/callping")
            .request()
            .get()) {
            MatcherAssert.assertThat(r.getStatus(), Matchers.is(400));
        }
    }

    @Test
    void countError() {
        try (Response r = this.target
            .path("api/callcount")
            .request()
            .get()) {
            MatcherAssert.assertThat(r.getStatus(), Matchers.is(400));
        }
    }

    @Test
    void pinghdrError() {
        try (Response r = this.target
            .path("api/callpinghdr")
            .request()
            .get()) {
            MatcherAssert.assertThat(r.getStatus(), Matchers.is(400));
        }
    }

    /**
     * Stub for PingApi.
     *
     * @since 1.0
     * @checkstyle DesignForExtensionCheck (20 lines)
     */
    @RestClient
    public static class PingApiStub implements PingApi {
        @Override
        public String count() throws ApiException {
            throw new ApiException(new ResponseStub());
        }

        @Override
        public String ping() throws ApiException {
            throw new ApiException(new ResponseStub());
        }

        @Override
        public String reset() {
            return "3";
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
