package my.test.authorization.domain.impl;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.function.Consumer;
import my.test.authorization.domain.api.store.User;
import my.test.authorization.domain.api.store.UserBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static my.test.authorization.domain.api.LoginPolicy.GUEST;
import static my.test.authorization.domain.api.LoginPolicy.GUEST_PASSWORD_HASH;
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
        when(builder.createUser(GUEST, GUEST_PASSWORD_HASH)).thenReturn(user);
        new LoginPolicyImpl(builder, null, null);
        verify(builder).createUser(GUEST, GUEST_PASSWORD_HASH);
    }

    @Test
    public void createGuestEmptyNameCtorTest() {
        UserBuilder builder = Mockito.mock(UserBuilder.class);
        User user = Mockito.mock(User.class);
        when(builder.createUser(GUEST, GUEST_PASSWORD_HASH)).thenReturn(user);
        new LoginPolicyImpl(builder, "", null);
        verify(builder).createUser(GUEST, GUEST_PASSWORD_HASH);
    }

    @Test
    public void loginUserNotFound() {
        Random random = new Random();
        String userName = "username" + random.nextLong();
        String passwordHash = "passwordHash" + random.nextLong();
        UserBuilder builder = Mockito.mock(UserBuilder.class);
        User user = Mockito.mock(User.class);
        when(builder.createUser(userName, passwordHash)).thenReturn(user);
        when(user.isUserLoaded()).thenReturn(false);
        LoginPolicyImpl subj = new LoginPolicyImpl(builder, userName, passwordHash);
        subj.loginUser();
        verify(user).loadUser();
        verify(user).isUserLoaded();
        verify(user, times(0)).isExpirationDateTimeBefore(any());
        verify(user, times(0)).updateExpirationDateTimeAndToken(any());
    }

    @Test
    public void loginExpired() {
        Random random = new Random();
        String userName = "username" + random.nextLong();
        String passwordHash = "passwordHash" + random.nextLong();
        UserBuilder builder = Mockito.mock(UserBuilder.class);
        User user = Mockito.mock(User.class);
        when(builder.createUser(userName, passwordHash)).thenReturn(user);
        when(user.isUserLoaded()).thenReturn(true);
        when(user.isExpirationDateTimeBefore(isA(LocalDateTime.class))).thenReturn(true);
        LoginPolicyImpl subj = new LoginPolicyImpl(builder, userName, passwordHash);
        subj.loginUser();
        verify(user).loadUser();
        verify(user).isUserLoaded();
        verify(user).isExpirationDateTimeBefore(isA(LocalDateTime.class));
        verify(user).updateExpirationDateTimeAndToken(isA(String.class));
    }

    @Test
    public void login() {
        Random random = new Random();
        String userName = "username" + random.nextLong();
        String passwordHash = "passwordHash" + random.nextLong();
        UserBuilder builder = Mockito.mock(UserBuilder.class);
        User user = Mockito.mock(User.class);
        when(builder.createUser(userName, passwordHash)).thenReturn(user);
        when(user.isUserLoaded()).thenReturn(true);
        when(user.isExpirationDateTimeBefore(isA(LocalDateTime.class))).thenReturn(false);
        LoginPolicyImpl subj = new LoginPolicyImpl(builder, userName, passwordHash);
        subj.loginUser();
        verify(user).loadUser();
        verify(user).isUserLoaded();
        verify(user).isExpirationDateTimeBefore(isA(LocalDateTime.class));
        verify(user).updateExpirationDateTime();
    }

    @Test
    public void loginGuest() {
        UserBuilder builder = Mockito.mock(UserBuilder.class);
        User user = Mockito.mock(User.class);
        when(builder.createUser(GUEST, GUEST_PASSWORD_HASH)).thenReturn(user);
        when(user.isUserLoaded()).thenReturn(true);
        when(user.isExpirationDateTimeBefore(isA(LocalDateTime.class))).thenReturn(false);
        LoginPolicyImpl subj = new LoginPolicyImpl(builder, GUEST, GUEST_PASSWORD_HASH);
        subj.loginUser();
        verify(user).loadUser();
        verify(user).isUserLoaded();
        verify(user).isExpirationDateTimeBefore(isA(LocalDateTime.class));
        verify(user).updateExpirationDateTime();
    }

    @Test
    public void isLoginGuestSuccess() {
        UserBuilder builder = Mockito.mock(UserBuilder.class);
        User user = Mockito.mock(User.class);
        when(builder.createUser(GUEST, GUEST_PASSWORD_HASH)).thenReturn(user);
        LoginPolicyImpl subj = new LoginPolicyImpl(builder, null, null);
        subj.isLoginSuccess();
        verify(user).isUserLoaded();
    }

    @Test
    public void writeTokenAndExpirationDateTime() {
        class Internal {

            public void setToken(String token) {
            }

            public void setExpirationDateTime(LocalDateTime expirationDateTime) {
            }
        }
        UserBuilder builder = Mockito.mock(UserBuilder.class);
        User user = Mockito.mock(User.class);
        when(builder.createUser(GUEST, GUEST_PASSWORD_HASH)).thenReturn(user);
        LoginPolicyImpl subj = new LoginPolicyImpl(builder, null, null);
        Internal internal = new Internal();
        subj.writeTokenAndExpirationDateTime(internal::setToken, internal::setExpirationDateTime);
        verify(user).writeExpirationDateTime(any(Consumer.class));
        verify(user).writeToken(any(Consumer.class));
    }
}
