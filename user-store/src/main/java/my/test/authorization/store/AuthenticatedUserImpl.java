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
import my.test.authorization.domain.api.store.AuthenticateUser;
import my.test.authorization.domain.events.DomainEvent;
import my.test.authorization.domain.events.UserAuthenticationSuccessfulEvent;
import my.test.authorization.domain.events.UserNotFoundEvent;
import my.test.authorization.domain.events.UserPasswordFailedEvent;
import my.test.authorization.store.UserStore.AuthInfoValue;

/**
 * User store implementation.
 *
 * @since 1.0
 */
final class AuthenticatedUserImpl implements AuthenticateUser {

    /**
     * Token validity period in minutes.
     */
    private static final int EXP_INTERVAL = 10;

    /**
     * Store.
     */
    private final UserStore store;

    /**
     * User information to load.
     */
    private final UserInfo uinfo;

    AuthenticatedUserImpl(final UserStore store, final UserInfo uinfo) {
        this.store = store;
        this.uinfo = uinfo;
    }

    @Override
    public DomainEvent authenticate() {
        final DomainEvent event;
        final AuthInfoValue entity = this.store.findUserByName(this.uinfo.name());
        if (userFound(entity)) {
            if (this.passwordCorrect(entity)) {
                final AuthInfoValue updated = this.refreshLastAccessTime(entity);
                event = buildSuccessOperation(updated);
            } else {
                event = new UserPasswordFailedEvent.UserPasswordFailedEventImpl();
            }
        } else {
            event = new UserNotFoundEvent.UserNotFoundEventImpl();
        }
        return event;
    }

    private static DomainEvent buildSuccessOperation(final AuthInfoValue entity) {
        return new UserAuthenticationSuccessfulEvent.UserAuthenticationSuccessfulEventImpl(
            entity.name(),
            entity.token(),
            entity.lastTime()
        );
    }

    private static boolean expired(final LocalDateTime lasttime) {
        return lasttime.plusMinutes(AuthenticatedUserImpl.EXP_INTERVAL)
            .isBefore(LocalDateTime.now());
    }

    private static boolean userFound(final AuthInfoValue entity) {
        return entity != UserStore.USER_NOF_FOUND;
    }

    private String geterateToken() {
        return this.uinfo.name() + LocalDateTime.now();
    }

    private boolean passwordCorrect(final AuthInfoValue entity) {
        return entity.password().equals(this.uinfo.passwordHash());
    }

    private AuthInfoValue refreshLastAccessTime(final AuthInfoValue entity) {
        final AuthInfoValue updated;
        if (expired(entity.lastTime())) {
            updated = entity.updateLastRefreshDateTimeAndToken(
                LocalDateTime.now(),
                this.geterateToken()
            );
        } else {
            updated = entity.updateLastRefreshDateTime(LocalDateTime.now());
        }
        this.store.updateUserInfo(updated);
        return updated;
    }
}
