package my.test.rest.incomings.controllers;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import my.test.authorization.rules.AuthenticateUserInteractor;
import my.test.authorization.rules.ResponseFactory;
import my.test.rest.incomings.controllers.api.dto.AuthInfo;
import my.test.rest.incomings.controllers.api.dto.Authentication;
import my.test.rest.incomings.controllers.api.dto.TokenPair;
import my.test.authorization.rules.AuthFactory;
import my.test.authorization.rules.CreateUserInteractor;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuthenticationControllerTest {

    private Random random = ThreadLocalRandom.current();
    private String userName = "username" + random.nextLong();
    private String passwordHash = "passwordHash" + random.nextLong();

    @Test
    public void checkLogin() {
        ResponseEntity<Authentication> renderModel = ResponseEntity.ok(new Authentication());
        AuthenticationController controller = new AuthenticationController(new AuthFactory.Fake(
                new AuthenticateUserInteractor.Fake(new AuthenticationResponseModel.Fake(renderModel)), null),
                new ResponseFactory.Fake());

        ResponseEntity<Authentication> result = controller.login(new AuthInfo());

        assertEquals(renderModel, result);
    }

    @Test
    public void createUser() {
        ResponseEntity<Authentication> renderModel = ResponseEntity.ok(new Authentication());
        AuthenticationController controller = new AuthenticationController(new AuthFactory.Fake(null,
                new CreateUserInteractor.Fake(new CreationUserResponseModel.Fake(renderModel))),
                new ResponseFactory.Fake());
        AuthInfo authInfo = new AuthInfo();

        ResponseEntity<Authentication> result = controller.create(authInfo);

        assertEquals(renderModel, result);
    }

    @Test
    public void logout() {
        ResponseEntity<Authentication> renderModel = ResponseEntity.ok(new Authentication());
        AuthenticationController controller = new AuthenticationController(new AuthFactory.Fake(
                new AuthenticateUserInteractor.Fake(new AuthenticationResponseModel.Fake(renderModel)), null),
                new ResponseFactory.Fake());

        ResponseEntity<Void> result = controller.logout();

        assertNotNull(result);
    }

    @Test
    public void refreshTokens() {
        ResponseEntity<Authentication> renderModel = ResponseEntity.ok(new Authentication());
        AuthenticationController controller = new AuthenticationController(new AuthFactory.Fake(
                new AuthenticateUserInteractor.Fake(new AuthenticationResponseModel.Fake(renderModel)), null),
                new ResponseFactory.Fake());

        ResponseEntity<TokenPair> result = controller.refreshTokens();

        assertNotNull(result);
    }
}
