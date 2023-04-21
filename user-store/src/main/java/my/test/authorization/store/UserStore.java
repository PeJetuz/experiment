package my.test.authorization.store;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserStore {

    String GUEST = "Guest";
    String GUEST_PASSWORD_HASH = "passwordHash";

    Optional<AuthInfoValue> findUserByName(String userName);

    boolean isUserExists(String userName);

    void updateUserInfo(AuthInfoValue newAuthInfoValue);

    boolean createUserAndCheckSuccessOperation(AuthInfoValue authInfoValue);

    record AuthInfoValue(String name, String passwordHash, LocalDateTime lastRefreshDateTime, String token) {

        public AuthInfoValue updateLastRefreshDateTime(LocalDateTime lastRefreshDateTime) {
            return new AuthInfoValue(this.name, this.passwordHash, lastRefreshDateTime, this.token);
        }

        public AuthInfoValue updateLastRefreshDateTimeAndToken(LocalDateTime lastRefreshDateTime,
                String token) {
            return new AuthInfoValue(this.name, this.passwordHash, lastRefreshDateTime, token);
        }
    }
}
