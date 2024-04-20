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

package my.test.authorization.domain.events;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Implementation of event dispatcher.
 *
 * @since 1.0
 */
public final class EventDispatcherImpl implements EventDispatcher<DomainEvent> {

    /**
     * Map of events and their subscribers.
     */
    private final Map<String, List<Consumer<DomainEvent>>> subs;

    /**
     * Ctor.
     * @param hnds Event handlers.
     */
    public EventDispatcherImpl(final Map<String, Consumer<DomainEvent>>... hnds) {
        this(createSubscribers(hnds));
    }

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public EventDispatcherImpl(final Map<String, List<Consumer<DomainEvent>>> hnds) {
        this.subs = hnds;
    }

    @Override
    public <T extends DomainEvent> void fire(final T event) {
        if (this.subs.containsKey(event.eventName())) {
            this.subs.get(event.eventName()).forEach(consumer -> consumer.accept(event));
        }
    }

    @SuppressWarnings("PMD.UseVarargs")
    private static Map<String, List<Consumer<DomainEvent>>> createSubscribers(
        final Map<String, Consumer<DomainEvent>>[] hnds
    ) {
        final Map<String, List<Consumer<DomainEvent>>> subscribers = new HashMap<>();
        for (final Map<String, Consumer<DomainEvent>> subscriber : hnds) {
            subscriber.forEach(
                (name, catcher) -> {
                    if (!subscribers.containsKey(name)) {
                        subscribers.put(name, new ArrayList<>(1));
                    }
                    subscribers.get(name).add(catcher);
                });
        }
        return subscribers;
    }
}
