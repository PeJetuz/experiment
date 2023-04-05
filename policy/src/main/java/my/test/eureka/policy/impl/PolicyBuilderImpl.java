package my.test.eureka.policy.impl;

import my.test.eureka.policy.CreatePolicy;
import my.test.eureka.policy.LoginPolicy;
import my.test.eureka.policy.PolicyBuilder;
import my.test.eureka.policy.store.UserBuilder;

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
