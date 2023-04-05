package my.test.eureka.policy.impl;

import my.test.eureka.policy.CreatePolicy;
import my.test.eureka.policy.LoginPolicy;
import my.test.eureka.policy.store.User;
import my.test.eureka.policy.store.UserBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class PolicyBuilderImplTest {

    @Test
    public void buildLoginPolicyTest() {
        UserBuilder userBuilder = Mockito.mock(UserBuilder.class);
        when(userBuilder.createUser(null, null)).thenReturn(Mockito.mock(User.class));
        PolicyBuilderImpl builder = new PolicyBuilderImpl(userBuilder);
        LoginPolicy loginPolicy = builder.buildLoginPolicy(null, null);
        assertNotNull(loginPolicy);
        assertTrue(loginPolicy instanceof LoginPolicyImpl);
    }

    @Test
    public void buildCreatePolicyTest() {
        UserBuilder userBuilder = Mockito.mock(UserBuilder.class);
        when(userBuilder.createUser(null, null)).thenReturn(Mockito.mock(User.class));
        PolicyBuilderImpl builder = new PolicyBuilderImpl(userBuilder);
        CreatePolicy createPolicy = builder.buildCreatePolicy(null, null);
        assertNotNull(createPolicy);
        assertTrue(createPolicy instanceof CreatePolicy);
    }
}
