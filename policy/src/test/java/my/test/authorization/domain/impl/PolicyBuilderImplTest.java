package my.test.authorization.domain.impl;

import my.test.authorization.domain.api.servicebus.LogonEventTransmitterBuilder;
import my.test.authorization.domain.api.store.LoginUser;
import my.test.authorization.domain.api.CreatePolicy;
import my.test.authorization.domain.api.LoginPolicy;
import my.test.authorization.domain.api.store.NewUser;
import my.test.authorization.domain.api.store.UserBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class PolicyBuilderImplTest {

    private UserBuilder userBuilder = Mockito.mock(UserBuilder.class);
    private LogonEventTransmitterBuilder logonEventTransmitterBuilder = Mockito.mock(
            LogonEventTransmitterBuilder.class);

    @Test
    public void checkCreatingLoginPolicyInstance() {
        LoginPolicy loginPolicy = buildLoginPolicy();

        assertTrue(loginPolicy instanceof LoginPolicyImpl);
    }

    private LoginPolicy buildLoginPolicy() {
        when(userBuilder.createLoginUser(null, null)).thenReturn(Mockito.mock(LoginUser.class));
        PolicyBuilderImpl builder = new PolicyBuilderImpl(userBuilder, logonEventTransmitterBuilder);
        return builder.buildLoginPolicy(null, null);
    }

    @Test
    public void checkCreatingCreatePolicyInstance() {
        CreatePolicy createPolicy = buildCreatePolicy();

        assertTrue(createPolicy instanceof CreatePolicy);
    }

    private CreatePolicy buildCreatePolicy() {
        when(userBuilder.createNewUser(null, null)).thenReturn(Mockito.mock(NewUser.class));
        PolicyBuilderImpl builder = new PolicyBuilderImpl(userBuilder, logonEventTransmitterBuilder);
        return builder.buildCreatePolicy(null, null);
    }
}
