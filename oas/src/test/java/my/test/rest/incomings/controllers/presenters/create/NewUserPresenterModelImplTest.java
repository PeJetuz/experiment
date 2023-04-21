package my.test.rest.incomings.controllers.presenters.create;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import my.test.rest.incomings.controllers.api.dto.Authentication;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NewUserPresenterModelImplTest {

    @Test
    public void checkInvalidUserModelState() {
        NewUserPresenterModelImpl subj = buildInvalidUserPresenterState();

        ResponseEntity<Authentication> result = subj.renderModel();

        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    private static NewUserPresenterModelImpl buildInvalidUserPresenterState() {
        NewUserPresenterModelImpl subj = new NewUserPresenterModelImpl(null);
        subj.invalidUserNameField();
        return subj;
    }

    @Test
    public void checkInvalidPasswordHashModelState() {
        NewUserPresenterModelImpl subj = buildInvalidPasswordHashPresenterState();

        ResponseEntity<Authentication> result = subj.renderModel();

        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    private static NewUserPresenterModelImpl buildInvalidPasswordHashPresenterState() {
        NewUserPresenterModelImpl subj = new NewUserPresenterModelImpl(null);
        subj.invalidPasswordHashField();
        return subj;
    }

    @Test
    public void checkStatusCodeOk() {
        NewUserPresenterModelImpl subj = new NewUserPresenterModelImpl(null);

        ResponseEntity<Authentication> result = subj.renderModel();

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void checkUsernameInResponse() {
        Random random = ThreadLocalRandom.current();
        String userName = "userName" + random.nextLong();
        NewUserPresenterModelImpl subj = new NewUserPresenterModelImpl(userName);

        ResponseEntity<Authentication> result = subj.renderModel();

        assertEquals(userName, result.getBody().getUsername());
    }

    @Test
    public void checkWriteTokenInResponse() {
        NewUserPresenterModelImpl subj = buildPresenterWithToken();

        ResponseEntity<Authentication> result = subj.renderModel();

        assertEquals("token", result.getBody().getAccessToken().getValue());
    }

    private static NewUserPresenterModelImpl buildPresenterWithToken() {
        NewUserPresenterModelImpl subj = new NewUserPresenterModelImpl(null);
        subj.writeToken("token");
        return subj;
    }


    @Test
    public void writeExpirationDateTime() {
        LocalDateTime expirationDateTime = LocalDateTime.now();
        NewUserPresenterModelImpl subj = buildPresenterWithExpirationDateTime(expirationDateTime);

        ResponseEntity<Authentication> result = subj.renderModel();

        assertEquals(expirationDateTime, result.getBody().getAccessToken().getExpirationDateTime());
    }

    private static NewUserPresenterModelImpl buildPresenterWithExpirationDateTime(LocalDateTime expirationDateTime) {
        NewUserPresenterModelImpl subj = new NewUserPresenterModelImpl(null);
        subj.writeExpirationDateTime(expirationDateTime);
        return subj;
    }
}
