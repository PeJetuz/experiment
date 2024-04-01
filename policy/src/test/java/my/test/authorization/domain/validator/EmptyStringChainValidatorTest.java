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

package my.test.authorization.domain.validator;

import my.test.authorization.domain.events.DomainEvent;
import my.test.authorization.domain.events.EmptyNameEvent;
import my.test.authorization.domain.events.EmptyPasswordEvent;
import my.test.authorization.domain.events.IpAuthenticationSuccessfulEvent;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test cases for the EmptyStringChainValidator.
 *
 * @since 1.0
 */
final class EmptyStringChainValidatorTest {

    @Test
    void validateNullString() {
        final DomainEvent event = new EmptyNameEvent.EmptyNameEventImpl();
        final EmptyStringChainValidator subj = new EmptyStringChainValidator(
            null,
            event,
            null
        );
        Assertions.assertThat(subj.validate()).isEqualTo(event);
    }

    @Test
    void validateEmptyString() {
        final DomainEvent event = new EmptyPasswordEvent.EmptyPasswordEventImpl();
        final EmptyStringChainValidator subj = new EmptyStringChainValidator(
            "",
            event,
            null
        );
        Assertions.assertThat(subj.validate()).isEqualTo(event);
    }

    @Test
    void validateString() {
        final DomainEvent event =
            new IpAuthenticationSuccessfulEvent.IpAuthenticationSuccessfulEventImpl();
        final EmptyStringChainValidator subj = new EmptyStringChainValidator(
            "any",
            event,
            Validator.TRUE
        );
        Assertions.assertThat(subj.validate()).isEqualTo(Validator.TRUE);
    }
}
