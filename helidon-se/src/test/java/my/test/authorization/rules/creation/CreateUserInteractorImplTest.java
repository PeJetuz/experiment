package my.test.authorization.rules.creation;

import my.test.authorization.domain.api.AuthenticationPolicy;
import my.test.authorization.domain.api.CreationPolicy;
import my.test.authorization.domain.api.PolicyFactory;
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
                new PolicyFactory.Fake(new AuthenticationPolicy.Fake(), new CreationPolicy.Fake()),
                new UserInfo(null, null),
                presenter,
                Validator.TRUE);

        assertEquals(presenter, subj.createNewUserAndGetPresenter());
    }

    @Test
    public void createNewUserAndGetPresenterConstructorTest() {
        CreationUserResponsePresenter.Fake presenter = new CreationUserResponsePresenter.Fake();
        CreateUserInteractorImpl subj = new CreateUserInteractorImpl(
                new PolicyFactory.Fake(new AuthenticationPolicy.Fake(), new CreationPolicy.Fake()),
                new UserInfo(null, null),
                presenter);

        assertEquals(presenter, subj.createNewUserAndGetPresenter());
    }
}
