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
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import java.util.HashMap;
import java.util.Map;
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
    public String callcount() {
        try {
            return this.ping.count();
        } catch (final ApiException aex) {
            LOG.log(System.Logger.Level.ERROR, aex);
            throw new MyException(aex.getResponse(), aex);
        }
    }

    @Override
    public String callping() {
        try {
            return this.ping.ping();
        } catch (final ApiException aex) {
            LOG.log(System.Logger.Level.ERROR, aex);
            throw new MyException(aex.getResponse(), aex);
        }
    }

    @GET
    @Path("/callpinghdr")
    @Produces("application/json")
    public String callpinghdr(@Context final HttpHeaders headers) throws JsonProcessingException {
        final Map<String, String> hdrs = new HashMap<>();
        headers.getRequestHeaders().forEach((key, value) -> hdrs.put(key, value.getFirst()));
        LOG.log(System.Logger.Level.INFO, new ObjectMapper().writeValueAsString(hdrs));
        try {
            return this.pinghdr.pinghdr();
        } catch (final ApiException aex) {
            LOG.log(System.Logger.Level.ERROR, aex);
            throw new MyException(aex.getResponse(), aex);
        }
    }
}
