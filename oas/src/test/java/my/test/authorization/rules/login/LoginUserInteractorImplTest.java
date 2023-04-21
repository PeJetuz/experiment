package my.test.authorization.rules.login;

import java.util.function.Consumer;
import my.test.authorization.domain.api.LoginPolicy;
import my.test.authorization.domain.api.PolicyBuilder;
import my.test.authorization.rules.LoginUserPresenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginUserInteractorImplTest {

    private PolicyBuilder policyBuilder = Mockito.mock(PolicyBuilder.class);
    private LoginUserPresenter presenter = Mockito.mock(LoginUserPresenter.class);
    private LoginPolicy policy = Mockito.mock(LoginPolicy.class);

    @BeforeEach
    public void beforeEachTestInit() {
        when(policyBuilder.buildLoginPolicy(null, null)).thenReturn(policy);
    }

    @Test
    public void checkLoginSuccessfully() {
        LoginUserInteractorImpl subj = buildInteractorForLoginSuccessfully();

        subj.login();

        verify(policy).loginUser();
        verify(policy).isLoginSuccess();
        verify(policy).writeTokenAndLastRefreshDateTime(isA(Consumer.class), isA(Consumer.class));
    }

    private LoginUserInteractorImpl buildInteractorForLoginSuccessfully() {
        when(policy.isLoginSuccess()).thenReturn(true);
        return new LoginUserInteractorImpl(policyBuilder, presenter, null, null);
    }

    @Test
    public void loginLoginFailed() {
        LoginUserInteractorImpl subj = buildInteractorForLoginFailed();

        subj.login();

        verify(policy).loginUser();
        verify(policy).isLoginSuccess();
        verify(presenter).initUserNotFoundResponseModel();
    }

    private LoginUserInteractorImpl buildInteractorForLoginFailed() {
        when(policy.isLoginSuccess()).thenReturn(false);
        return new LoginUserInteractorImpl(policyBuilder, presenter, null, null);
    }
}
