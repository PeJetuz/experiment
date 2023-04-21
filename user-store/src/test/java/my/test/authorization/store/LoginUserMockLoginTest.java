package my.test.authorization.store;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import my.test.authorization.store.UserStore.AuthInfoValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static my.test.authorization.store.UserStore.GUEST;
import static my.test.authorization.store.UserStore.GUEST_PASSWORD_HASH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class LoginUserMockLoginTest {
    private UserStore userStore = Mockito.mock(UserStore.class);

    private String userName;
    private String passwordHash;


    @BeforeEach
    public void beforeEachTestInit() {
        Random random = ThreadLocalRandom.current();
        userName = "userName" + random.nextLong();
        passwordHash = "passwordHash" + random.nextLong();
    }

    @Test
    public void createGuestUserTest() {
        LoginUserMock subj = LoginUserMock.createGuestUser(null);

        assertEquals(GUEST, subj.getUserNameForTestingOnly());
        assertEquals(GUEST_PASSWORD_HASH, subj.getUserPasswordHashForTestingOnly());
    }

    @Test
    public void loginUserWithPasswordSuccessfully() {
        LoginUserMock subj = new LoginUserMock(userStore, userName, passwordHash);
        Optional<AuthInfoValue> expectedAuthInfoValue = createExpectedAuthInfoValue4LoginUserSuccessfully();

        subj.loadUser();

        assertEquals(expectedAuthInfoValue, subj.getAuthUserInfoValueForTestingOnly());
    }

    @Test
    public void loginUserWithIncorrectPasswordFailed() {
        LoginUserMock subj = createLoginUserWithIncorrectPassword();

        subj.loadUser();

        assertTrue(subj.getAuthUserInfoValueForTestingOnly().isEmpty());
    }

    private LoginUserMock createLoginUserWithIncorrectPassword() {
        createExpectedAuthInfoValue4LoginUserSuccessfully();
        return new LoginUserMock(userStore, userName, passwordHash + "asd");
    }

    private Optional<AuthInfoValue> createExpectedAuthInfoValue4LoginUserSuccessfully() {
        Optional<AuthInfoValue> expectedAuthInfoValue = Optional.of(
                new AuthInfoValue(userName, passwordHash, null, null));
        when(userStore.findUserByName(userName)).thenReturn(expectedAuthInfoValue);
        return expectedAuthInfoValue;
    }

    @Test
    public void loginWithEmptyUserFailed() {
        LoginUserMock subj = createLoginEmptyUser();

        subj.loadUser();

        assertTrue(subj.getAuthUserInfoValueForTestingOnly().isEmpty());
    }

    private LoginUserMock createLoginEmptyUser() {
        when(userStore.findUserByName(userName)).thenReturn(Optional.empty());
        return new LoginUserMock(userStore, userName, passwordHash);
    }
}
