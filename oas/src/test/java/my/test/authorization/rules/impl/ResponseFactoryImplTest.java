package my.test.authorization.rules.impl;

import my.test.authorization.rules.ResponseFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ResponseFactoryImplTest {

    private ResponseFactoryImpl subj = new ResponseFactoryImpl();

    @Test
    public void createAuthenticationResponsePresenter() {
        assertNotNull(subj.createAuthenticationResponsePresenter());
    }

    @Test
    public void createCreationUserResponsePresenter() {
        assertNotNull(subj.createCreationUserResponsePresenter());
    }

    @Test
    public void fake() {
        ResponseFactory subj = new ResponseFactory.Fake();
        assertNotNull(subj.createAuthenticationResponsePresenter());
        assertNotNull(subj.createAuthenticationResponsePresenter());
    }
}
