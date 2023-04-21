package my.test.authorization.domain.impl;

import my.test.authorization.domain.api.servicebus.LogonEventTransmitterBuilder;
import my.test.authorization.domain.api.store.LoginUser;
import my.test.authorization.domain.api.store.UserBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginPolicyImplTest {

    private UserBuilder userBuilder = Mockito.mock(UserBuilder.class);
    private LogonEventTransmitterBuilder eventTransmitterBuilder = Mockito.mock(LogonEventTransmitterBuilder.class);
    private LoginUser user = Mockito.mock(LoginUser.class);

    @Test
    public void createGuestUserFromNullName() {
        new LoginPolicyImpl(userBuilder, null, null, eventTransmitterBuilder);

        verify(userBuilder).createGuestUser();
    }

    @Test
    public void createGuestUserFromEmptyName() {
        new LoginPolicyImpl(userBuilder, "", null, eventTransmitterBuilder);

        verify(userBuilder).createGuestUser();
    }

    @Test
    public void checkLoginSuccess() {
        LoginPolicyImpl subj = createLoginUserForCheckLoginSuccess();

        assertTrue(subj.isLoginSuccess());
    }

    private LoginPolicyImpl createLoginUserForCheckLoginSuccess() {
        when(userBuilder.createGuestUser()).thenReturn(user);
        when(user.isUserLoaded()).thenReturn(true);
        return new LoginPolicyImpl(userBuilder, null, null, eventTransmitterBuilder);
    }

    @Test
    public void checkWriteTokenAndLastRefreshDateTime() {
        LoginPolicyImpl subj = createLoginUserForWriteTokenAndLastRefreshDateTime();

        subj.writeTokenAndLastRefreshDateTime(null, null);

        verify(user).writeToken(null);
        verify(user).writeLastRefreshDateTime(null);
    }

    private LoginPolicyImpl createLoginUserForWriteTokenAndLastRefreshDateTime() {
        when(userBuilder.createGuestUser()).thenReturn(user);
        return new LoginPolicyImpl(userBuilder, null, null, eventTransmitterBuilder);
    }
}
