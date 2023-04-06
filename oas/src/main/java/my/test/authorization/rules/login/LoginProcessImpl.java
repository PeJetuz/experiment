package my.test.authorization.rules.login;

import my.test.authorization.domain.api.api.LoginPolicy;
import my.test.authorization.domain.api.api.PolicyBuilder;
import my.test.authorization.rules.LoginPresenter;
import my.test.authorization.rules.LoginProcess;

public class LoginProcessImpl implements LoginProcess {

    private final LoginPolicy policy;
    private final LoginPresenter presenter;

    public LoginProcessImpl(PolicyBuilder policyBuilder, LoginPresenter presenter, String userName,
            String passwordHash) {
        this.policy = policyBuilder.buildLoginPolicy(userName, passwordHash);
        this.presenter = presenter;
    }

    @Override
    public void login() {
        policy.loginUser();
        if (policy.isLoginSuccess()) {
            policy.writeTokenAndExpirationDateTime(presenter::writeToken, presenter::writeExpirationDateTime);
        } else {
            presenter.initUserNotFoundResponseModel();
        }
    }
}
