package my.test.authorization.store;

import java.time.LocalDateTime;
import java.util.Optional;
import my.test.authorization.store.UserStore.AuthInfoValue;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

public class LoginUserMockTest {

    private UserStore userStore = Mockito.mock(UserStore.class);

    @Test
    public void isUserLoadedTest() {
        LoginUserMock subj = createLoadedLoginUser();

        assertTrue(subj.isUserLoaded());
    }

    private static LoginUserMock createLoadedLoginUser() {
        LoginUserMock subj = new LoginUserMock(null, null, null);
        subj.setAuthUserInfoValueForTestingOnly(Optional.of(new AuthInfoValue(null, null, null, null)));
        return subj;
    }

    @Test
    public void updateExpirationDateTimeAndToken() {
        LoginUserMock subj = createLoadedLoginUserForUpdate();
        String token = "token";

        subj.updateLastRefreshDateTimeAndToken(token);

        verify(userStore).updateUserInfo(subj.getAuthUserInfoValueForTestingOnly().get());

        assertEquals(token, subj.getAuthUserInfoValueForTestingOnly().get().token());
        assertNotNull(subj.getAuthUserInfoValueForTestingOnly().get().lastRefreshDateTime());
    }

    @Test
    public void updateExpirationDateTime() {
        LoginUserMock subj = createLoadedLoginUserForUpdate();

        subj.updateLastRefreshDateTime();

        verify(userStore).updateUserInfo(subj.getAuthUserInfoValueForTestingOnly().get());

        assertNotNull(subj.getAuthUserInfoValueForTestingOnly().get().lastRefreshDateTime());
    }

    private LoginUserMock createLoadedLoginUserForUpdate() {
        LoginUserMock subj = new LoginUserMock(userStore, null, null);
        subj.setAuthUserInfoValueForTestingOnly(Optional.of(new AuthInfoValue(null, null, null, null)));
        return subj;
    }

    @Test
    public void isExpirationDateTimeBeforeSuccessfully() {
        LoginUserMock subj = createLoadedLoginUserForExpirationDateTime();

        assertTrue(subj.isLastRefreshDateTimeBefore(
                subj.getAuthUserInfoValueForTestingOnly().get().lastRefreshDateTime().plusNanos(1L)));
    }

    @Test
    public void isExpirationDateTimeBeforeFailed() {
        LoginUserMock subj = createLoadedLoginUserForExpirationDateTime();

        assertFalse(subj.isLastRefreshDateTimeBefore(
                subj.getAuthUserInfoValueForTestingOnly().get().lastRefreshDateTime()));
    }

    @Test
    public void writeLastRefreshDateTime() {
        LoginUserMock subj = createLoadedLoginUserForExpirationDateTime();
        LocalDateTimeConsumer localDateTimeConsumer = new LocalDateTimeConsumer();

        subj.writeLastRefreshDateTime(localDateTimeConsumer::setLocalDateTime);

        assertEquals(subj.getAuthUserInfoValueForTestingOnly().get().lastRefreshDateTime(),
                localDateTimeConsumer.localDateTime);
    }

    private static LoginUserMock createLoadedLoginUserForExpirationDateTime() {
        LoginUserMock subj = new LoginUserMock(null, null, null);
        subj.setAuthUserInfoValueForTestingOnly(Optional.of(new AuthInfoValue(null, null, LocalDateTime.now(), null)));
        return subj;
    }

    @Test
    public void writeToken() {
        LoginUserMock subj = createLoadedLoginUserForWriteToken();
        StringConsumer tokenConsumer = new StringConsumer();

        subj.writeToken(tokenConsumer::setString);

        assertEquals(subj.getAuthUserInfoValueForTestingOnly().get().token(), tokenConsumer.string);
    }

    private static LoginUserMock createLoadedLoginUserForWriteToken() {
        LoginUserMock subj = new LoginUserMock(null, null, null);
        subj.setAuthUserInfoValueForTestingOnly(Optional.of(new AuthInfoValue(null, null, null, "token")));
        return subj;
    }

    @Test
    public void updateLastRefreshDateTimeAndTokenUserNotLoaded() {
        LoginUserMock user = new LoginUserMock(null, null, null);

        assertThrows(AssertionError.class, () -> user.updateLastRefreshDateTimeAndToken(null));
    }

    @Test
    public void updateLastRefreshDateTimeUserNotLoaded() {
        LoginUserMock user = new LoginUserMock(null, null, null);

        assertThrows(AssertionError.class, () -> user.updateLastRefreshDateTime());
    }

    @Test
    public void writeLastRefreshDateTimeUserNotLoaded() {
        LoginUserMock user = new LoginUserMock(null, null, null);

        assertThrows(AssertionError.class, () -> user.writeLastRefreshDateTime(null));
    }

    @Test
    public void writeTokenUserNotLoaded() {
        LoginUserMock user = new LoginUserMock(null, null, null);

        assertThrows(AssertionError.class, () -> user.writeToken(null));
    }

    @Test
    public void isLastRefreshDateTimeBeforeUserNotLoaded() {
        LoginUserMock user = new LoginUserMock(null, null, null);

        assertThrows(AssertionError.class, () -> user.isLastRefreshDateTimeBefore(null));
    }

    private static class StringConsumer {

        public String string;

        public void setString(String string) {
            this.string = string;
        }
    }

    private static class LocalDateTimeConsumer {

        public LocalDateTime localDateTime;

        public void setLocalDateTime(LocalDateTime localDateTime) {
            this.localDateTime = localDateTime;
        }
    }

}
