package my.test.authorization.rules;

import my.test.authorization.domain.api.PolicyBuilder;
import my.test.authorization.rules.create.NewUserInteractorImpl;
import my.test.authorization.rules.login.LoginUserInteractorImpl;

public class AuthFactoryImpl implements AuthFactory {

    private final PolicyBuilder policyBuilder;

    public AuthFactoryImpl(PolicyBuilder policyBuilder) {
        this.policyBuilder = policyBuilder;
    }

    @Override
    public LoginUserInteractor createLoginUserInteractor(LoginUserPresenter presenter, String userName, String passwordHash) {
        return new LoginUserInteractorImpl(policyBuilder, presenter, userName, passwordHash);
    }

    @Override
    public NewUserInteractor createNewUserProcess(NewUserPresenter presenter, String userName, String passwordHash) {
        return new NewUserInteractorImpl(policyBuilder, presenter, userName, passwordHash);
    }
}
