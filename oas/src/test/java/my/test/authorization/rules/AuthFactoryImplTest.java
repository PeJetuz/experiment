package my.test.authorization.rules;

import my.test.authorization.domain.api.api.PolicyBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuthFactoryImplTest {

    @Test
    public void createLoginProcess() {
        PolicyBuilder policyBuilder = Mockito.mock(PolicyBuilder.class);
        AuthFactoryImpl subj = new AuthFactoryImpl(policyBuilder);
        LoginProcess loginProcess = subj.createLoginProcess(null, null, null);
        assertNotNull(loginProcess);
    }

    @Test
    public void createNewUserProcess() {
        PolicyBuilder policyBuilder = Mockito.mock(PolicyBuilder.class);
        AuthFactoryImpl subj = new AuthFactoryImpl(policyBuilder);
        CreateProcess createProcess = subj.createNewUserProcess(null, null, null);
        assertNotNull(createProcess);
    }
}
