package my.test.authorization.rules.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import my.test.authorization.domain.api.PolicyFactory;
import my.test.authorization.domain.api.UserInfo;
import my.test.authorization.domain.api.servicebus.LogonEventTransmitter;
import my.test.authorization.domain.api.servicebus.LogonEventTransmitterBuilder;
import my.test.authorization.domain.impl.PolicyFactoryImpl;
import my.test.authorization.rules.AuthFactory;
import my.test.authorization.rules.AuthenticateUserInteractor;
import my.test.authorization.rules.AuthenticationResponsePresenter;
import my.test.authorization.rules.CreateUserInteractor;
import my.test.authorization.rules.CreationUserResponsePresenter;
import my.test.authorization.rules.authentication.AuthenticateUserInteractorImpl;
import my.test.authorization.rules.creation.CreateUserInteractorImpl;
import my.test.authorization.store.UserMockBuilderImpl;

@ApplicationScoped
public class AuthFactoryImpl implements AuthFactory {

    private final PolicyFactory policyFactory;

//    @Inject
//    public AuthFactoryImpl(PolicyFactory policyFactory) {
//        this.policyFactory = policyFactory;
//    }

    public AuthFactoryImpl() {
        this.policyFactory = new PolicyFactoryImpl(new UserMockBuilderImpl(),
                        new LogonEventTransmitterBuilder.Fake(new LogonEventTransmitter.Fake()));
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
