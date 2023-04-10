package my.test.authorization.store;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import my.test.authorization.domain.api.LoginPolicy;
import my.test.authorization.domain.api.store.User;


public class UserMock implements User {

    private static final Map<String, AuthInfoValue> store;
    private static final AuthInfoValue GUEST_INFO_VALUE =
            new AuthInfoValue(LoginPolicy.GUEST, LoginPolicy.GUEST_PASSWORD_HASH, LocalDateTime.now(), null);

    private AuthInfoValue info;
    private final String name;
    private final String password;


    static {
        store = new ConcurrentHashMap<>();
        store.put("Vasya", new AuthInfoValue("Vasya", "passwordHash", LocalDateTime.now(), null));
        store.put(LoginPolicy.GUEST, GUEST_INFO_VALUE);
    }

    public UserMock(String userName) {
        this.name = userName;
        this.password = null;
        if (userName != null) {
            info = store.getOrDefault(userName, null);
        } else {
            info = GUEST_INFO_VALUE;
        }
    }

    public UserMock(String userName, String passwordHash) {
        this.name = userName;
        this.password = passwordHash;
    }

    @Override
    public void loadUser() {
        AuthInfoValue loadInfo = store.getOrDefault(name, null);
        if (loadInfo != null && loadInfo.passwordHash.equals(password)) {
            info = loadInfo;
        } else {
            info = null;
        }
    }

    @Override
    public boolean isUserLoaded() {
        return info != null;
    }

    @Override
    public boolean validatePasswordHash(String passwordHash) {
        return info != null && info.passwordHash.equals(passwordHash);
    }

    @Override
    public LocalDateTime updateLastRefreshDateTime() {
        Objects.requireNonNull(info);
        LocalDateTime currentTime = LocalDateTime.now();
        AuthInfoValue authInfoValue = store.get(info.name);
        AuthInfoValue newAuthInfoValue = authInfoValue.updateLastRefreshDateTime(currentTime);
        store.put(info.name, newAuthInfoValue);
        info = newAuthInfoValue;
        return currentTime;
    }

    @Override
    public LocalDateTime updateLastRefreshDateTimeAndToken(String token) {
        Objects.requireNonNull(info);
        LocalDateTime currentTime = LocalDateTime.now();
        AuthInfoValue authInfoValue = store.get(info.name);
        AuthInfoValue newAuthInfoValue = authInfoValue.updateLastRefreshDateTimeAndToken(currentTime, token);
        store.put(info.name, newAuthInfoValue);
        info = newAuthInfoValue;
        return currentTime;
    }

    @Override
    public boolean isUserExists() {
        return store.containsKey(name);
    }

    @Override
    public boolean createNewUser(String token) {
        AuthInfoValue authInfoValue = new AuthInfoValue(name, password, LocalDateTime.now(), token);
        boolean isUserCreated = store.putIfAbsent(name, authInfoValue) == null;
        if (isUserCreated) {
            info = authInfoValue;
        }
        return isUserCreated;
    }


    @Override
    public void writeLastRefreshDateTime(Consumer<LocalDateTime> lastRefreshDateTime) {
        lastRefreshDateTime.accept(info.lastRefreshDateTime);
    }

    @Override
    public void writeToken(Consumer<String> tokenConsumer) {
        tokenConsumer.accept(info.token);
    }

    @Override
    public boolean isLastRefreshDateTime(LocalDateTime compareTo) {
        return info.lastRefreshDateTime.isBefore(compareTo);
    }

    private record AuthInfoValue(String name, String passwordHash, LocalDateTime lastRefreshDateTime, String token) {

        public AuthInfoValue updateLastRefreshDateTime(LocalDateTime lastRefreshDateTime) {
            return new AuthInfoValue(this.name, this.passwordHash, lastRefreshDateTime, this.token);
        }

        public AuthInfoValue updateLastRefreshDateTimeAndToken(LocalDateTime lastRefreshDateTime, String token) {
            return new AuthInfoValue(this.name, this.passwordHash, lastRefreshDateTime, token);
        }
    }

    /////////////////////
    //// for testing ////
    /////////////////////
    protected String whatIsYourName() {
        return info.name;
    }

    protected String whatIsYourPasswordHash() {
        return info.passwordHash;
    }
}
