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

package my.test.authorization.domain.api.servicebus;

/**
 * Interface for instantiating a Service Bus to send a logon event.
 *
 * @since 1.0
 */
public interface LoginEventTransmitterBuilder {

    /**
     * Creates a service bus instance to dispatch a logon event.
     *
     * @return Returns the transmitter for sending the login event.
     */
    LoginEventTransmitter createLoginEventTransmitter();

    /**
     * Creates a service bus instance to dispatch the new user creation event.
     *
     * @return Returns the transmitter for sending the new user creation event.
     */
    CreateNewUserEventTransmitter createNewUserEventTransmitter();

    record Stub(LoginEventTransmitter ltransmitter, CreateNewUserEventTransmitter crtransmitter)
        implements LoginEventTransmitterBuilder {
        public Stub() {
            this(new LoginEventTransmitter.Dummy(), new CreateNewUserEventTransmitter.Dummy());
        }

        @Override
        public LoginEventTransmitter createLoginEventTransmitter() {
            return this.ltransmitter;
        }

        @Override
        public CreateNewUserEventTransmitter createNewUserEventTransmitter() {
            return this.crtransmitter;
        }
    }
}
