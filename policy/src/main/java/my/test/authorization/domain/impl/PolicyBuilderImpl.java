package my.test.authorization.domain.impl;

import my.test.authorization.domain.api.LoginPolicy;
import my.test.authorization.domain.api.PolicyBuilder;
import my.test.authorization.domain.api.CreatePolicy;
import my.test.authorization.domain.api.store.UserBuilder;

public class PolicyBuilderImpl implements PolicyBuilder {

    private final UserBuilder userBuilder;

    public PolicyBuilderImpl(UserBuilder userBuilder) {
        this.userBuilder = userBuilder;
    }

    @Override
    public LoginPolicy buildLoginPolicy(String userName, String passwordHash) {
        return new LoginPolicyImpl(userBuilder, userName, passwordHash);
    }

    @Override
    public CreatePolicy buildCreatePolicy(String userName, String passwordHash) {
        return new CreatePolicyImpl(userBuilder, userName, passwordHash);
    }
}
