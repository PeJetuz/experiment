package my.test.authorization.domain.impl;

import my.test.authorization.domain.api.store.NewUser;
import my.test.authorization.domain.api.store.UserBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreatePolicyImplTest {

    private UserBuilder userBuilder = Mockito.mock(UserBuilder.class);
    private NewUser user = Mockito.mock(NewUser.class);

    @BeforeEach
    public void beforeEachTestInit() {
        when(userBuilder.createNewUser(null, null)).thenReturn(user);
    }

    @Test
    public void creatingAnExistingUser() {
        when(user.isUserExists()).thenReturn(true);
        CreatePolicyImpl subj = new CreatePolicyImpl(userBuilder, null, null);

        subj.createNewUser();

        verify(user, times(0)).createNewUser(isA(String.class));
    }

    @Test
    public void creatingNewUser() {
        when(user.isUserExists()).thenReturn(false);
        CreatePolicyImpl subj = new CreatePolicyImpl(userBuilder, null, null);

        subj.createNewUser();

        verify(user).createNewUser(isA(String.class));
    }

    @Test
    public void checkNewUserCreatedSuccessfully() {
        when(user.isNewUserCreatedSuccessfully()).thenReturn(true);
        CreatePolicyImpl subj = new CreatePolicyImpl(userBuilder, null, null);

        assertTrue(subj.isNewUserCreatedSuccessfully());
    }

    @Test
    public void checkWriteTokenAndLastRefreshDateTime() {
        CreatePolicyImpl policy = new CreatePolicyImpl(userBuilder, null, null);
        policy.writeTokenAndLastRefreshDateTime(null, null);

        verify(user).writeToken(null);
        verify(user).writeLastRefreshDateTime(null);
    }
}
