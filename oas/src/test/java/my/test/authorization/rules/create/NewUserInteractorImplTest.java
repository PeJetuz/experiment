package my.test.authorization.rules.create;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import my.test.authorization.domain.api.CreatePolicy;
import my.test.authorization.domain.api.PolicyBuilder;
import my.test.authorization.rules.NewUserPresenter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NewUserInteractorImplTest {

    private Random random = ThreadLocalRandom.current();
    private PolicyBuilder policyBuilder = Mockito.mock(PolicyBuilder.class);
    private CreatePolicy policy = Mockito.mock(CreatePolicy.class);
    private NewUserPresenter presenter = Mockito.mock(NewUserPresenter.class);

    @Test
    public void checkUserNameNull() {
        NewUserInteractorImpl subj = buildInteractorWithUsernameNull();

        assertFalse(subj.isUserDataValid());
        verify(presenter).invalidUserNameField();
    }

    private NewUserInteractorImpl buildInteractorWithUsernameNull() {
        String passwordHash = "passwordHash" + random.nextLong();
        when(policyBuilder.buildCreatePolicy(null, passwordHash)).thenReturn(policy);
        return new NewUserInteractorImpl(policyBuilder, presenter, null, passwordHash);
    }

    @Test
    public void checkUserNameEmpty() {
        NewUserInteractorImpl subj = buildInteractorWithEmptyUsername();

        assertFalse(subj.isUserDataValid());
        verify(presenter).invalidUserNameField();
    }

    private NewUserInteractorImpl buildInteractorWithEmptyUsername() {
        String passwordHash = "passwordHash" + random.nextLong();
        when(policyBuilder.buildCreatePolicy("", passwordHash)).thenReturn(policy);
        return new NewUserInteractorImpl(policyBuilder, presenter, "", passwordHash);
    }

    @Test
    public void checkUserPasswordNull() {
        NewUserInteractorImpl subj = buildInteractorWithNullPassword();

        assertFalse(subj.isUserDataValid());
        verify(presenter).invalidPasswordHashField();
    }

    private NewUserInteractorImpl buildInteractorWithNullPassword() {
        String userName = "userName" + random.nextLong();
        when(policyBuilder.buildCreatePolicy(userName, null)).thenReturn(policy);
        return new NewUserInteractorImpl(policyBuilder, presenter, userName, null);
    }

    @Test
    public void checkUserPasswordEmpty() {
        NewUserInteractorImpl subj = buildInteractorWithEmptyPassword();

        assertFalse(subj.isUserDataValid());
        verify(presenter).invalidPasswordHashField();
    }

    private NewUserInteractorImpl buildInteractorWithEmptyPassword() {
        String userName = "userName" + random.nextLong();
        when(policyBuilder.buildCreatePolicy(userName, "")).thenReturn(policy);
        return new NewUserInteractorImpl(policyBuilder, presenter, userName, "");
    }

    @Test
    public void creatingUserWithNullUsernameAndPasswordHash() {
        NewUserInteractorImpl subj = buildInteractorWithNullUsernameAndPassword();

        subj.createNewUser();

        verify(policy, times(0)).createNewUser();
        verify(policy, times(0)).writeTokenAndLastRefreshDateTime(any(), any());
        verify(presenter, times(0)).initUserAlreadyExistsResponseModel();
    }

    private NewUserInteractorImpl buildInteractorWithNullUsernameAndPassword() {
        when(policyBuilder.buildCreatePolicy(null, null)).thenReturn(policy);
        when(policy.isNewUserCreatedSuccessfully()).thenReturn(false);
        return new NewUserInteractorImpl(policyBuilder, presenter, null, null);
    }

    @Test
    public void checkUnsuccessfulUserCreation() {
        NewUserInteractorImpl subj = buildPresenterWithUnsuccessfulUserCreation();

        subj.createNewUser();

        verify(policy, times(0)).writeTokenAndLastRefreshDateTime(any(), any());
        verify(presenter).initUserAlreadyExistsResponseModel();
    }

    private NewUserInteractorImpl buildPresenterWithUnsuccessfulUserCreation() {
        String userName = "userName" + random.nextLong();
        String passwordHash = "passwordHash" + random.nextLong();
        when(policyBuilder.buildCreatePolicy(userName, passwordHash)).thenReturn(policy);
        when(policy.isNewUserCreatedSuccessfully()).thenReturn(false);
        return new NewUserInteractorImpl(policyBuilder, presenter, userName, passwordHash);
    }

    @Test
    public void checkOfSuccessfulUserCreation() {
        NewUserInteractorImpl subj = buildInteractorForSuccessfulUserCreation();

        subj.createNewUser();

        verify(presenter, times(0)).initUserAlreadyExistsResponseModel();
        verify(policy).writeTokenAndLastRefreshDateTime(isA(Consumer.class), isA(Consumer.class));
    }

    private NewUserInteractorImpl buildInteractorForSuccessfulUserCreation() {
        String userName = "userName" + random.nextLong();
        String passwordHash = "passwordHash" + random.nextLong();
        when(policyBuilder.buildCreatePolicy(userName, passwordHash)).thenReturn(policy);
        when(policy.isNewUserCreatedSuccessfully()).thenReturn(true);
        return new NewUserInteractorImpl(policyBuilder, presenter, userName, passwordHash);
    }
}
