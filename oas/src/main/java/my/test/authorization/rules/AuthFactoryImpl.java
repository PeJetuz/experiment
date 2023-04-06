package my.test.authorization.rules;

import my.test.authorization.domain.api.PolicyBuilder;
import my.test.authorization.rules.create.CreateProcessImpl;
import my.test.authorization.rules.login.LoginProcessImpl;

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
