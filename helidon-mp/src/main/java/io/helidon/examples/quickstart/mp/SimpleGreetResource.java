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

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

/**
 * A simple JAX-RS resource to greet you. Examples:
 *
 * Get default greeting message:
 * curl -X GET http://localhost:8080/simple-greet
 *
 * The message is returned as a JSON object.
 *
 * @since 1.0
 */
@Path("/simple-greet")
public class SimpleGreetResource {

    /**
     * Counter name.
     *
     * @checkstyle ConstantUsageCheck (18 lines)
     */
    private static final String COUNTER_NAME = "personalizedGets";

    /**
     * Counter description.
     */
    private static final String COUNTER_DESC = "Counts personalized GET operations";

    /**
     * Timer name.
     */
    private static final String TIMER_NAME = "allGets";

    /**
     * Timer description.
     */
    private static final String TIMER_DESCRIPTION = "Tracks all GET operations";

    /**
     * Message.
     */
    private final String message;

    @Inject
    public SimpleGreetResource(@ConfigProperty(name = "app.greeting") final String message) {
        this.message = message;
    }

    /**
     * Return a worldly greeting message.
     *
     * @return Message {@link Message}.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Message getDefaultMessage() {
        final String msg = String.format("%s %s!", this.message, "World");
        return new Message(msg);
    }

    /**
     * Request message.
     *
     * @param name Name.
     * @return Message.
     * @checkstyle NonStaticMethodCheck (19 lines)
     */
    @Path("/{name}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Counted(
        name = SimpleGreetResource.COUNTER_NAME,
        absolute = true,
        description = SimpleGreetResource.COUNTER_DESC
    )
    @Timed(
        name = SimpleGreetResource.TIMER_NAME,
        description = SimpleGreetResource.TIMER_DESCRIPTION,
        unit = MetricUnits.SECONDS,
        absolute = true
    )
    public String getMessage(@PathParam("name") final String name) {
        return String.format("Hello %s", name);
    }
}
