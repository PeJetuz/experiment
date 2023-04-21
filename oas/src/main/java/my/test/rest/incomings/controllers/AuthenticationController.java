package my.test.rest.incomings.controllers;

import my.test.rest.incomings.controllers.api.AuthenticationApi;
import my.test.rest.incomings.controllers.api.dto.AuthInfo;
import my.test.rest.incomings.controllers.api.dto.Authentication;
import my.test.rest.incomings.controllers.api.dto.TokenPair;
import my.test.rest.incomings.controllers.presenters.create.NewUserPresenterModel;
import my.test.rest.incomings.controllers.presenters.create.NewUserPresenterModelImpl;
import my.test.rest.incomings.controllers.presenters.login.LoginUserPresenterModel;
import my.test.rest.incomings.controllers.presenters.login.LoginUserPresenterModelImpl;
import my.test.authorization.rules.AuthFactory;
import my.test.authorization.rules.NewUserInteractor;
import my.test.authorization.rules.LoginUserInteractor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/api")
public class AuthenticationController implements AuthenticationApi {

    private final AuthFactory authFactory;

    public AuthenticationController(AuthFactory authFactory) {
        this.authFactory = authFactory;
    }

    @Override
    public ResponseEntity<Authentication> login(AuthInfo authInfo) {
        LoginUserPresenterModel presenter = new LoginUserPresenterModelImpl(authInfo.getUserName());
        LoginUserInteractor loginUserInteractor = authFactory
                .createLoginUserInteractor(presenter, authInfo.getUserName(), authInfo.getPasswordHash());
        loginUserInteractor.login();
        return presenter.renderModel();
    }

    @Override
    public ResponseEntity<Authentication> create(AuthInfo authInfo) {
        NewUserPresenterModel presenter = new NewUserPresenterModelImpl(authInfo.getUserName());
        NewUserInteractor newUserInteractor = authFactory
                .createNewUserProcess(presenter, authInfo.getUserName(), authInfo.getPasswordHash());
        newUserInteractor.createNewUser();
        return presenter.renderModel();
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
