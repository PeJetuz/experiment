package my.test.authorization.domain.impl;

import my.test.authorization.domain.api.AuthenticationPolicy;
import my.test.authorization.domain.api.CreationPolicy;
import my.test.authorization.domain.api.PolicyFactory;
import my.test.authorization.domain.api.UserInfo;
import my.test.authorization.domain.api.servicebus.LogonEventTransmitter;
import my.test.authorization.domain.api.servicebus.LogonEventTransmitterBuilder;
import my.test.authorization.domain.api.store.UserBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class PolicyFactoryImplTest {

    @Test
    public void fake() {
        AuthenticationPolicy authenticationPolicy = new AuthenticationPolicy.Fake();
        CreationPolicy creationPolicy = new CreationPolicy.Fake();
        PolicyFactory.Fake subj = new PolicyFactory.Fake(authenticationPolicy, creationPolicy);
        assertEquals(authenticationPolicy, subj.buildAuthenticatePolicy(null, null));
        assertEquals(creationPolicy, subj.buildCreationPolicy(null, null));
    }

    @Test
    public void buildAuthenticatePolicy() {
        PolicyFactoryImpl builder = new PolicyFactoryImpl(new UserBuilder.Fake(null, null),
                new LogonEventTransmitterBuilder.Fake(new LogonEventTransmitter.Fake()));
        AuthenticationPolicy authenticationPolicy = builder.buildAuthenticatePolicy(createUserInfo(), null);

        assertNotNull(authenticationPolicy);
    }

    @Test
    public void CreationPolicy() {
        PolicyFactoryImpl builder = new PolicyFactoryImpl(new UserBuilder.Fake(null, null), null);
        CreationPolicy authenticationPolicy = builder.buildCreationPolicy(createUserInfo(), null);

        assertNotNull(authenticationPolicy);
    }

    private UserInfo createUserInfo() {
        return new UserInfo(null, null);
    }
}
