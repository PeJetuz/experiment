package my.test.authorization.domain.impl;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import my.test.authorization.domain.api.LoginPolicy;
import my.test.authorization.domain.api.servicebus.LogonEventTransmitter;
import my.test.authorization.domain.api.servicebus.LogonEventTransmitterBuilder;
import my.test.authorization.domain.api.store.LoginUser;
import my.test.authorization.domain.api.store.UserBuilder;

public class LoginPolicyImpl implements LoginPolicy {

    private static final int SESSION_EXPIRATION_INTERVAL = 10;
    private final LoginUser user;
    private final LogonEventTransmitter logonEventTransmitter;

    public LoginPolicyImpl(UserBuilder userBuilder, String userName, String passwordHash,
            LogonEventTransmitterBuilder logonEventTransmitterBuilder) {
        if (userName == null || userName.isBlank()) {
            this.user = userBuilder.createGuestUser();
        } else {
            this.user = userBuilder.createLoginUser(userName, passwordHash);
        }
        this.logonEventTransmitter = logonEventTransmitterBuilder.createLoginEventTransmitter(userName);
    }

    @Override
    public void loginUser() {
        user.loadUser();
        if (user.isUserLoaded()) {
            updateUserAndSendLogonEvent();
        }
    }

    private void updateUserAndSendLogonEvent() {
        if (isUserLoginExpired()) {
            user.updateLastRefreshDateTimeAndToken(generateToken());
        } else {
            user.updateLastRefreshDateTime();
            user.writeToken(logonEventTransmitter::sendUserLogonEvent);
        }
    }

    private boolean isUserLoginExpired() {
        return user.isLastRefreshDateTimeBefore(LocalDateTime.now().minusMinutes(SESSION_EXPIRATION_INTERVAL));
    }

    private String generateToken() {
        return "" + LocalDateTime.now();
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
}
