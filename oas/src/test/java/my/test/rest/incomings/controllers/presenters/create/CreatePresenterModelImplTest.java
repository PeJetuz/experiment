package my.test.rest.incomings.controllers.presenters.create;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import my.test.rest.incomings.controllers.api.dto.Authentication;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreatePresenterModelImplTest {

    @Test
    public void invalidUserNameField() {
        CreatePresenterModelImpl subj = new CreatePresenterModelImpl(null);
        subj.invalidUserNameField();
        ResponseEntity<Authentication> result = subj.renderModel();
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    public void invalidPasswordHashField() {
        CreatePresenterModelImpl subj = new CreatePresenterModelImpl(null);
        subj.invalidPasswordHashField();
        ResponseEntity<Authentication> result = subj.renderModel();
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    public void writeToken() {
        Random random = ThreadLocalRandom.current();
        String userName = "userName" + random.nextLong();
        String token = "token" + random.nextLong();
        CreatePresenterModelImpl subj = new CreatePresenterModelImpl(userName);
        subj.writeToken(token);
        ResponseEntity<Authentication> result = subj.renderModel();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        Authentication body = result.getBody();
        assertEquals(userName, body.getUsername());
        assertEquals(token, body.getAccessToken().getValue());
    }


    @Test
    public void writeExpirationDateTime() {
        Random random = ThreadLocalRandom.current();
        String userName = "userName" + random.nextLong();
        LocalDateTime expirationDateTime = LocalDateTime.now();
        CreatePresenterModelImpl subj = new CreatePresenterModelImpl(userName);
        subj.writeExpirationDateTime(expirationDateTime);
        ResponseEntity<Authentication> result = subj.renderModel();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        Authentication body = result.getBody();
        assertEquals(userName, body.getUsername());
        assertEquals(expirationDateTime, body.getAccessToken().getExpirationDateTime());
    }
}
