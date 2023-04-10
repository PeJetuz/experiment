package my.test.authorization.domain.impl;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import my.test.authorization.domain.api.LoginPolicy;
import my.test.authorization.domain.api.servicebus.LoginEventTransmitter;
import my.test.authorization.domain.api.servicebus.LoginEventTransmitterBuilder;
import my.test.authorization.domain.api.store.User;
import my.test.authorization.domain.api.store.UserBuilder;

public class LoginPolicyImpl implements LoginPolicy {

    /**
     * Expiration time in minutes
     */
    private static final int EXPIRATION_INTERVAL = 10;
    private final User user;
    private final LoginEventTransmitter loginEventTransmitter;

    public LoginPolicyImpl(UserBuilder userBuilder, String userName, String passwordHash,
            LoginEventTransmitterBuilder loginEventTransmitterBuilder) {
        if (userName == null || userName.isBlank()) {
            userName = User.GUEST;
            passwordHash = User.GUEST_PASSWORD_HASH;
        }
        this.user = userBuilder.createUser(userName, passwordHash);
        this.loginEventTransmitter = loginEventTransmitterBuilder.createLoginEventTransmitter(userName);
    }

    @Override
    public void loginUser() {
        user.loadUser();
        if (user.isUserLoaded()) {
            if (isUserLoginExpired()) {
                user.updateLastRefreshDateTimeAndToken(generateToken());
            } else {
                user.updateLastRefreshDateTime();
                loginEventTransmitter.sendUserLoginEvent();
            }
        }
    }

    @Override
    public boolean isLoginSuccess() {
        return user.isUserLoaded();
    }

    @Override
    public void writeTokenAndLastRefreshDateTime(Consumer<String> tokenConsumer,
            Consumer<LocalDateTime> lastRefreshDateTime) {
        user.writeLastRefreshDateTime(lastRefreshDateTime);
        user.writeToken(tokenConsumer);
    }

    private String generateToken() {
        return "" + LocalDateTime.now();
    }

    private boolean isUserLoginExpired() {
        return user.isLastRefreshDateTime(LocalDateTime.now().minusMinutes(EXPIRATION_INTERVAL));
    }
}
