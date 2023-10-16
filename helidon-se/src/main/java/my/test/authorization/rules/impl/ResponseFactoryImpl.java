package my.test.authorization.rules.impl;

import io.helidon.webserver.ServerResponse;
import jakarta.json.bind.Jsonb;
import my.test.authorization.rules.AuthenticationResponsePresenter;
import my.test.authorization.rules.CreationUserResponsePresenter;
import my.test.authorization.rules.ResponseFactory;
import my.test.rest.incomings.controllers.presenters.authentication.AuthenticationUserResponseModelImpl;
import my.test.rest.incomings.controllers.presenters.creation.CreationUserResponseModelImpl;

public class ResponseFactoryImpl implements ResponseFactory {
    private final Jsonb jsonb;

    public ResponseFactoryImpl(Jsonb jsonb) {
        this.jsonb = jsonb;
    }

    @Override
    public AuthenticationResponsePresenter createAuthenticationResponsePresenter(ServerResponse response) {
        return new AuthenticationUserResponseModelImpl(response, jsonb);
    }

    @Override
    public CreationUserResponsePresenter createCreationUserResponsePresenter(ServerResponse response) {
        return new CreationUserResponseModelImpl(response, jsonb);
    }
}
