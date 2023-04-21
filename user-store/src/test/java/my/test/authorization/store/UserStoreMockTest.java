package my.test.authorization.store;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import my.test.authorization.store.UserStore.AuthInfoValue;
import org.junit.jupiter.api.Test;

import static my.test.authorization.store.UserStore.GUEST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserStoreMockTest {

    @Test
    public void isGuestUserExists() {
        UserStoreMock subj = new UserStoreMock();
        assertTrue(subj.isUserExists(GUEST));
    }

    @Test
    public void isVasyaUserExists() {
        UserStoreMock subj = new UserStoreMock();
        assertTrue(subj.isUserExists("Vasya"));
    }

    @Test
    public void isEmptyUserExists() {
        UserStoreMock subj = new UserStoreMock();
        assertFalse(subj.isUserExists(""));
    }

    @Test
    public void guestUserLoad() {
        UserStoreMock subj = new UserStoreMock();
        assertTrue(subj.findUserByName(GUEST).isPresent());
    }

    @Test
    public void vasyaUserLoad() {
        UserStoreMock subj = new UserStoreMock();
        assertTrue(subj.findUserByName("Vasya").isPresent());
    }

    @Test
    public void emptyUserLoad() {
        UserStoreMock subj = new UserStoreMock();
        assertFalse(subj.findUserByName("").isPresent());
    }

    @Test
    public void createUserSuccessfully() {
        AuthInfoValue newUser = createNewUser();
        UserStoreMock subj = new UserStoreMock();

        assertTrue(subj.createUserAndCheckSuccessOperation(newUser));
    }

    private AuthInfoValue createNewUser() {
        Random random = ThreadLocalRandom.current();
        String userName = "userName" + random.nextLong();
        String passwordHash = "passwordHash" + random.nextLong();
        return new AuthInfoValue(userName, passwordHash, null, null);
    }

    @Test
    public void createUserAlreadyExists() {
        UserStoreMock subj = new UserStoreMock();
        AuthInfoValue expectedUser = createExpectedUser(subj);
        AuthInfoValue newUser = createNewUserFromExpected(expectedUser);

        assertFalse(subj.createUserAndCheckSuccessOperation(newUser));
    }

    @Test
    public void updateUserTest() {
        UserStoreMock subj = new UserStoreMock();
        AuthInfoValue newUser = createUser4Update(subj);

        subj.updateUserInfo(newUser);

        assertEquals(newUser, subj.findUserByName(newUser.name()).get());
    }

    private AuthInfoValue createUser4Update(UserStoreMock subj) {
        AuthInfoValue expectedUser = createExpectedUser(subj);
        return createNewUserFromExpected(expectedUser);
    }

    private AuthInfoValue createExpectedUser(UserStoreMock subj) {
        Random random = ThreadLocalRandom.current();
        String userName = "userName" + random.nextLong();
        String passwordHash = "passwordHash" + random.nextLong();
        AuthInfoValue expectedUser = new AuthInfoValue(userName, passwordHash, null, null);
        assertTrue(subj.createUserAndCheckSuccessOperation(expectedUser));
        return expectedUser;
    }

    private AuthInfoValue createNewUserFromExpected(AuthInfoValue expectedUser) {
        return new AuthInfoValue(expectedUser.name(), expectedUser.passwordHash(), null, null);
    }

}
