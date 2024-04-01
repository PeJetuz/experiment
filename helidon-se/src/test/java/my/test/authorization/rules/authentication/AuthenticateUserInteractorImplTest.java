package my.test.authorization.rules.authentication;

import my.test.authorization.domain.api.AuthenticationPolicy;
import my.test.authorization.domain.api.CreationPolicy.Dummy;
import my.test.authorization.domain.api.PolicyFactory.PolicyFactorySpy;
import my.test.authorization.domain.api.UserInfo;
import my.test.authorization.rules.AuthenticationResponsePresenter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class AuthenticateUserInteractorImplTest {
    @Test
    void createNewUserAndGetPresenterTest() {
        AuthenticationResponsePresenter.Fake presenter = new AuthenticationResponsePresenter.Fake();
        AuthenticateUserInteractorImpl subj = new AuthenticateUserInteractorImpl(
                new PolicyFactorySpy(new AuthenticationPolicy.Dummy(), new Dummy()),
                new UserInfo(null, null),
                presenter);

        assertEquals(presenter, subj.authenticateAndGetPresenter());
    }
}
