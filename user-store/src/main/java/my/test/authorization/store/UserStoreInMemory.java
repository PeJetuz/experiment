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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Representation of user storage in memory.
 *
 * @since 1.0
 */
final class UserStoreInMemory implements UserStore {

    /**
     * Memory storage.
     */
    @SuppressWarnings("PMD.AvoidDirectAccessToStaticFields")
    private static final Map<String, AuthInfoValue> STORE = new ConcurrentHashMap<>();

    /**
     * Guest user.
     */
    private static final AuthInfoValue GUEST_INFO_VALUE =
        new AuthInfoValue(UserStore.GUEST, UserStore.GUEST_PASSWORD, LocalDateTime.now(), null);

    static {
        UserStoreInMemory.STORE.put(
            "Vasya",
            new AuthInfoValue(
                "Vasya", "password", LocalDateTime.now(), null
            )
        );
        UserStoreInMemory.STORE.put(UserStore.GUEST, UserStoreInMemory.GUEST_INFO_VALUE);
    }

    @Override
    public AuthInfoValue findUserByName(final String name) {
        final AuthInfoValue result;
        final AuthInfoValue user = UserStoreInMemory.STORE.get(name);
        if (user == null) {
            result = UserStore.USER_NOF_FOUND;
        } else {
            result = user;
        }
        return result;
    }

    @Override
    public AuthInfoValue createUser(final AuthInfoValue entity) {
        final AuthInfoValue result;
        if (createUserSuccess(entity)) {
            result = entity;
        } else {
            result = UserStore.USER_EXISTS;
        }
        return result;
    }

    @Override
    public void updateUserInfo(final AuthInfoValue entity) {
        STORE.put(entity.name(), entity);
    }

    private static boolean createUserSuccess(final AuthInfoValue entity) {
        return STORE.putIfAbsent(entity.name(), entity) == null;
    }
}
