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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

/**
 * A simple JAX-RS resource to greet you. Examples:
 *
 * Get default greeting message:
 * curl -X GET http://localhost:8080/greet
 *
 * Get greeting message for Joe:
 * curl -X GET http://localhost:8080/greet/Joe
 *
 * Change greeting
 * curl -X PUT -H "Content-Type: application/json" -d '{"greeting" : "Howdy"}' http://localhost:8080/greet/greeting
 *
 * The message is returned as a JSON object.
 *
 * @since 1.0
 */
@Path("/greet")
@RequestScoped
public class GreetResource {

    /**
     * The greeting message provider.
     */
    private final GreetingProvider grprovider;

    /**
     * Using constructor injection to get a configuration property.
     * By default this gets the value from META-INF/microprofile-config
     *
     * @param provider The configured greeting message.
     */
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    @Inject
    public GreetResource(final GreetingProvider provider) {
        this.grprovider = provider;
    }

    /**
     * Return a worldly greeting message.
     *
     * @return Message {@link Message}.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Message getDefaultMessage() {
        return this.createResponse("World");
    }

    /**
     * Return a greeting message using the name that was provided.
     *
     * @param name The name to greet.
     * @return Message {@link Message}.
     */
    @Path("/{name}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Message getMessage(@PathParam("name") final String name) {
        return this.createResponse(name);
    }

    /**
     * Set the greeting to use in future messages.
     *
     * @param message Message containing the new greeting.
     * @return Response {@link Response}.
     */
    @Path("/greeting")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RequestBody(
        name = "greeting",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(type = SchemaType.OBJECT, requiredProperties = "greeting")
        )
    )
    @APIResponses({
        @APIResponse(name = "normal", responseCode = "204", description = "Greeting updated"),
        @APIResponse(name = "missing 'greeting'", responseCode = "400",
            description = "JSON did not contain setting for 'greeting'")
    })
    public Response updateGreeting(final Message message) {
        final Response response;
        if (message.getGreeting() == null || message.getGreeting().isEmpty()) {
            final Message error = new Message();
            error.setMsg("No greeting provided");
            response = Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        } else {
            this.grprovider.setMessage(message.getGreeting());
            response = Response.status(Response.Status.NO_CONTENT).build();
        }
        return response;
    }

    private Message createResponse(final String who) {
        final String msg = String.format("%s %s!", this.grprovider.getMessage(), who);
        return new Message(msg);
    }
}
