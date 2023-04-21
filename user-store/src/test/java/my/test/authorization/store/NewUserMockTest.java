package my.test.authorization.store;

import java.time.LocalDateTime;
import java.util.Optional;
import my.test.authorization.store.UserStore.AuthInfoValue;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

public class NewUserMockTest {

    @Test
    public void createNewUserSuccessfully() {
        NewUserMock subj = createNewUserMockSuccessfully();

        subj.createNewUser(null);

        assertTrue(subj.getAuthUserInfoValueForTestingOnly().isPresent());
    }

    private static NewUserMock createNewUserMockSuccessfully() {
        String name = "Vasya1Vasya";
        String passwordHash = "passwordHash1Vasya";
        UserStore store = Mockito.mock(UserStore.class);
        when(store.createUserAndCheckSuccessOperation(isA(AuthInfoValue.class))).thenReturn(true);
        return new NewUserMock(store, name, passwordHash);
    }

    @Test
    public void createNewUserAlreadyExist() {
        NewUserMock subj = createNewUserMockAlreadyExist();

        subj.createNewUser(null);

        assertTrue(subj.getAuthUserInfoValueForTestingOnly().isEmpty());
    }

    private static NewUserMock createNewUserMockAlreadyExist() {
        UserStore store = Mockito.mock(UserStore.class);
        when(store.createUserAndCheckSuccessOperation(isA(AuthInfoValue.class))).thenReturn(false);
        return new NewUserMock(store, null, null);
    }

    @Test
    public void isUserExists() {
        NewUserMock subj = createNewUserExist();

        assertTrue(subj.isUserExists());
    }

    private static NewUserMock createNewUserExist() {
        UserStore store = Mockito.mock(UserStore.class);
        when(store.isUserExists(null)).thenReturn(true);
        return new NewUserMock(store, null, null);
    }

    @Test
    public void isNewUserCreatedFailed() {
        NewUserMock subj = new NewUserMock(null, null, null);

        assertFalse(subj.isNewUserCreatedSuccessfully());
    }

    @Test
    public void isNewUserCreatedSuccessfully() {
        NewUserMock subj = new NewUserMock(null, null, null);
        subj.setAuthUserInfoValueForTestingOnly(Optional.of(new AuthInfoValue(null, null, null, null)));

        assertTrue(subj.isNewUserCreatedSuccessfully());
    }

    @Test
    public void writeLastRefreshDateTime() {
        NewUserMock subj = createNewUserMockWithLastRefreshDateTime();
        LocalDateTimeConsumer localDateTimeConsumer = new LocalDateTimeConsumer();

        subj.writeLastRefreshDateTime(localDateTimeConsumer::setLocalDateTime);

        assertEquals(subj.getAuthUserInfoValueForTestingOnly().get().lastRefreshDateTime(),
                localDateTimeConsumer.localDateTime);
    }

    private static NewUserMock createNewUserMockWithLastRefreshDateTime() {
        NewUserMock subj = new NewUserMock(null, null, null);
        subj.setAuthUserInfoValueForTestingOnly(Optional.of(
                new AuthInfoValue(null, null, LocalDateTime.now(), null)));
        return subj;
    }

    @Test
    public void writeToken() {
        NewUserMock subj = createNewUserMockWithToken();
        TokenConsumer tokenConsumer = new TokenConsumer();

        subj.writeToken(tokenConsumer::setToken);

        assertEquals(subj.getAuthUserInfoValueForTestingOnly().get().token(), tokenConsumer.token);
    }

    private NewUserMock createNewUserMockWithToken() {
        NewUserMock subj = new NewUserMock(null, null, null);
        subj.setAuthUserInfoValueForTestingOnly(Optional.of(
                new AuthInfoValue(null, null, null, "token")));
        return subj;
    }

    @Test
    public void writeTokenUserHasNotBeCreated() {
        NewUserMock user = new NewUserMock(null, null, null);

        assertThrows(AssertionError.class, () -> user.writeToken(null));
    }

    @Test
    public void updateExpirationDateTimeUserHasNotBeCreated() {
        NewUserMock user = new NewUserMock(null, null, null);

        assertThrows(AssertionError.class, () -> user.writeLastRefreshDateTime(null));
    }

    private static class TokenConsumer {

        public String token;

        public void setToken(String token) {
            this.token = token;
        }
    }

    private static class LocalDateTimeConsumer {

        public LocalDateTime localDateTime;

        public void setLocalDateTime(LocalDateTime localDateTime) {
            this.localDateTime = localDateTime;
        }
    }

}
