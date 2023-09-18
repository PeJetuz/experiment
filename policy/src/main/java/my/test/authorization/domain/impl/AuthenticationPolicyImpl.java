package my.test.authorization.domain.impl;

import java.time.LocalDateTime;
import my.test.authorization.domain.api.AuthenticationPolicy;
import my.test.authorization.domain.api.AuthenticationResponseFactory;
import my.test.authorization.domain.api.UserInfo;
import my.test.authorization.domain.api.servicebus.LogonEventTransmitter;
import my.test.authorization.domain.api.store.AuthenticateUser;
import my.test.authorization.domain.api.store.AuthenticateUser.AuthenticationResult;
import my.test.authorization.domain.api.store.UserBuilder;

public class AuthenticationPolicyImpl implements AuthenticationPolicy {

    private final AuthenticateUser user;
    private final AuthenticationResponseFactory responseFactory;
    private final LogonEventTransmitter logonEventTransmitter;

    public AuthenticationPolicyImpl(UserBuilder userBuilder, UserInfo userInfo,
            AuthenticationResponseFactory responseFactory, LogonEventTransmitter logonEventTransmitter) {
        this.user = userBuilder.createAuthenticatedUser(userInfo, responseFactory);
        this.responseFactory = responseFactory;
        this.logonEventTransmitter = logonEventTransmitter;
    }

    @Override
    public void authenticate() {
        if (success(user)) {
            logonEventTransmitter.sendUserLogonEvent(LocalDateTime.now().toString());
        }
    }

    private boolean success(AuthenticateUser user) {
        return user.authenticate() == AuthenticationResult.SUCCESS;
    }
}
