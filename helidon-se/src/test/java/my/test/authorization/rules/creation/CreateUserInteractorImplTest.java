package my.test.authorization.rules.creation;

import my.test.authorization.domain.api.AuthenticationPolicy;
import my.test.authorization.domain.api.CreationPolicy.Dummy;
import my.test.authorization.domain.api.PolicyFactory.PolicyFactorySpy;
import my.test.authorization.domain.api.UserInfo;
import my.test.authorization.rules.CreationUserResponsePresenter;
import my.test.authorization.rules.creation.validator.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateUserInteractorImplTest {

    @Test
    public void createNewUserAndGetPresenterTest() {
        CreationUserResponsePresenter.Fake presenter = new CreationUserResponsePresenter.Fake();
        CreateUserInteractorImpl subj = new CreateUserInteractorImpl(
                new PolicyFactorySpy(new AuthenticationPolicy.Dummy(), new Dummy()),
                new UserInfo(null, null),
                presenter,
                Validator.TRUE);

        assertEquals(presenter, subj.createNewUserAndGetPresenter());
    }

    @Test
    public void createNewUserAndGetPresenterConstructorTest() {
        CreationUserResponsePresenter.Fake presenter = new CreationUserResponsePresenter.Fake();
        CreateUserInteractorImpl subj = new CreateUserInteractorImpl(
                new PolicyFactorySpy(new AuthenticationPolicy.Dummy(), new Dummy()),
                new UserInfo(null, null),
                presenter);

        assertEquals(presenter, subj.createNewUserAndGetPresenter());
    }
}
