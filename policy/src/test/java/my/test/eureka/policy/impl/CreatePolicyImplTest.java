package my.test.eureka.policy.impl;

import my.test.eureka.policy.store.User;
import my.test.eureka.policy.store.UserBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreatePolicyImplTest {

    @Test
    public void createNewUserFalse() {
        UserBuilder userBuilder = Mockito.mock(UserBuilder.class);
        User user = Mockito.mock(User.class);
        when(userBuilder.createUser(null, null)).thenReturn(user);
        when(user.isUserExists()).thenReturn(true);
        CreatePolicyImpl policy = new CreatePolicyImpl(userBuilder, null, null);
        assertFalse(policy.createNewUser());
        verify(user, times(0)).createNewUser(any());
    }

    @Test
    public void createNewUserSuccess() {
        UserBuilder userBuilder = Mockito.mock(UserBuilder.class);
        User user = Mockito.mock(User.class);
        when(userBuilder.createUser(null, null)).thenReturn(user);
        when(user.isUserExists()).thenReturn(false);
        ArgumentCaptor<String> tokenCaptor = ArgumentCaptor.forClass(String.class);
        when(user.createNewUser(tokenCaptor.capture())).thenReturn(true);
        CreatePolicyImpl policy = new CreatePolicyImpl(userBuilder, null, null);
        assertTrue(policy.createNewUser());
        assertEquals("token", tokenCaptor.getValue());
    }

    @Test
    public void writeTokenAndExpirationDateTime() {
        UserBuilder userBuilder = Mockito.mock(UserBuilder.class);
        User user = Mockito.mock(User.class);
        when(userBuilder.createUser(null, null)).thenReturn(user);
        CreatePolicyImpl policy = new CreatePolicyImpl(userBuilder, null, null);
        policy.writeTokenAndExpirationDateTime(null, null);
        verify(user).writeToken(null);
        verify(user).writeExpirationDateTime(null);
    }
}
