package my.test.rest.incomings.eureka.rules.login;

import my.test.eureka.policy.LoginPolicy;
import my.test.eureka.policy.PolicyBuilder;
import my.test.rest.incomings.eureka.rules.LoginPresenter;
import my.test.rest.incomings.eureka.rules.LoginProcess;

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
