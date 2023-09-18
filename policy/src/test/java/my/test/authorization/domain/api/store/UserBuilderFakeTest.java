package my.test.authorization.domain.api.store;

import my.test.authorization.domain.api.store.AuthenticateUser.AuthenticationResult;
import my.test.authorization.domain.api.store.NewUser.CreationResult;
import my.test.authorization.domain.api.store.UserBuilder.Fake;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserBuilderFakeTest {

    @Test
    public void createFake() {
        AuthenticateUser.Fake authenticatedUser = new AuthenticateUser.Fake(AuthenticationResult.UNSUCCESS);
        NewUser.Fake newUser = new NewUser.Fake(CreationResult.SUCCESS);
        Fake subj = new Fake(authenticatedUser, newUser);
        assertEquals(authenticatedUser, subj.createAuthenticatedUser(null, null));
        assertEquals(newUser, subj.createNewUser(null, null));
    }
}
