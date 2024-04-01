package my.test.authorization.rules;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreationUserResponsePresenterDummyTest {

    @Test
    public void test() {
        CreationUserResponsePresenter.Fake fake = new CreationUserResponsePresenter.Fake();
        fake.send();
        fake.success();
        fake.emptyPassword();
        fake.emptyName();
        fake.userExists();

        assertTrue(fake.invalidPasswordHashField);
        assertTrue(fake.invalidUserNameField);
        assertTrue(fake.userAlreadyExists);
    }
}
