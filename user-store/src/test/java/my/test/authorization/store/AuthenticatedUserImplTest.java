package my.test.authorization.store;

import java.time.LocalDateTime;
import my.test.authorization.domain.api.UserInfo;
import my.test.authorization.domain.api.store.AuthenticateUser.AuthenticationResult;
import my.test.authorization.store.UserStore.AuthInfoValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthenticatedUserImplTest {

    private static final String USER_NAME = "test name";
    private static final String USER_PASSWORD = "test password";
    private static final String EXPECTED_TOKEN = "ABCDEF";
    private static final LocalDateTime EXPIRED_TOKEN_TIME = LocalDateTime.now().minusMinutes(11L);

    @Test
    public void authenticateUserNotFound() {
        AuthenticationResponseBuilderSpy factory = new AuthenticationResponseBuilderSpy();

        AuthenticationResult result = new AuthenticatedUserImpl(new UserStore.Fake(UserStore.USER_NOF_FOUND),
                createEmptyUserInfo(), factory).authenticate();

        assertEquals(AuthenticationResult.UNSUCCESS, result);
        assertTrue(factory.userNotFound);
    }

    private UserInfo createEmptyUserInfo() {
        return new UserInfo(null, null);
    }

    @Test
    public void authenticateIncorrectUserPassword() {
        AuthenticationResponseBuilderSpy factory = new AuthenticationResponseBuilderSpy();

        AuthenticationResult result = new AuthenticatedUserImpl(new UserStore.Fake(createAuthInfoValue(null)),
                createEmptyUserInfo(), factory).authenticate();

        assertEquals(AuthenticationResult.UNSUCCESS, result);
        assertTrue(factory.incorrectPassword);
    }

    private AuthInfoValue createAuthInfoValue(LocalDateTime expirationDateTime) {
        return new AuthInfoValue(USER_NAME, USER_PASSWORD, expirationDateTime, EXPECTED_TOKEN);
    }

    @Test
    public void authenticateUserSuccess() {
        LocalDateTime expirationDateTime = LocalDateTime.now();
        AuthenticationResponseBuilderSpy factory = new AuthenticationResponseBuilderSpy();

        AuthenticationResult result = new AuthenticatedUserImpl(new UserStore.Fake(
                createAuthInfoValue(expirationDateTime)),
                createUserInfoWithPassword(), factory).authenticate();

        assertEquals(AuthenticationResult.SUCCESS, result);
        assertEquals(USER_NAME, factory.name);
        assertEquals(EXPECTED_TOKEN, factory.token);
        assertTrue(factory.expirationDateTime.equals(expirationDateTime)
                || factory.expirationDateTime.isAfter(expirationDateTime));
    }

    private UserInfo createUserInfoWithPassword() {
        return new UserInfo(null, USER_PASSWORD);
    }

    @Test
    public void authenticateUserSuccessUpdateExpirationTimeAndToken() {
        AuthenticationResponseBuilderSpy factory = new AuthenticationResponseBuilderSpy();
        AuthInfoValue authInfoValue = createAuthInfoValue(EXPIRED_TOKEN_TIME);

        AuthenticationResult result = new AuthenticatedUserImpl(new UserStore.Fake(authInfoValue),
                createUserInfoWithPassword(), factory).authenticate();

        LocalDateTime now = LocalDateTime.now();

        assertEquals(AuthenticationResult.SUCCESS, result);
        assertEquals(USER_NAME, factory.name);
        assertNotNull(factory.token);
        assertNotEquals(EXPECTED_TOKEN, factory.token);
        assertTrue(factory.expirationDateTime.isAfter(EXPIRED_TOKEN_TIME)
                && (factory.expirationDateTime.equals(now) || factory.expirationDateTime.isBefore(now)));
    }
}
