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

import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test RFC3339DateFormat.
 *
 * @since 1.0
 * @checkstyle AbbreviationAsWordInNameCheck (5 lines)
 */
final class RFC3339DateFormatTest {

    @Test
    void parse() {
        final RFC3339DateFormat subj = new RFC3339DateFormat();
        Assertions.assertThat(subj.parse("2024-05-06T19:47:19+07:00", new ParsePosition(0)))
            .isEqualTo(new Date(1_714_999_639_000L));
    }

    @Test
    void format() {
        final RFC3339DateFormat subj = new RFC3339DateFormat();
        Assertions.assertThat(
            subj.format(
                new Date(1_714_999_639_000L),
                new StringBuffer(),
                new FieldPosition(0)
            ).toString()
            ).isEqualTo("2024-05-06T12:47:19.000+00:00");
    }

    @Test
    void canClone() {
        final RFC3339DateFormat subj = new RFC3339DateFormat();
        Assertions.assertThat(subj.clone()).isEqualTo(subj);
    }
}
