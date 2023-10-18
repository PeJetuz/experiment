package my.test.authorization.rules.impl;

import my.test.authorization.domain.api.PolicyFactory;
import my.test.authorization.domain.api.UserInfo;
import my.test.authorization.rules.AuthFactory;
import my.test.authorization.rules.AuthenticateUserInteractor;
import my.test.authorization.rules.AuthenticationResponsePresenter;
import my.test.authorization.rules.CreateUserInteractor;
import my.test.authorization.rules.CreationUserResponsePresenter;
import my.test.authorization.rules.authentication.AuthenticateUserInteractorImpl;
import my.test.authorization.rules.creation.CreateUserInteractorImpl;

public class AuthFactoryImpl implements AuthFactory {

    private final PolicyFactory policyFactory;

    public AuthFactoryImpl(PolicyFactory policyFactory) {
        this.policyFactory = policyFactory;
    }

    @Override
    public CreateUserInteractor createNewUserInteractor(String userName, String passwordHash,
            CreationUserResponsePresenter presenter) {
        UserInfo userInfo = new UserInfo(userName, passwordHash);
        return new CreateUserInteractorImpl(policyFactory, userInfo, presenter);
    }

    @Override
    public AuthenticateUserInteractor createAuthenticateUserInteractor(String userName, String passwordHash,
            AuthenticationResponsePresenter presenter) {
        UserInfo userInfo = new UserInfo(userName, passwordHash);
        return new AuthenticateUserInteractorImpl(policyFactory, userInfo, presenter);
    }
}
