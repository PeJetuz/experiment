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

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import mesh.test.rest.incomings.controllers.api.PingService;

/**
 * Ping controller.
 * Helidon does not support final classes.
 *
 * @since 1.0
 * @checkstyle DesignForExtensionCheck (60 lines)
 */
@Path("/api")
public class PingResource implements PingService {

    /**
     * Counter.
     */
    private final CounterService counter;

    @Inject
    public PingResource(final CounterService cservice) {
        this.counter = cservice;
    }

    @Override
    public String ping() {
        return String.valueOf(this.counter.ping());
    }

    @Override
    public String count() {
        return String.valueOf(this.counter.count());
    }

    @Override
    public String reset() {
        return String.valueOf(this.counter.reset());
    }
}
