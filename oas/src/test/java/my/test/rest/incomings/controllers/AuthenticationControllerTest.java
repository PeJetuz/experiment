package my.test.rest.incomings.controllers;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import my.test.rest.incomings.controllers.api.dto.AuthInfo;
import my.test.rest.incomings.controllers.api.dto.Authentication;
import my.test.rest.incomings.controllers.api.dto.TokenPair;
import my.test.rest.incomings.controllers.presenters.create.NewUserPresenterModel;
import my.test.rest.incomings.controllers.presenters.login.LoginUserPresenterModel;
import my.test.authorization.rules.AuthFactory;
import my.test.authorization.rules.NewUserPresenter;
import my.test.authorization.rules.NewUserInteractor;
import my.test.authorization.rules.LoginUserPresenter;
import my.test.authorization.rules.LoginUserInteractor;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class AuthenticationControllerTest {

    private AuthFactory authFactory = Mockito.mock(AuthFactory.class);
    private AuthenticationController controller = new AuthenticationController(authFactory);
    private Random random = ThreadLocalRandom.current();
    private String userName = "username" + random.nextLong();
    private String passwordHash = "passwordHash" + random.nextLong();

    @Test
    public void checkLoginStatusCodeOk() {
        AuthInfo authInfo = buildAuthInfoForCheckLoginOk();

        ResponseEntity<Authentication> result = controller.login(authInfo);

        assertEquals(OK, result.getStatusCode());
    }

    @Test
    public void checkUsernameLoginResponse() {
        AuthInfo authInfo = buildAuthInfoForCheckLoginOk();

        ResponseEntity<Authentication> result = controller.login(authInfo);

        Authentication body = result.getBody();
        assertEquals(userName, body.getUsername());
    }

    private AuthInfo buildAuthInfoForCheckLoginOk() {
        LoginUserInteractor loginUserInteractor = Mockito.mock(LoginUserInteractor.class);
        when(authFactory.createLoginUserInteractor(isA(LoginUserPresenter.class), eq(userName), eq(passwordHash)))
                .thenReturn(loginUserInteractor);
        return new AuthInfo().userName(userName).passwordHash(passwordHash);
    }

    @Test
    public void checkUnauthorizedLogin() {
        AuthInfo authInfo = buildAuthInfoForCheckUnauthorizedLogin();

        ResponseEntity<Authentication> result = controller.login(authInfo);

        assertEquals(UNAUTHORIZED, result.getStatusCode());
    }

    private AuthInfo buildAuthInfoForCheckUnauthorizedLogin() {
        ArgumentCaptor<LoginUserPresenterModel> presenterModel = ArgumentCaptor.forClass(LoginUserPresenterModel.class);
        LoginUserInteractor loginUserInteractor = () -> presenterModel.getValue().initUserNotFoundResponseModel();
        when(authFactory.createLoginUserInteractor(presenterModel.capture(), eq(userName), eq(passwordHash)))
                .thenReturn(loginUserInteractor);
        return new AuthInfo().userName(userName).passwordHash(passwordHash);
    }

    @Test
    public void checkCreateAlreadyExistingUser() {
        AuthInfo authInfo = buildAuthInfoForAlreadyExistingUser();

        ResponseEntity<Authentication> result = controller.create(authInfo);

        assertEquals(FORBIDDEN, result.getStatusCode());
    }

    private AuthInfo buildAuthInfoForAlreadyExistingUser() {
        AuthInfo authInfo = new AuthInfo().userName(null).passwordHash(null);
        ArgumentCaptor<NewUserPresenterModel> presenterModel = ArgumentCaptor.forClass(NewUserPresenterModel.class);
        NewUserInteractor newUserInteractor = () -> {
            presenterModel.getValue().initUserAlreadyExistsResponseModel();
            return false;
        };
        when(authFactory.createNewUserProcess(presenterModel.capture(), eq(null), eq(null))).thenReturn(
                newUserInteractor);
        return authInfo;
    }

    @Test
    public void checkStatusCodeOkForCreatingUser() {
        NewUserInteractor newUserInteractor = Mockito.mock(NewUserInteractor.class);
        AuthInfo authInfo = buildAuthInfoForCreatingUserOk(newUserInteractor);

        ResponseEntity<Authentication> result = controller.create(authInfo);

        verify(newUserInteractor).createNewUser();
        assertEquals(OK, result.getStatusCode());
    }

    @Test
    public void checkResponseUserNameForCreatingUser() {
        NewUserInteractor newUserInteractor = Mockito.mock(NewUserInteractor.class);
        AuthInfo authInfo = buildAuthInfoForCreatingUserOk(newUserInteractor);

        ResponseEntity<Authentication> result = controller.create(authInfo);

        verify(newUserInteractor).createNewUser();
        Authentication body = result.getBody();
        assertEquals(userName, body.getUsername());
    }

    private AuthInfo buildAuthInfoForCreatingUserOk(NewUserInteractor newUserInteractor) {
        AuthInfo authInfo = new AuthInfo().userName(userName).passwordHash(passwordHash);
        when(authFactory.createNewUserProcess(isA(NewUserPresenter.class), eq(userName), eq(passwordHash))).thenReturn(
                newUserInteractor);
        when(newUserInteractor.createNewUser()).thenReturn(true);
        return authInfo;
    }

    @Test
    public void logout() {
        ResponseEntity<Void> result = controller.logout();

        assertNotNull(result);
    }

    @Test
    public void refreshTokens() {
        ResponseEntity<TokenPair> result = controller.refreshTokens();

        assertNotNull(result);
    }
}
