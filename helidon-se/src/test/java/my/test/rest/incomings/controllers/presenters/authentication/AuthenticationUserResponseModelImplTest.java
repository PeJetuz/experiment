package my.test.rest.incomings.controllers.presenters.authentication;

import io.helidon.common.http.Http.Status;
import java.time.LocalDateTime;
import java.time.ZoneId;
import my.test.rest.incomings.controllers.api.dto.Authentication;
import my.test.rest.incomings.controllers.presenters.JsonbSpy;
import my.test.rest.incomings.controllers.ServerResponseSpy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthenticationUserResponseModelImplTest {

    @Test
    public void testOK() {
        String result = "result test";
        LocalDateTime now = LocalDateTime.now();
        ServerResponseSpy response = new ServerResponseSpy();
        JsonbSpy jsonb = new JsonbSpy(result);
        AuthenticationUserResponseModelImpl subj = new AuthenticationUserResponseModelImpl(response, jsonb);
        subj.writeUserName("userName");
        subj.writeToken("token");
        subj.writeTokenExpirationDate(now);

        subj.send();
        Authentication auth = (Authentication) jsonb.toJsonParam();

        assertEquals(subj, subj.success());
        assertEquals(Status.OK_200, response.status());
        assertEquals(result, response.content());
        assertEquals("userName", auth.getUsername());
        assertEquals("token", auth.getAccessToken().getValue());
        assertEquals(now.atZone(ZoneId.systemDefault()).toOffsetDateTime(),
                auth.getAccessToken().getExpirationDateTime());
    }

    @Test
    public void testUserNotFound() {
        String result = "User not found";
        ServerResponseSpy response = new ServerResponseSpy();
        AuthenticationUserResponseModelImpl subj = createModel(result, response);

        subj.userNotFound();
        subj.send();

        assertEquals(Status.UNAUTHORIZED_401, response.status());
        assertEquals(result, response.content());
    }

    private AuthenticationUserResponseModelImpl createModel(String jsonResult, ServerResponseSpy responseFake) {
        JsonbSpy jsonb = new JsonbSpy(jsonResult);
        return new AuthenticationUserResponseModelImpl(responseFake, jsonb);
    }

    @Test
    public void testIncorrectPassword() {
        String result = "Invalid password";
        ServerResponseSpy response = new ServerResponseSpy();
        AuthenticationUserResponseModelImpl subj = createModel(result, response);

        subj.incorrectPassword();
        subj.send();

        assertEquals(Status.UNAUTHORIZED_401, response.status());
        assertEquals(result, response.content());
    }
}
