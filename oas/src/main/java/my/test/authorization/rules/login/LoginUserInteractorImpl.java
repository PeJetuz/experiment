package my.test.authorization.rules.login;

import my.test.authorization.domain.api.LoginPolicy;
import my.test.authorization.domain.api.PolicyBuilder;
import my.test.authorization.rules.LoginUserPresenter;
import my.test.authorization.rules.LoginUserInteractor;

public class LoginUserInteractorImpl implements LoginUserInteractor {

    private final LoginPolicy policy;
    private final LoginUserPresenter presenter;

    public LoginUserInteractorImpl(PolicyBuilder policyBuilder, LoginUserPresenter presenter, String userName,
            String passwordHash) {
        this.policy = policyBuilder.buildLoginPolicy(userName, passwordHash);
        this.presenter = presenter;
    }

    @Override
    public void login() {
        policy.loginUser();
        if (policy.isLoginSuccess()) {
            policy.writeTokenAndLastRefreshDateTime(presenter::writeToken, presenter::writeExpirationDateTime);
        } else {
            presenter.initUserNotFoundResponseModel();
        }
    }
}
