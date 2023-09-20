package my.test.authorization.rules.impl;

import jakarta.enterprise.context.ApplicationScoped;
import my.test.authorization.rules.AuthenticationResponsePresenter;
import my.test.authorization.rules.CreationUserResponsePresenter;
import my.test.authorization.rules.ResponseFactory;
import my.test.rest.incomings.controllers.presenters.authentication.AuthenticationUserResponseModelImpl;
import my.test.rest.incomings.controllers.presenters.creation.CreationUserResponseModelImpl;

@ApplicationScoped
public class ResponseFactoryImpl implements ResponseFactory {

    @Override
    public AuthenticationResponsePresenter createAuthenticationResponsePresenter() {
        return new AuthenticationUserResponseModelImpl();
    }

    @Override
    public CreationUserResponsePresenter createCreationUserResponsePresenter() {
        return new CreationUserResponseModelImpl();
    }
}
