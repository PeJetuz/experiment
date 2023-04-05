package my.test.rest.incomings.eureka.controllers.presenters;

import java.time.LocalDateTime;
import java.util.Random;
import my.test.rest.incomings.eureka.controllers.dto.Authentication;
import my.test.rest.incomings.eureka.controllers.presenters.login.LoginPresenterModelImpl;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginPresenterModelImplTest {

    @Test
    public void buildModelTest() {
        Random random = new Random();
        String userName = "username" + random.nextLong();
        String token = "token" + random.nextLong();
        LocalDateTime expirationDateTime = LocalDateTime.now();
        LoginPresenterModelImpl subj = new LoginPresenterModelImpl(userName);
        subj.writeToken(token);
        subj.writeExpirationDateTime(expirationDateTime);
        ResponseEntity<Authentication> result = subj.buildModel();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        Authentication body = result.getBody();
        assertEquals(userName, body.getUsername());
        assertEquals(token, body.getAccessToken().getValue());
        assertEquals(expirationDateTime, body.getAccessToken().getExpirationDateTime());
    }

    @Test
    public void buildModelUserNotFoundTest() {
        Random random = new Random();
        String userName = "username" + random.nextLong();
        LoginPresenterModelImpl subj = new LoginPresenterModelImpl(userName);
        subj.initUserNotFoundResponseModel();
        ResponseEntity<Authentication> result = subj.buildModel();
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
    }
}
