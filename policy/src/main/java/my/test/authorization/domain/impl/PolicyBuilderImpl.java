package my.test.authorization.domain.impl;

import my.test.authorization.domain.api.LoginPolicy;
import my.test.authorization.domain.api.PolicyBuilder;
import my.test.authorization.domain.api.CreatePolicy;
import my.test.authorization.domain.api.servicebus.LogonEventTransmitterBuilder;
import my.test.authorization.domain.api.store.UserBuilder;

public class PolicyBuilderImpl implements PolicyBuilder {

    private final UserBuilder userBuilder;
    private final LogonEventTransmitterBuilder logonEventTransmitterBuilder;

    public PolicyBuilderImpl(UserBuilder userBuilder, LogonEventTransmitterBuilder logonEventTransmitterBuilder) {
        this.userBuilder = userBuilder;
        this.logonEventTransmitterBuilder = logonEventTransmitterBuilder;
    }

    @Override
    public LoginPolicy buildLoginPolicy(String userName, String passwordHash) {
        return new LoginPolicyImpl(userBuilder, userName, passwordHash, logonEventTransmitterBuilder);
    }

    @Override
    public CreatePolicy buildCreatePolicy(String userName, String passwordHash) {
        return new CreatePolicyImpl(userBuilder, userName, passwordHash);
    }
}
