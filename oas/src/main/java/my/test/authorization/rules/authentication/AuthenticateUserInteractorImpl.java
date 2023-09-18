package my.test.authorization.rules.authentication;

import my.test.authorization.domain.api.AuthenticationPolicy;
import my.test.authorization.domain.api.PolicyFactory;
import my.test.authorization.domain.api.UserInfo;
import my.test.authorization.rules.AuthenticateUserInteractor;
import my.test.rest.incomings.controllers.AuthenticationResponseModel;
import my.test.authorization.rules.AuthenticationResponsePresenter;

public class AuthenticateUserInteractorImpl implements AuthenticateUserInteractor {

    private final AuthenticationPolicy authenticationPolicy;
    private final UserInfo userInfo;
    private final AuthenticationResponsePresenter responseBuilder;

    public AuthenticateUserInteractorImpl(PolicyFactory policyFactory, UserInfo userInfo,
            AuthenticationResponsePresenter responseBuilder) {
        this.authenticationPolicy = policyFactory.buildAuthenticatePolicy(userInfo, responseBuilder);
        this.userInfo = userInfo;
        this.responseBuilder = responseBuilder;
    }

    @Override
    public AuthenticationResponseModel authenticateAndGetPresenter() {
        authenticationPolicy.authenticate();
        return responseBuilder;
    }
}
