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

package my.test.rest.incomings.controllers;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test JavaTimeFormatter.
 *
 * @since 1.0
 */
final class JavaTimeFormatterTest {

    @Test
    void isIsoFormatter() {
        final JavaTimeFormatter subj = new JavaTimeFormatter();
        Assertions.assertThat(subj.getOffsetDateTimeFormatter())
            .isEqualTo(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        subj.setOffsetDateTimeFormatter(subj.getOffsetDateTimeFormatter());
    }

    @Test
    void format() {
        final JavaTimeFormatter subj = new JavaTimeFormatter();
        final LocalDateTime date = LocalDateTime.now();
        final ZoneOffset offset = ZoneOffset.UTC;
        Assertions.assertThat(subj.formatOffsetDateTime(OffsetDateTime.of(date, offset)))
            .isEqualTo(
                DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(OffsetDateTime.of(date, offset))
            );
    }

    @Test
    void parse() {
        final JavaTimeFormatter subj = new JavaTimeFormatter();
        final LocalDateTime date = LocalDateTime.now();
        final ZoneOffset offset = ZoneOffset.UTC;
        Assertions.assertThat(
            subj.parseOffsetDateTime(
                DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(OffsetDateTime.of(date, offset))
            ))
            .isEqualTo(OffsetDateTime.of(date, offset));
    }

    @Test
    void parseError() {
        final JavaTimeFormatter subj = new JavaTimeFormatter();
        Assertions.assertThatThrownBy(
            () -> {
                subj.parseOffsetDateTime("asdasdas");
            }).isInstanceOf(RuntimeException.class);
    }
}
