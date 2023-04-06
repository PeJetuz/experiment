package my.test.rest.incomings.controllers;

import my.test.rest.incomings.controllers.api.AuthenticationApi;
import my.test.rest.incomings.controllers.api.dto.AuthInfo;
import my.test.rest.incomings.controllers.api.dto.Authentication;
import my.test.rest.incomings.controllers.api.dto.TokenPair;
import my.test.rest.incomings.controllers.presenters.create.CreatePresenterModel;
import my.test.rest.incomings.controllers.presenters.create.CreatePresenterModelImpl;
import my.test.rest.incomings.controllers.presenters.login.LoginPresenterModel;
import my.test.rest.incomings.controllers.presenters.login.LoginPresenterModelImpl;
import my.test.authorization.rules.AuthFactory;
import my.test.authorization.rules.CreateProcess;
import my.test.authorization.rules.LoginProcess;
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
        LoginPresenterModel presenter = new LoginPresenterModelImpl(authInfo.getUserName());
        LoginProcess loginProcess = authFactory
                .createLoginProcess(presenter, authInfo.getUserName(), authInfo.getPasswordHash());
        loginProcess.login();
        return presenter.renderModel();
    }

    @Override
    public ResponseEntity<Authentication> create(AuthInfo authInfo) {
        CreatePresenterModel presenter = new CreatePresenterModelImpl(authInfo.getUserName());
        CreateProcess createProcess = authFactory
                .createNewUserProcess(presenter, authInfo.getUserName(), authInfo.getPasswordHash());
        createProcess.createNewUser();
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
