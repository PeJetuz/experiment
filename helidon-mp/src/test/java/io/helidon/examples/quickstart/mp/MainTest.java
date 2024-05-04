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

package io.helidon.examples.quickstart.mp;

import io.helidon.metrics.api.MetricsFactory;
import io.helidon.microprofile.testing.junit5.HelidonTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test class.
 *
 * @since 1.0
 */
@SuppressWarnings({"PMD.JUnitAssertionsShouldIncludeMessage", "PMD.JUnitTestClassShouldBeFinal"})
@HelidonTest
class MainTest {

    /**
     * Registry.
     */
    @Inject
    private MetricRegistry registry;

    /**
     * Target.
     */
    @Inject
    private WebTarget target;

    @AfterAll
    static void clear() {
        MetricsFactory.closeAll();
    }

    @Test
    void testMicroprofileMetrics() {
        String message = this.target.path("simple-greet/Joe")
            .request()
            .get(String.class);
        MatcherAssert.assertThat(message, Matchers.is("Hello Joe"));
        final Counter counter = this.registry.counter("personalizedGets");
        final double before = counter.getCount();
        message = this.target.path("simple-greet/Eric")
            .request()
            .get(String.class);
        MatcherAssert.assertThat(message, Matchers.is("Hello Eric"));
        final double after = counter.getCount();
        Assertions.assertEquals(
            1d,
            after - before,
            "Difference in personalized greeting counter between successive calls"
        );
    }

    @Test
    void testHealth() {
        final Response response = this.target
            .path("health")
            .request()
            .get();
        MatcherAssert.assertThat(response.getStatus(), Matchers.is(200));
    }

    @Test
    void testGreet() {
        final Message message = this.target
            .path("simple-greet")
            .request()
            .get(Message.class);
        MatcherAssert.assertThat(message.getMsg(), Matchers.is("Hello World!"));
    }

    @Test
    void testGreetings() {
        Message jsmessage = this.target
            .path("greet/Joe")
            .request()
            .get(Message.class);
        MatcherAssert.assertThat(jsmessage.getMsg(), Matchers.is("Hello Joe!"));
        try (Response r = this.target
            .path("greet/greeting")
            .request()
            .put(Entity.entity("{\"greeting\" : \"Hola\"}", MediaType.APPLICATION_JSON))) {
            MatcherAssert.assertThat(r.getStatus(), Matchers.is(204));
        }
        jsmessage = this.target
            .path("greet/Jose")
            .request()
            .get(Message.class);
        MatcherAssert.assertThat(jsmessage.getMsg(), Matchers.is("Hola Jose!"));
    }
}
