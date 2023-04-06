package my.test.authorization.rules.login;

import java.util.Random;
import java.util.function.Consumer;
import my.test.authorization.domain.api.api.LoginPolicy;
import my.test.authorization.domain.api.api.PolicyBuilder;
import my.test.authorization.rules.LoginPresenter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginProcessImplTest {

    @Test
    public void loginLoginSuccess() {
        Random random = new Random();
        String userName = "username" + random.nextLong();
        String passwordHash = "passwordHash" + random.nextLong();
        PolicyBuilder policyBuilder = Mockito.mock(PolicyBuilder.class);
        LoginPresenter presenter = Mockito.mock(LoginPresenter.class);
        LoginPolicy policy = Mockito.mock(LoginPolicy.class);
        when(policyBuilder.buildLoginPolicy(userName, passwordHash)).thenReturn(policy);
        when(policy.isLoginSuccess()).thenReturn(true);
        LoginProcessImpl subj = new LoginProcessImpl(policyBuilder, presenter, userName, passwordHash);
        subj.login();
        verify(policy).loginUser();
        verify(policy).isLoginSuccess();
        verify(policy).writeTokenAndExpirationDateTime(isA(Consumer.class), isA(Consumer.class));
    }

    @Test
    public void loginLoginFailed() {
        Random random = new Random();
        String userName = "username" + random.nextLong();
        String passwordHash = "passwordHash" + random.nextLong();
        PolicyBuilder policyBuilder = Mockito.mock(PolicyBuilder.class);
        LoginPresenter presenter = Mockito.mock(LoginPresenter.class);
        LoginPolicy policy = Mockito.mock(LoginPolicy.class);
        when(policyBuilder.buildLoginPolicy(userName, passwordHash)).thenReturn(policy);
        when(policy.isLoginSuccess()).thenReturn(false);
        LoginProcessImpl subj = new LoginProcessImpl(policyBuilder, presenter, userName, passwordHash);
        subj.login();
        verify(policy).loginUser();
        verify(policy).isLoginSuccess();
        verify(presenter).initUserNotFoundResponseModel();
    }
}
