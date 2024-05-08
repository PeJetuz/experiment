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

import jakarta.enterprise.context.ApplicationScoped;
import java.util.concurrent.atomic.AtomicLong;
import mesh.test.rest.incomings.exceptions.MyException;

/**
 * Counter service implementation.
 *
 * @since 1.0
 * @checkstyle DesignForExtensionCheck (70 lines)
 */
@ApplicationScoped
public class CounterServiceImpl implements CounterService {

    /**
     * Counter.
     */
    private final AtomicLong counter;

    public CounterServiceImpl() {
        this.counter = new AtomicLong(1);
    }

    @Override
    public long reset() {
        final long current = this.counter.get();
        this.counter.set(1);
        return current;
    }

    @Override
    public long count() {
        return this.counter.get();
    }

    @Override
    public long ping() {
        final long current = this.counter.getAndIncrement();
        if (current > 10 && current < 15) {
            throw new MyException(current);
        }
        return current;
    }
}
