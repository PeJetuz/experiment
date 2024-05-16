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
import jakarta.inject.Inject;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;
import mesh.test.rest.incomings.exceptions.MyException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Counter service implementation.
 *
 * @since 1.0
 * @checkstyle DesignForExtensionCheck (70 lines)
 */
@ApplicationScoped
public class CounterServiceImpl implements CounterService {

    /**
     * Log.
     */
    private static final System.Logger LOG = System.getLogger(CounterServiceImpl.class.getName());

    /**
     * Counter.
     */
    private final AtomicLong counter;

    /**
     * Delay when an error occurs.
     */
    private final Delay delay;

    @Inject
    public CounterServiceImpl(
        @ConfigProperty(name = "counterservice.delay") final Integer cnt
    ) {
        this(
            new AtomicLong(1),
            () -> Thread.sleep(Duration.ofSeconds(cnt))
        );
    }

    public CounterServiceImpl(final AtomicLong cnt, final Delay rdelay) {
        this.counter = cnt;
        this.delay = rdelay;
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
    @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
    public long ping() {
        final long current = this.counter.getAndIncrement();
        LOG.log(System.Logger.Level.TRACE, String.format("Old counter = %d", current));
        if (current > 10 && current < 15) {
            LOG.log(
                System.Logger.Level.ERROR,
                String.format(
                    "Error on counter = %d",
                    current
                )
            );
            try {
                this.delay.run();
            } catch (final InterruptedException exception) {
                LOG.log(
                    System.Logger.Level.ERROR,
                    String.format(
                        "Error on counter = %d, interrupted: %s",
                        current,
                        exception.getMessage()
                    )
                );
                throw new RuntimeException("Delay interrupted", exception);
            }
            throw new MyException(current);
        }
        return current;
    }

    /**
     * Wrapper for sleep.
     *
     * @since 1.0
     */
    interface Delay {
        void run() throws InterruptedException;
    }
}
