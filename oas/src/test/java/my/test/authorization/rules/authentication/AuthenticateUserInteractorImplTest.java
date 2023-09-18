package my.test.authorization.rules.authentication;

import my.test.authorization.domain.api.AuthenticationPolicy;
import my.test.authorization.domain.api.CreationPolicy;
import my.test.authorization.domain.api.PolicyFactory;
import my.test.authorization.domain.api.UserInfo;
import my.test.rest.incomings.controllers.AuthenticationResponseModel;
import my.test.authorization.rules.AuthenticationResponsePresenter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthenticateUserInteractorImplTest {

    @Test
    public void authenticateAndGetPresenter() {
        AuthenticationResponsePresenter.Fake expectedPresenter = new AuthenticationResponsePresenter.Fake();
        AuthenticateUserInteractorImpl subj = new AuthenticateUserInteractorImpl(
                new PolicyFactory.Fake(new AuthenticationPolicy.Fake(), new CreationPolicy.Fake()),
                new UserInfo(null, null), expectedPresenter);

        AuthenticationResponseModel presenter = subj.authenticateAndGetPresenter();

        assertEquals(expectedPresenter, presenter);
    }
}
