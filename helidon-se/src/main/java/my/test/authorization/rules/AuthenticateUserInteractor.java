package my.test.authorization.rules;

import my.test.rest.incomings.controllers.AuthenticationResponseModel;

public interface AuthenticateUserInteractor {

    AuthenticationResponseModel authenticateAndGetPresenter();

    record Fake(AuthenticationResponseModel authenticateAndGetPresenter) implements AuthenticateUserInteractor {

    }
}
