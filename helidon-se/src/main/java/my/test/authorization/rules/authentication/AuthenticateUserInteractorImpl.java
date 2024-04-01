package my.test.authorization.rules.authentication;

import my.test.authorization.domain.api.AuthenticationPolicy;
import my.test.authorization.domain.api.PolicyFactory;
import my.test.authorization.domain.api.UserInfo;
import my.test.authorization.rules.AuthenticateUserInteractor;
import my.test.authorization.rules.AuthenticationResponsePresenter;
import my.test.rest.incomings.controllers.AuthenticationResponseModel;

public class AuthenticateUserInteractorImpl implements AuthenticateUserInteractor {

    private final AuthenticationPolicy authenticationPolicy;
    private final UserInfo userInfo;
    private final AuthenticationResponsePresenter responsePresenter;

    public AuthenticateUserInteractorImpl(PolicyFactory policyFactory, UserInfo userInfo,
            AuthenticationResponsePresenter responsePresenter) {
        this.authenticationPolicy = policyFactory.makeAuthenticatePolicy(userInfo, responsePresenter);
        this.userInfo = userInfo;
        this.responsePresenter = responsePresenter;
    }

    @Override
    public AuthenticationResponseModel authenticateAndGetPresenter() {
        authenticationPolicy.authenticate();
        return responsePresenter;
    }
}
