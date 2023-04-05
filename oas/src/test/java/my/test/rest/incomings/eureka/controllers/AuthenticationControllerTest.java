package my.test.rest.incomings.eureka.controllers;

import java.util.Random;
import my.test.rest.incomings.eureka.controllers.dto.AuthInfo;
import my.test.rest.incomings.eureka.controllers.dto.Authentication;
import my.test.rest.incomings.eureka.controllers.presenters.create.CreatePresenterModel;
import my.test.rest.incomings.eureka.controllers.presenters.login.LoginPresenterModel;
import my.test.rest.incomings.eureka.rules.AuthFactory;
import my.test.rest.incomings.eureka.rules.CreatePresenter;
import my.test.rest.incomings.eureka.rules.CreateProcess;
import my.test.rest.incomings.eureka.rules.LoginPresenter;
import my.test.rest.incomings.eureka.rules.LoginProcess;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class AuthenticationControllerTest {

    @Test
    public void loginBody() {
        Random random = new Random();
        String userName = "username" + random.nextLong();
        String passwordHash = "passwordHash" + random.nextLong();
        AuthInfo authInfo = new AuthInfo().userName(userName).passwordHash(passwordHash);
        LoginProcess loginProcess = Mockito.mock(LoginProcess.class);
        AuthFactory authFactory = Mockito.mock(AuthFactory.class);
        when(authFactory.createLoginProcess(isA(LoginPresenter.class), eq(userName), eq(passwordHash)))
                .thenReturn(loginProcess);
        AuthenticationController controller = new AuthenticationController(authFactory);
        ResponseEntity<Authentication> result = controller.login(authInfo);
        assertEquals(OK, result.getStatusCode());
        Authentication body = result.getBody();
        assertEquals(userName, body.getUsername());
    }

    @Test
    public void loginUnauthorized() {
        Random random = new Random();
        String userName = "username" + random.nextLong();
        String passwordHash = "passwordHash" + random.nextLong();
        AuthInfo authInfo = new AuthInfo().userName(userName).passwordHash(passwordHash);
        AuthFactory authFactory = Mockito.mock(AuthFactory.class);
        ArgumentCaptor<LoginPresenterModel> presenterModel = ArgumentCaptor.forClass(LoginPresenterModel.class);
        LoginProcess loginProcess = () -> presenterModel.getValue().initUserNotFoundResponseModel();
        when(authFactory.createLoginProcess(presenterModel.capture(), eq(userName), eq(passwordHash)))
                .thenReturn(loginProcess);
        AuthenticationController controller = new AuthenticationController(authFactory);
        ResponseEntity<Authentication> result = controller.login(authInfo);
        assertEquals(UNAUTHORIZED, result.getStatusCode());
    }

    @Test
    public void createTestFailed() {
        AuthInfo authInfo = new AuthInfo().userName(null).passwordHash(null);
        AuthFactory authFactory = Mockito.mock(AuthFactory.class);
        ArgumentCaptor<CreatePresenterModel> presenterModel = ArgumentCaptor.forClass(CreatePresenterModel.class);
        CreateProcess createProcess = () -> {
            presenterModel.getValue().initUserAlreadyExistsResponseModel();
            return false;
        };
        when(authFactory.createNewUserProcess(presenterModel.capture(), eq(null), eq(null))).thenReturn(createProcess);
        AuthenticationController controller = new AuthenticationController(authFactory);
        ResponseEntity<Authentication> result = controller.create(authInfo);
        assertEquals(FORBIDDEN, result.getStatusCode());
    }

    @Test
    public void createTest() {
        Random random = new Random();
        String userName = "username" + random.nextLong();
        String passwordHash = "passwordHash" + random.nextLong();
        AuthInfo authInfo = new AuthInfo().userName(userName).passwordHash(passwordHash);
        CreateProcess createProcess = Mockito.mock(CreateProcess.class);
        AuthFactory authFactory = Mockito.mock(AuthFactory.class);
        when(authFactory.createNewUserProcess(isA(CreatePresenter.class), eq(userName), eq(passwordHash))).thenReturn(
                createProcess);
        when(createProcess.createNewUser()).thenReturn(true);
        AuthenticationController controller = new AuthenticationController(authFactory);
        ResponseEntity<Authentication> result = controller.create(authInfo);
        verify(createProcess).createNewUser();
        assertEquals(OK, result.getStatusCode());
        Authentication body = result.getBody();
        assertEquals(userName, body.getUsername());
    }
}
