package my.test.authorization.rules;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreationUserResponsePresenterFakeTest {

    @Test
    public void test() {
        CreationUserResponsePresenter.Fake fake = new CreationUserResponsePresenter.Fake();
        fake.send();
        fake.success();
        fake.invalidPasswordHashField();
        fake.invalidUserNameField();
        fake.userAlreadyExists();

        assertTrue(fake.invalidPasswordHashField);
        assertTrue(fake.invalidUserNameField);
        assertTrue(fake.userAlreadyExists);
    }
}
