package my.test.authorization.rules.impl;

import my.test.authorization.domain.api.PolicyFactory;
import my.test.authorization.rules.AuthenticationResponsePresenter;
import my.test.authorization.rules.CreationUserResponsePresenter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuthFactoryImplTest {

    @Test
    public void createNewUserInteractorTest() {
        AuthFactoryImpl subj = new AuthFactoryImpl(new PolicyFactory.Fake(null, null));

        assertNotNull(subj.createNewUserInteractor(null, null, new CreationUserResponsePresenter.Fake()));
    }

    @Test
    public void createAuthenticateUserInteractorTest() {
        AuthFactoryImpl subj = new AuthFactoryImpl(new PolicyFactory.Fake(null, null));

        assertNotNull(subj.createAuthenticateUserInteractor(null, null, new AuthenticationResponsePresenter.Fake()));
    }
}
