package my.test.authorization.rules;

import my.test.authorization.domain.api.PolicyBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuthFactoryImplTest {

    private PolicyBuilder policyBuilder = Mockito.mock(PolicyBuilder.class);
    private AuthFactoryImpl subj = new AuthFactoryImpl(policyBuilder);

    @Test
    public void createLoginUserInteractor() {
        LoginUserInteractor loginUserInteractor = subj.createLoginUserInteractor(null, null, null);

        assertNotNull(loginUserInteractor);
    }

    @Test
    public void createNewUserProcess() {
        NewUserInteractor newUserInteractor = subj.createNewUserProcess(null, null, null);

        assertNotNull(newUserInteractor);
    }
}
