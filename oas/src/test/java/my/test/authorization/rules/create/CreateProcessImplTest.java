package my.test.authorization.rules.create;

import java.util.Random;
import java.util.function.Consumer;
import my.test.authorization.domain.api.api.CreatePolicy;
import my.test.authorization.domain.api.api.PolicyBuilder;
import my.test.authorization.rules.CreatePresenter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreateProcessImplTest {

    @Test
    public void validateUserNameNull() {
        Random random = new Random();
        String passwordHash = "passwordHash" + random.nextLong();
        PolicyBuilder policyBuilder = Mockito.mock(PolicyBuilder.class);
        CreatePolicy policy = Mockito.mock(CreatePolicy.class);
        CreatePresenter presenter = Mockito.mock(CreatePresenter.class);
        when(policyBuilder.buildCreatePolicy(null, passwordHash)).thenReturn(policy);
        CreateProcessImpl subj = new CreateProcessImpl(policyBuilder, presenter, null, passwordHash);
        assertFalse(subj.isUserDataValid());
        verify(presenter).invalidUserNameField();
    }

    @Test
    public void validateUserNameEmpty() {
        Random random = new Random();
        String passwordHash = "passwordHash" + random.nextLong();
        PolicyBuilder policyBuilder = Mockito.mock(PolicyBuilder.class);
        CreatePolicy policy = Mockito.mock(CreatePolicy.class);
        CreatePresenter presenter = Mockito.mock(CreatePresenter.class);
        when(policyBuilder.buildCreatePolicy("", passwordHash)).thenReturn(policy);
        CreateProcessImpl subj = new CreateProcessImpl(policyBuilder, presenter, "", passwordHash);
        assertFalse(subj.isUserDataValid());
        verify(presenter).invalidUserNameField();
    }

    @Test
    public void validateUserPasswordNull() {
        Random random = new Random();
        String userName = "userName" + random.nextLong();
        PolicyBuilder policyBuilder = Mockito.mock(PolicyBuilder.class);
        CreatePolicy policy = Mockito.mock(CreatePolicy.class);
        CreatePresenter presenter = Mockito.mock(CreatePresenter.class);
        when(policyBuilder.buildCreatePolicy(userName, null)).thenReturn(policy);
        CreateProcessImpl subj = new CreateProcessImpl(policyBuilder, presenter, userName, null);
        assertFalse(subj.isUserDataValid());
        verify(presenter).invalidPasswordHashField();
    }

    @Test
    public void validateUserPasswordEmpty() {
        Random random = new Random();
        String userName = "userName" + random.nextLong();
        PolicyBuilder policyBuilder = Mockito.mock(PolicyBuilder.class);
        CreatePolicy policy = Mockito.mock(CreatePolicy.class);
        CreatePresenter presenter = Mockito.mock(CreatePresenter.class);
        when(policyBuilder.buildCreatePolicy(userName, "")).thenReturn(policy);
        CreateProcessImpl subj = new CreateProcessImpl(policyBuilder, presenter, userName, "");
        assertFalse(subj.isUserDataValid());
        verify(presenter).invalidPasswordHashField();
    }

    @Test
    public void createUserValidationFailed() {
        PolicyBuilder policyBuilder = Mockito.mock(PolicyBuilder.class);
        CreatePolicy policy = Mockito.mock(CreatePolicy.class);
        CreatePresenter presenter = Mockito.mock(CreatePresenter.class);
        when(policyBuilder.buildCreatePolicy(null, null)).thenReturn(policy);
        when(policy.createNewUser()).thenReturn(false);
        CreateProcessImpl subj = new CreateProcessImpl(policyBuilder, presenter, null, null);
        subj.createNewUser();
        verify(policy, times(0)).createNewUser();
        verify(policy, times(0)).writeTokenAndExpirationDateTime(any(), any());
        verify(presenter, times(0)).initUserAlreadyExistsResponseModel();
    }

    @Test
    public void createUserFailed() {
        Random random = new Random();
        String userName = "userName" + random.nextLong();
        String passwordHash = "passwordHash" + random.nextLong();
        PolicyBuilder policyBuilder = Mockito.mock(PolicyBuilder.class);
        CreatePolicy policy = Mockito.mock(CreatePolicy.class);
        CreatePresenter presenter = Mockito.mock(CreatePresenter.class);
        when(policyBuilder.buildCreatePolicy(userName, passwordHash)).thenReturn(policy);
        when(policy.createNewUser()).thenReturn(false);
        CreateProcessImpl subj = new CreateProcessImpl(policyBuilder, presenter, userName, passwordHash);
        subj.createNewUser();
        verify(policy, times(0)).writeTokenAndExpirationDateTime(any(), any());
        verify(presenter).initUserAlreadyExistsResponseModel();
    }

    @Test
    public void createUserSuccess() {
        Random random = new Random();
        String userName = "userName" + random.nextLong();
        String passwordHash = "passwordHash" + random.nextLong();
        PolicyBuilder policyBuilder = Mockito.mock(PolicyBuilder.class);
        CreatePolicy policy = Mockito.mock(CreatePolicy.class);
        CreatePresenter presenter = Mockito.mock(CreatePresenter.class);
        when(policyBuilder.buildCreatePolicy(userName, passwordHash)).thenReturn(policy);
        when(policy.createNewUser()).thenReturn(true);
        CreateProcessImpl subj = new CreateProcessImpl(policyBuilder, presenter, userName, passwordHash);
        subj.createNewUser();
        verify(presenter, times(0)).initUserAlreadyExistsResponseModel();
        verify(policy).writeTokenAndExpirationDateTime(isA(Consumer.class), isA(Consumer.class));
    }
}
