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
import mesh.test.rest.incomings.controllers.api.ApiException;
import mesh.test.rest.incomings.controllers.api.PingApi;
import org.assertj.core.api.Assertions;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.glassfish.jersey.ext.cdi1x.internal.CdiComponentProvider;
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
@AddBean(value = TransmitterServiceResourceTest.PingApiStub.class)
@AddBean(value = TransmitterServiceResourceTest.PingHdrApiStub.class)
@AddBean(value = TransmitterServiceResource.class)
@AddExtension(ServerCdiExtension.class)
@AddExtension(JaxRsCdiExtension.class)
@AddExtension(CdiComponentProvider.class)
@AddExtension(TelemetryCdiExtension.class)
@AddExtension(ConfigCdiExtension.class)
public class TransmitterServiceResourceTest {

    /**
     * Target.
     */
    @Inject
    private WebTarget target;

    @Test
    void ping() {
        final String count = this.target
            .path("api/callcount")
            .request()
            .get(String.class);
        Assertions.assertThat(count).isIn("1");
    }

    @Test
    void count() {
        final String count = this.target
            .path("api/callping")
            .request()
            .get(String.class);
        Assertions.assertThat(count).isIn("2");
    }

    @Test
    void pinghdr() {
        final String count = this.target
            .path("api/callpinghdr")
            .request()
            .get(String.class);
        Assertions.assertThat(count).isIn("4");
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
        public String count() {
            return "1";
        }

        @Override
        public String ping() {
            return "2";
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
            return "4";
        }
    }
}
