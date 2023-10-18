package my.test.rest.incomings.controllers.presenters.creation;

import io.helidon.common.http.Http.Status;
import java.time.LocalDateTime;
import java.time.ZoneId;
import my.test.rest.incomings.controllers.api.dto.Authentication;
import my.test.rest.incomings.controllers.presenters.JsonbSpy;
import my.test.rest.incomings.controllers.ServerResponseSpy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreationUserResponseModelImplTest {

    @Test
    public void testOK() {
        String result = "result test";
        LocalDateTime now = LocalDateTime.now();
        ServerResponseSpy response = new ServerResponseSpy();
        JsonbSpy jsonb = new JsonbSpy(result);
        CreationUserResponseModelImpl subj = new CreationUserResponseModelImpl(response, jsonb);
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
    public void testUserAlreadyExists() {
        String result = "User already exists";
        ServerResponseSpy response = new ServerResponseSpy();
        CreationUserResponseModelImpl subj = createModel(result, response);

        subj.userAlreadyExists();
        subj.send();

        assertEquals(Status.FORBIDDEN_403, response.status());
        assertEquals(result, response.content());
    }

    private CreationUserResponseModelImpl createModel(String jsonResult, ServerResponseSpy responseFake) {
        JsonbSpy jsonb = new JsonbSpy(jsonResult);
        return new CreationUserResponseModelImpl(responseFake, jsonb);
    }

    @Test
    public void testInvalidPasswordHashField() {
        String result = "Invalid user password";
        ServerResponseSpy response = new ServerResponseSpy();
        CreationUserResponseModelImpl subj = createModel(result, response);

        subj.invalidPasswordHashField();
        subj.send();

        assertEquals(Status.FORBIDDEN_403, response.status());
        assertEquals(result, response.content());
    }

    @Test
    public void testInvalidUserNameField() {
        String result = "Invalid user name";
        ServerResponseSpy response = new ServerResponseSpy();
        CreationUserResponseModelImpl subj = createModel(result, response);

        subj.invalidUserNameField();
        subj.send();

        assertEquals(Status.FORBIDDEN_403, response.status());
        assertEquals(result, response.content());
    }
}
