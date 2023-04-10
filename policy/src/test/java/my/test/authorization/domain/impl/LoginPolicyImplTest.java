package my.test.authorization.domain.impl;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import my.test.authorization.domain.api.servicebus.LoginEventTransmitter;
import my.test.authorization.domain.api.servicebus.LoginEventTransmitterBuilder;
import my.test.authorization.domain.api.store.User;
import my.test.authorization.domain.api.store.UserBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static my.test.authorization.domain.api.store.User.GUEST;
import static my.test.authorization.domain.api.store.User.GUEST_PASSWORD_HASH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginPolicyImplTest {

    @Test
    public void createGuestNullNameCtorTest() {
        UserBuilder builder = Mockito.mock(UserBuilder.class);
        User user = Mockito.mock(User.class);
        LoginEventTransmitterBuilder loginEventTransmitterBuilder = Mockito.mock(LoginEventTransmitterBuilder.class);
        when(builder.createUser(GUEST, GUEST_PASSWORD_HASH)).thenReturn(user);
        new LoginPolicyImpl(builder, null, null, loginEventTransmitterBuilder);
        verify(builder).createUser(GUEST, GUEST_PASSWORD_HASH);
    }

    @Test
    public void createGuestEmptyNameCtorTest() {
        UserBuilder builder = Mockito.mock(UserBuilder.class);
        User user = Mockito.mock(User.class);
        LoginEventTransmitterBuilder loginEventTransmitterBuilder = Mockito.mock(LoginEventTransmitterBuilder.class);
        when(builder.createUser(GUEST, GUEST_PASSWORD_HASH)).thenReturn(user);
        new LoginPolicyImpl(builder, "", null, loginEventTransmitterBuilder);
        verify(builder).createUser(GUEST, GUEST_PASSWORD_HASH);
    }

    @Test
    public void loginUserNotFound() {
        Random random = ThreadLocalRandom.current();
        String userName = "username" + random.nextLong();
        String passwordHash = "passwordHash" + random.nextLong();
        UserBuilder builder = Mockito.mock(UserBuilder.class);
        User user = Mockito.mock(User.class);
        LoginEventTransmitterBuilder loginEventTransmitterBuilder = Mockito.mock(LoginEventTransmitterBuilder.class);
        when(builder.createUser(userName, passwordHash)).thenReturn(user);
        when(user.isUserLoaded()).thenReturn(false);
        LoginPolicyImpl subj = new LoginPolicyImpl(builder, userName, passwordHash, loginEventTransmitterBuilder);
        subj.loginUser();
        verify(user).loadUser();
        verify(user).isUserLoaded();
        verify(user, times(0)).isLastRefreshDateTime(any());
        verify(user, times(0)).updateLastRefreshDateTimeAndToken(any());
    }

    @Test
    public void loginExpired() {
        Random random = ThreadLocalRandom.current();
        String userName = "username" + random.nextLong();
        String passwordHash = "passwordHash" + random.nextLong();
        UserBuilder builder = Mockito.mock(UserBuilder.class);
        User user = Mockito.mock(User.class);
        LoginEventTransmitterBuilder loginEventTransmitterBuilder = Mockito.mock(LoginEventTransmitterBuilder.class);
        when(builder.createUser(userName, passwordHash)).thenReturn(user);
        when(user.isUserLoaded()).thenReturn(true);
        when(user.isLastRefreshDateTime(isA(LocalDateTime.class))).thenReturn(true);
        LoginPolicyImpl subj = new LoginPolicyImpl(builder, userName, passwordHash, loginEventTransmitterBuilder);
        subj.loginUser();
        verify(user).loadUser();
        verify(user).isUserLoaded();
        verify(user).isLastRefreshDateTime(isA(LocalDateTime.class));
        verify(user).updateLastRefreshDateTimeAndToken(isA(String.class));
    }

    @Test
    public void login() {
        Random random = ThreadLocalRandom.current();
        String userName = "username" + random.nextLong();
        String passwordHash = "passwordHash" + random.nextLong();
        UserBuilder builder = Mockito.mock(UserBuilder.class);
        User user = Mockito.mock(User.class);
        LoginEventTransmitterBuilder loginEventTransmitterBuilder = Mockito.mock(LoginEventTransmitterBuilder.class);
        LoginEventTransmitter loginEventTransmitter = Mockito.mock(LoginEventTransmitter.class);
        when(loginEventTransmitterBuilder.createLoginEventTransmitter(userName)).thenReturn(loginEventTransmitter);
        when(builder.createUser(userName, passwordHash)).thenReturn(user);
        when(user.isUserLoaded()).thenReturn(true);
        when(user.isLastRefreshDateTime(isA(LocalDateTime.class))).thenReturn(false);
        LoginPolicyImpl subj = new LoginPolicyImpl(builder, userName, passwordHash, loginEventTransmitterBuilder);
        subj.loginUser();
        verify(user).loadUser();
        verify(user).isUserLoaded();
        verify(user).isLastRefreshDateTime(isA(LocalDateTime.class));
        verify(user).updateLastRefreshDateTime();
        verify(loginEventTransmitter).sendUserLoginEvent();
    }

    @Test
    public void loginGuest() {
        UserBuilder builder = Mockito.mock(UserBuilder.class);
        User user = Mockito.mock(User.class);
        LoginEventTransmitterBuilder loginEventTransmitterBuilder = Mockito.mock(LoginEventTransmitterBuilder.class);
        LoginEventTransmitter loginEventTransmitter = Mockito.mock(LoginEventTransmitter.class);
        when(loginEventTransmitterBuilder.createLoginEventTransmitter(GUEST)).thenReturn(loginEventTransmitter);
        when(builder.createUser(GUEST, GUEST_PASSWORD_HASH)).thenReturn(user);
        when(user.isUserLoaded()).thenReturn(true);
        when(user.isLastRefreshDateTime(isA(LocalDateTime.class))).thenReturn(false);
        LoginPolicyImpl subj = new LoginPolicyImpl(builder, GUEST, GUEST_PASSWORD_HASH, loginEventTransmitterBuilder);
        subj.loginUser();
        verify(user).loadUser();
        verify(user).isUserLoaded();
        verify(user).isLastRefreshDateTime(isA(LocalDateTime.class));
        verify(user).updateLastRefreshDateTime();
        verify(loginEventTransmitter).sendUserLoginEvent();
    }

    @Test
    public void isLoginGuestSuccess() {
        UserBuilder builder = Mockito.mock(UserBuilder.class);
        User user = Mockito.mock(User.class);
        LoginEventTransmitterBuilder loginEventTransmitterBuilder = Mockito.mock(LoginEventTransmitterBuilder.class);
        when(builder.createUser(GUEST, GUEST_PASSWORD_HASH)).thenReturn(user);
        LoginPolicyImpl subj = new LoginPolicyImpl(builder, null, null, loginEventTransmitterBuilder);
        subj.isLoginSuccess();
        verify(user).isUserLoaded();
    }

    @Test
    public void writeTokenAndLastRefreshDateTime() {
        class Internal {

            public void setToken(String token) {
            }

            public void setLastRefreshDateTime(LocalDateTime lastRefreshDateTime) {
            }
        }
        UserBuilder builder = Mockito.mock(UserBuilder.class);
        User user = Mockito.mock(User.class);
        LoginEventTransmitterBuilder loginEventTransmitterBuilder = Mockito.mock(LoginEventTransmitterBuilder.class);
        when(builder.createUser(GUEST, GUEST_PASSWORD_HASH)).thenReturn(user);
        LoginPolicyImpl subj = new LoginPolicyImpl(builder, null, null, loginEventTransmitterBuilder);
        Internal internal = new Internal();
        subj.writeTokenAndLastRefreshDateTime(internal::setToken, internal::setLastRefreshDateTime);
        verify(user).writeLastRefreshDateTime(any(Consumer.class));
        verify(user).writeToken(any(Consumer.class));
    }
}
