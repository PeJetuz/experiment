package my.test.rest.incomings.eureka.rules;

import my.test.eureka.policy.PolicyBuilder;
import my.test.rest.incomings.eureka.rules.create.CreateProcessImpl;
import my.test.rest.incomings.eureka.rules.login.LoginProcessImpl;

public class AuthFactoryImpl implements AuthFactory {

    private final PolicyBuilder policyBuilder;

    public AuthFactoryImpl(PolicyBuilder policyBuilder) {
        this.policyBuilder = policyBuilder;
    }

    @Override
    public LoginProcess createLoginProcess(LoginPresenter presenter, String userName, String passwordHash) {
        return new LoginProcessImpl(policyBuilder, presenter, userName, passwordHash);
    }

    @Override
    public CreateProcess createNewUserProcess(CreatePresenter presenter, String userName, String passwordHash) {
        return new CreateProcessImpl(policyBuilder, presenter, userName, passwordHash);
    }
}
