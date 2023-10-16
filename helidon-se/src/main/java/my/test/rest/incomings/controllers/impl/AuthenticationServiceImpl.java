package my.test.rest.incomings.controllers.impl;

import io.helidon.common.http.Http.Status;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import my.test.authorization.rules.AuthFactory;
import my.test.authorization.rules.AuthenticateUserInteractor;
import my.test.authorization.rules.CreateUserInteractor;
import my.test.authorization.rules.ResponseFactory;
import my.test.rest.incomings.controllers.api.AuthenticationService;
import my.test.rest.incomings.controllers.api.dto.AuthInfo;


public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthFactory authFactory;
    private final ResponseFactory responseFactory;

    public AuthenticationServiceImpl(AuthFactory authFactory, ResponseFactory responseFactory) {
        this.authFactory = authFactory;
        this.responseFactory = responseFactory;
    }

    public void create(ServerRequest request, ServerResponse response, AuthInfo authInfo) {
        CreateUserInteractor createUserInteractor = authFactory
                .createNewUserInteractor(authInfo.getUserName(), authInfo.getPasswordHash(),
                        responseFactory.createCreationUserResponsePresenter(response));
        createUserInteractor.createNewUserAndGetPresenter().send();
    }

    public void login(ServerRequest request, ServerResponse response, AuthInfo authInfo) {
        AuthenticateUserInteractor authenticateUserInteractor = authFactory
                .createAuthenticateUserInteractor(authInfo.getUserName(), authInfo.getPasswordHash(),
                        responseFactory.createAuthenticationResponsePresenter(response));
        authenticateUserInteractor.authenticateAndGetPresenter().send();
    }

    public void logout(ServerRequest request, ServerResponse response) {
        response.status(Status.NOT_IMPLEMENTED_501).send();
    }

    public void refreshTokens(ServerRequest request, ServerResponse response) {
        response.status(Status.NOT_IMPLEMENTED_501).send();
    }
}
