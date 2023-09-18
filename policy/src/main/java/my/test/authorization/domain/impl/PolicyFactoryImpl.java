package my.test.authorization.domain.impl;

import my.test.authorization.domain.api.AuthenticationPolicy;
import my.test.authorization.domain.api.AuthenticationResponseFactory;
import my.test.authorization.domain.api.CreationPolicy;
import my.test.authorization.domain.api.CreationUserResponseFactory;
import my.test.authorization.domain.api.PolicyFactory;
import my.test.authorization.domain.api.UserInfo;
import my.test.authorization.domain.api.servicebus.LogonEventTransmitterBuilder;
import my.test.authorization.domain.api.store.UserBuilder;

public class PolicyFactoryImpl implements PolicyFactory {

    private final UserBuilder userBuilder;
    private final LogonEventTransmitterBuilder logonEventTransmitterBuilder;

    public PolicyFactoryImpl(UserBuilder userBuilder, LogonEventTransmitterBuilder logonEventTransmitterBuilder) {
        this.userBuilder = userBuilder;
        this.logonEventTransmitterBuilder = logonEventTransmitterBuilder;
    }

    @Override
    public CreationPolicy buildCreationPolicy(UserInfo userInfo, CreationUserResponseFactory responseFactory) {
        return new CreationPolicyImpl(userBuilder, userInfo, responseFactory);
    }

    @Override
    public AuthenticationPolicy buildAuthenticatePolicy(UserInfo userInfo,
            AuthenticationResponseFactory responseFactory) {
        return new AuthenticationPolicyImpl(userBuilder, userInfo, responseFactory,
                logonEventTransmitterBuilder.createLoginEventTransmitter(userInfo.name()));
    }
}
