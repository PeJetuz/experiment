package my.test.authorization.store;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


public class UserStoreMock implements UserStore {
    private static final Map<String, AuthInfoValue> store;
    private static final AuthInfoValue GUEST_INFO_VALUE =
            new AuthInfoValue(GUEST, GUEST_PASSWORD_HASH, LocalDateTime.now(), null);

    static {
        store = new ConcurrentHashMap<>();
        store.put("Vasya", new AuthInfoValue("Vasya", "passwordHash", LocalDateTime.now(), null));
        store.put(GUEST, GUEST_INFO_VALUE);
    }

    public Optional<AuthInfoValue> findUserByName(String userName) {
        return Optional.ofNullable(store.get(userName));
    }

    public boolean isUserExists(String userName) {
        return store.containsKey(userName);
    }

    public boolean createUserAndCheckSuccessOperation(AuthInfoValue authInfoValue) {
        return store.putIfAbsent(authInfoValue.name(), authInfoValue) == null;
    }

    public void updateUserInfo(AuthInfoValue newAuthInfoValue) {
        store.put(newAuthInfoValue.name(), newAuthInfoValue);
    }
}
