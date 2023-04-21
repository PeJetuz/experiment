package my.test.authorization.domain.impl;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import my.test.authorization.domain.api.servicebus.LogonEventTransmitter;
import my.test.authorization.domain.api.servicebus.LogonEventTransmitterBuilder;
import my.test.authorization.domain.api.store.LoginUser;
import my.test.authorization.domain.api.store.UserBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginUserLoginPolicyImplTest {

    private UserBuilder userBuilder = Mockito.mock(UserBuilder.class);
    private LogonEventTransmitterBuilder eventTransmitterBuilder = Mockito.mock(LogonEventTransmitterBuilder.class);
    private LoginUser user = Mockito.mock(LoginUser.class);

    @Test
    public void checkUnloadedLoginUser() {
        LoginPolicyImpl subj = createUnloadedLoginUser();

        subj.loginUser();

        verify(user, times(0)).isLastRefreshDateTimeBefore(isA(LocalDateTime.class));
    }

    private LoginPolicyImpl createUnloadedLoginUser() {
        String userName = "checkLoginUserNotLoaded";
        when(userBuilder.createLoginUser(userName, null)).thenReturn(user);
        when(user.isUserLoaded()).thenReturn(false);
        return new LoginPolicyImpl(userBuilder, userName, null, eventTransmitterBuilder);
    }

    @Test
    public void checkLoadedAndExpiredUser() {
        LoginPolicyImpl subj = createLoadedAndExpiredLoginUser();

        subj.loginUser();

        verify(user).updateLastRefreshDateTimeAndToken(isA(String.class));
    }

    private LoginPolicyImpl createLoadedAndExpiredLoginUser() {
        String userName = "checkLoginUserNotLoaded";
        when(userBuilder.createLoginUser(userName, null)).thenReturn(user);
        when(user.isUserLoaded()).thenReturn(true);
        when(user.isLastRefreshDateTimeBefore(isA(LocalDateTime.class))).thenReturn(true);
        return new LoginPolicyImpl(userBuilder, userName, null, eventTransmitterBuilder);
    }

    @Test
    public void checkLoadedAndNotExpiredUser() {
        LoginPolicyImpl subj = createLoadedAndNotExpiredLoginUser();

        subj.loginUser();

        verify(user).updateLastRefreshDateTime();
        verify(user).writeToken(isA(Consumer.class));
    }

    private LoginPolicyImpl createLoadedAndNotExpiredLoginUser() {
        String userName = "checkLoginUserNotLoaded";
        when(eventTransmitterBuilder.createLoginEventTransmitter(userName)).thenReturn(Mockito.mock(
                LogonEventTransmitter.class));
        when(userBuilder.createLoginUser(userName, null)).thenReturn(user);
        when(user.isUserLoaded()).thenReturn(true);
        when(user.isLastRefreshDateTimeBefore(isA(LocalDateTime.class))).thenReturn(false);
        return new LoginPolicyImpl(userBuilder, userName, null, eventTransmitterBuilder);
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
}
