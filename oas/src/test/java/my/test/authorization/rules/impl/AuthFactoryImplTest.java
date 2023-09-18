package my.test.authorization.rules.impl;

import my.test.authorization.domain.api.AuthenticationPolicy;
import my.test.authorization.domain.api.CreationPolicy;
import my.test.authorization.domain.api.PolicyFactory;
import my.test.authorization.rules.AuthenticateUserInteractor;
import my.test.authorization.rules.CreateUserInteractor;
import my.test.authorization.rules.CreationUserResponsePresenter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuthFactoryImplTest {

    private AuthFactoryImpl subj = new AuthFactoryImpl(
            new PolicyFactory.Fake(new AuthenticationPolicy.Fake(), new CreationPolicy.Fake()));

    @Test
    public void createNewUserProcess() {
        CreateUserInteractor createUserInteractor = subj.createNewUserInteractor(null, null,
                new CreationUserResponsePresenter.Fake());

        assertNotNull(createUserInteractor);
    }

    @Test
    public void createAuthenticateUserInteractor() {
        AuthenticateUserInteractor authenticateUserInteractor = subj.createAuthenticateUserInteractor(null, null, null);

        assertNotNull(authenticateUserInteractor);
    }
}
