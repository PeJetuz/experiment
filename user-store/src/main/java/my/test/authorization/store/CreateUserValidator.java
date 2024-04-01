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

package my.test.authorization.store;

import java.time.LocalDateTime;
import my.test.authorization.domain.api.UserInfo;
import my.test.authorization.domain.events.DomainEvent;
import my.test.authorization.domain.events.UserAlreadyExistsEvent;
import my.test.authorization.domain.events.UserCreatedSuccessfullyEvent;
import my.test.authorization.domain.validator.Validator;

/**
 * Creates a new user and returns an event indicating success or that the user already exists.
 * This is the last validator in the chain.
 *
 * @since 1.0
 */
final class CreateUserValidator implements Validator {

    /**
     * Store.
     */
    private final UserStore store;

    /**
     * User information to load.
     */
    private final UserInfo uinfo;

    CreateUserValidator(final UserStore store, final UserInfo uinfo) {
        this.store = store;
        this.uinfo = uinfo;
    }

    @Override
    public DomainEvent validate() {
        final UserStore.AuthInfoValue entity = this.store.createUser(
            new UserStore.AuthInfoValue(
                this.uinfo.name(),
                this.uinfo.passwordHash(),
                LocalDateTime.now(),
                this.geterateToken()
            )
        );
        final DomainEvent event;
        if (userAlreadyExists(entity)) {
            event = new UserAlreadyExistsEvent.UserAlreadyExistsEventImpl();
        } else {
            event = new UserCreatedSuccessfullyEvent.UserCreatedSuccessfullyEventImpl(
                entity.name(),
                entity.token(),
                entity.lastTime()
            );
        }
        return event;
    }

    private String geterateToken() {
        return this.uinfo.name() + LocalDateTime.now();
    }

    private static boolean userAlreadyExists(final UserStore.AuthInfoValue entity) {
        return entity == UserStore.USER_EXISTS;
    }
}
