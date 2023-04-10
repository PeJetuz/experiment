package my.test.authorization.domain.impl;

import my.test.authorization.domain.api.LoginPolicy;
import my.test.authorization.domain.api.PolicyBuilder;
import my.test.authorization.domain.api.CreatePolicy;
import my.test.authorization.domain.api.servicebus.LoginEventTransmitterBuilder;
import my.test.authorization.domain.api.store.UserBuilder;

public class PolicyBuilderImpl implements PolicyBuilder {

    private final UserBuilder userBuilder;
    private final LoginEventTransmitterBuilder loginEventTransmitterBuilder;

    public PolicyBuilderImpl(UserBuilder userBuilder, LoginEventTransmitterBuilder loginEventTransmitterBuilder) {
        this.userBuilder = userBuilder;
        this.loginEventTransmitterBuilder = loginEventTransmitterBuilder;
    }

    @Override
    public LoginPolicy buildLoginPolicy(String userName, String passwordHash) {
        return new LoginPolicyImpl(userBuilder, userName, passwordHash, loginEventTransmitterBuilder);
    }

    @Override
    public CreatePolicy buildCreatePolicy(String userName, String passwordHash) {
        return new CreatePolicyImpl(userBuilder, userName, passwordHash);
    }
}
