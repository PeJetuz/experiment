package my.test.rest.incomings.controllers.presenters.login;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import my.test.rest.incomings.controllers.api.dto.Authentication;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginUserPresenterModelImplTest {

    private Random random = ThreadLocalRandom.current();
    private String userName = "username" + random.nextLong();
    private LoginUserPresenterModelImpl subj = new LoginUserPresenterModelImpl(userName);

    @Test
    public void checkStatusOk() {
        ResponseEntity<Authentication> result = subj.renderModel();

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void checkUserName() {
        ResponseEntity<Authentication> result = subj.renderModel();

        assertEquals(userName, result.getBody().getUsername());
    }

    @Test
    public void checkWriteToken() {
        String token = "token" + random.nextLong();
        buildPresenterForWriteToken(token);

        ResponseEntity<Authentication> result = subj.renderModel();

        assertEquals(token, result.getBody().getAccessToken().getValue());
    }

    private void buildPresenterForWriteToken(String token) {
        subj.writeToken(token);
    }

    @Test
    public void checkWriteExpirationDateTime() {
        LocalDateTime expirationDateTime = LocalDateTime.now();
        buildPresenterForWriteWriteExpirationDateTime(expirationDateTime);

        ResponseEntity<Authentication> result = subj.renderModel();

        assertEquals(expirationDateTime, result.getBody().getAccessToken().getExpirationDateTime());
    }

    private void buildPresenterForWriteWriteExpirationDateTime(
            LocalDateTime expirationDateTime) {
        subj.writeExpirationDateTime(expirationDateTime);
    }

    @Test
    public void checkUserNotFoundInRenderResponse() {
        buildPresenterForCheckUserNotFound();

        ResponseEntity<Authentication> result = subj.renderModel();

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
    }

    private void buildPresenterForCheckUserNotFound() {
        subj.initUserNotFoundResponseModel();
    }
}
