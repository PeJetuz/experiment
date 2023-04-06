package my.test.authorization.domain.impl;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import my.test.authorization.domain.api.LoginPolicy;
import my.test.authorization.domain.api.store.User;
import my.test.authorization.domain.api.store.UserBuilder;

public class LoginPolicyImpl implements LoginPolicy {

    /**
     * Expiration time in minutes
     */
    private static final int EXPIRATION_INTERVAL = 10;
    private final User user;

    public LoginPolicyImpl(UserBuilder userBuilder, String userName, String passwordHash) {
        if (userName == null || userName.isBlank()) {
            userName = GUEST;
            passwordHash = GUEST_PASSWORD_HASH;
        }
        this.user = userBuilder.createUser(userName, passwordHash);
    }

    @Override
    public void loginUser() {
        user.loadUser();
        if (user.isUserLoaded()) {
            if (isUserLoginExpired()) {
                user.updateExpirationDateTimeAndToken(generateToken());
            } else {
                user.updateExpirationDateTime();
            }
        }
    }

    @Override
    public boolean isLoginSuccess() {
        return user.isUserLoaded();
    }

    @Override
    public void writeTokenAndExpirationDateTime(Consumer<String> tokenConsumer,
            Consumer<LocalDateTime> expirationDateTime) {
        user.writeExpirationDateTime(expirationDateTime);
        user.writeToken(tokenConsumer);
    }

    private String generateToken() {
        return "" + LocalDateTime.now();
    }

    private boolean isUserLoginExpired() {
        return user.isExpirationDateTimeBefore(LocalDateTime.now().minusMinutes(EXPIRATION_INTERVAL));
    }
}
