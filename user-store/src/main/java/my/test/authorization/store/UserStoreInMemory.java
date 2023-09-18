package my.test.authorization.store;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class UserStoreInMemory implements UserStore {

    private static final Map<String, AuthInfoValue> store;
    private static final AuthInfoValue GUEST_INFO_VALUE =
            new AuthInfoValue(GUEST, GUEST_PASSWORD_HASH, LocalDateTime.now(), null);

    static {
        store = new ConcurrentHashMap<>();
        store.put("Vasya", new AuthInfoValue("Vasya", "passwordHash", LocalDateTime.now(), null));
        store.put(GUEST, GUEST_INFO_VALUE);
    }

    public AuthInfoValue findUserByName(String userName) {
        AuthInfoValue user = store.get(userName);
        if (user == null) {
            return UserStore.USER_NOF_FOUND;
        }
        return user;
    }

    public AuthInfoValue createUser(AuthInfoValue authInfoValue) {
        if (createUserSuccess(authInfoValue)) {
            return authInfoValue;
        }
        return UserStore.USER_ALREADY_EXISTS;
    }

    private boolean createUserSuccess(AuthInfoValue authInfoValue) {
        return store.putIfAbsent(authInfoValue.name(), authInfoValue) == null;
    }

    public void updateUserInfo(AuthInfoValue newAuthInfoValue) {
        store.put(newAuthInfoValue.name(), newAuthInfoValue);
    }
}
