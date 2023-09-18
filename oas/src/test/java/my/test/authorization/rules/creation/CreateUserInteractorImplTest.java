package my.test.authorization.rules.creation;

import my.test.authorization.domain.api.CreationPolicy;
import my.test.authorization.domain.api.PolicyFactory;
import my.test.authorization.rules.CreationUserResponsePresenter;
import my.test.authorization.rules.creation.validator.Validator;
import org.junit.jupiter.api.Test;

public class CreateUserInteractorImplTest {

    @Test
    public void validateTrue() {
        CreationUserResponsePresenter.Fake responseBuilder = new CreationUserResponsePresenter.Fake();
        CreateUserInteractorImpl subj = new CreateUserInteractorImpl(
                new PolicyFactory.Fake(null, new CreationPolicy.Fake()), null,
                responseBuilder, Validator.TRUE);

        subj.createNewUserAndGetPresenter();
    }

    @Test
    public void validateFalse() {
        CreationUserResponsePresenter.Fake responseBuilder = new CreationUserResponsePresenter.Fake();
        CreateUserInteractorImpl subj = new CreateUserInteractorImpl(new PolicyFactory.Fake(null, null), null,
                responseBuilder, new Validator.FalseValidator());

        subj.createNewUserAndGetPresenter();
    }
}
