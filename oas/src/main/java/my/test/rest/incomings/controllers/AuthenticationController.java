package my.test.rest.incomings.controllers;

import my.test.authorization.rules.AuthenticateUserInteractor;
import my.test.authorization.rules.ResponseFactory;
import my.test.rest.incomings.controllers.api.AuthenticationApi;
import my.test.rest.incomings.controllers.api.dto.AuthInfo;
import my.test.rest.incomings.controllers.api.dto.Authentication;
import my.test.rest.incomings.controllers.api.dto.TokenPair;
import my.test.authorization.rules.AuthFactory;
import my.test.authorization.rules.CreateUserInteractor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/api")
public class AuthenticationController implements AuthenticationApi {

    private final AuthFactory authFactory;
    private final ResponseFactory responseFactory;

    public AuthenticationController(AuthFactory authFactory, ResponseFactory responseFactory) {
        this.authFactory = authFactory;
        this.responseFactory = responseFactory;
    }

    @Override
    public ResponseEntity<Authentication> login(AuthInfo authInfo) {
        AuthenticateUserInteractor authInteractor = authFactory.createAuthenticateUserInteractor(
                authInfo.getUserName(), authInfo.getPasswordHash(),
                responseFactory.createAuthenticationResponsePresenter());
        return authInteractor.authenticateAndGetPresenter().renderModel();
    }

    @Override
    public ResponseEntity<Authentication> create(AuthInfo authInfo) {
        CreateUserInteractor createUserInteractor = authFactory
                .createNewUserInteractor(authInfo.getUserName(), authInfo.getPasswordHash(),
                        responseFactory.createCreationUserResponsePresenter());
        return createUserInteractor.createNewUserAndGetPresenter().renderModel();
    }

    @Override
    public ResponseEntity<Void> logout() {
        return AuthenticationApi.super.logout();
    }

    @Override
    public ResponseEntity<TokenPair> refreshTokens() {
        return AuthenticationApi.super.refreshTokens();
    }
}
