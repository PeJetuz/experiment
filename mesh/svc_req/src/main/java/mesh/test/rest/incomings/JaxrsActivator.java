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

package mesh.test.rest.incomings;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import mesh.test.rest.incomings.controllers.TransmitterServiceResource;
import mesh.test.rest.incomings.controllers.api.ApiExceptionMapper;
import mesh.test.rest.incomings.controllers.api.PingApi;
import mesh.test.rest.incomings.exceptions.MyExceptionMapper;

/**
 * Application runner.
 *
 * @since 1.0
 * @checkstyle DesignForExtensionCheck (60 lines)
 */
@ApplicationScoped
@ApplicationPath("/")
public class JaxrsActivator extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> set = new HashSet<>();
        set.add(PingApi.class);
        set.add(TransmitterServiceResource.class);
        set.add(MyExceptionMapper.class);
        set.add(ApiExceptionMapper.class);
        return Collections.unmodifiableSet(set);
    }
}
