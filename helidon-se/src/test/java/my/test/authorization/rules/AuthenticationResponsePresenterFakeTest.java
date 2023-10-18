package my.test.authorization.rules;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthenticationResponsePresenterFakeTest {

    @Test
    public void test() {
        AuthenticationResponsePresenter.Fake fake = new AuthenticationResponsePresenter.Fake();
        fake.send();
        fake.success();
        fake.incorrectPassword();
        fake.userNotFound();

        assertTrue(fake.incorrectPassword);
        assertTrue(fake.userNotFound);
    }
}
