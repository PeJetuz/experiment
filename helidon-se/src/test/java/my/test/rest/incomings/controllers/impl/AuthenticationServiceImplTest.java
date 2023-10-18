package my.test.rest.incomings.controllers.impl;

import io.helidon.common.http.Http.Status;
import my.test.authorization.rules.AuthFactory;
import my.test.authorization.rules.AuthenticateUserInteractor;
import my.test.authorization.rules.CreateUserInteractor;
import my.test.authorization.rules.ResponseFactory;
import my.test.rest.incomings.controllers.AuthenticationResponseModel;
import my.test.rest.incomings.controllers.CreationUserResponseModel;
import my.test.rest.incomings.controllers.ServerRequestFake;
import my.test.rest.incomings.controllers.ServerResponseSpy;
import my.test.rest.incomings.controllers.api.dto.AuthInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthenticationServiceImplTest {

    @Test
    public void createTest() {
        CreationUserResponseModel.Fake model = new CreationUserResponseModel.Fake();
        AuthenticationServiceImpl subj = new AuthenticationServiceImpl(new AuthFactory.Fake(
                new AuthenticateUserInteractor.Fake(new AuthenticationResponseModel.Fake()),
                new CreateUserInteractor.Fake(model)), new ResponseFactory.Fake());

        subj.create(new ServerRequestFake(), new ServerResponseSpy(), new AuthInfo());

        assertEquals(1, model.sendCount);
    }

    @Test
    public void loginTest() {
        AuthenticationResponseModel.Fake model = new AuthenticationResponseModel.Fake();
        AuthenticationServiceImpl subj = new AuthenticationServiceImpl(new AuthFactory.Fake(
                new AuthenticateUserInteractor.Fake(model),
                new CreateUserInteractor.Fake(new CreationUserResponseModel.Fake())), new ResponseFactory.Fake());

        subj.login(new ServerRequestFake(), new ServerResponseSpy(), new AuthInfo());

        assertEquals(1, model.sendCount);
    }

    @Test
    public void logoutTest() {
        ServerResponseSpy response = new ServerResponseSpy();
        AuthenticationServiceImpl subj = new AuthenticationServiceImpl(null, null);

        subj.logout(new ServerRequestFake(), response);

        assertStatusAndResponse(response);
    }

    private static void assertStatusAndResponse(ServerResponseSpy response) {
        assertEquals(Status.NOT_IMPLEMENTED_501, response.status());
        assertEquals(1, response.sendCount);
    }

    @Test
    public void refreshTokensTest() {
        ServerResponseSpy response = new ServerResponseSpy();
        AuthenticationServiceImpl subj = new AuthenticationServiceImpl(null, null);

        subj.refreshTokens(new ServerRequestFake(), response);

        assertStatusAndResponse(response);
    }
}
