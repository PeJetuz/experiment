package my.test.authorization.store;

import java.time.LocalDateTime;

public interface UserStore {

    String GUEST = "Guest";
    String GUEST_PASSWORD_HASH = "passwordHash";

    AuthInfoValue USER_NOF_FOUND = new AuthInfoValue(null, null, null, null);
    AuthInfoValue USER_ALREADY_EXISTS = new AuthInfoValue(null, null, null, null);

    AuthInfoValue findUserByName(String userName);

    void updateUserInfo(AuthInfoValue newAuthInfoValue);

    AuthInfoValue createUser(AuthInfoValue authInfoValue);

    record AuthInfoValue(String name, String passwordHash, LocalDateTime lastRefreshDateTime, String token) {

        public AuthInfoValue updateLastRefreshDateTime(LocalDateTime lastRefreshDateTime) {
            return new AuthInfoValue(this.name, this.passwordHash, lastRefreshDateTime, this.token);
        }

        public AuthInfoValue updateLastRefreshDateTimeAndToken(LocalDateTime lastRefreshDateTime,
                String token) {
            return new AuthInfoValue(this.name, this.passwordHash, lastRefreshDateTime, token);
        }
    }

    record Fake(AuthInfoValue authInfoValue) implements UserStore {

        @Override
        public AuthInfoValue findUserByName(String userName) {
            return authInfoValue;
        }

        @Override
        public void updateUserInfo(AuthInfoValue newAuthInfoValue) {

        }

        @Override
        public AuthInfoValue createUser(AuthInfoValue newInfoValue) {
            return authInfoValue;
        }
    }
}
