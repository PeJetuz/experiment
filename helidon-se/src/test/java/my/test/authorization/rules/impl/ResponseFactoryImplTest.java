package my.test.authorization.rules.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ResponseFactoryImplTest {

    @Test
    public void createResponsesTest() {
        ResponseFactoryImpl subj = new ResponseFactoryImpl(null);
        assertNotNull(subj.createAuthenticationResponsePresenter(null));
        assertNotNull(subj.createCreationUserResponsePresenter(null));
    }
}
