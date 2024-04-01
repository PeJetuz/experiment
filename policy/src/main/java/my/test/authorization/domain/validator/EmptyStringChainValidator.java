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

/**
 * Empty String chain validator.
 *
 * @since 1.0
 */
public final class EmptyStringChainValidator implements Validator {

    /**
     * String to be checked.
     */
    private final String subject;

    /**
     * Method that will be called if the check fails.
     */
    private final Runnable action;

    /**
     * Next validator in case of successful verification.
     */
    private final Validator next;

    public EmptyStringChainValidator(final String subject, final Runnable action,
        final Validator next
    ) {
        this.subject = subject;
        this.action = action;
        this.next = next;
    }

    @Override
    public boolean validate() {
        final boolean result;
        if (this.subject == null || this.subject.isBlank()) {
            this.action.run();
            result = false;
        } else {
            result = this.next.validate();
        }
        return result;
    }
}
