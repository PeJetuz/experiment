package my.test.rest.incomings.controllers.presenters.authentication;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Random;
import my.test.authorization.domain.api.AuthenticationResponseFactory.UserData;
import my.test.authorization.rules.AuthenticationResponsePresenter;
import my.test.rest.incomings.controllers.api.dto.Authentication;
import my.test.rest.incomings.controllers.api.dto.Token;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class AuthenticationUserModelImplTest {

    @Test
    public void checkUserNotFound() {
        AuthenticationUserResponseModelImpl subj = new AuthenticationUserResponseModelImpl();
        subj.userNotFound();

        assertEquals(ResponseEntity.status(UNAUTHORIZED).build(), subj.renderModel());
    }

    @Test
    public void checkIncorrectUserPassword() {
        AuthenticationUserResponseModelImpl subj = new AuthenticationUserResponseModelImpl();
        subj.incorrectPassword();

        assertEquals(ResponseEntity.status(UNAUTHORIZED).build(), subj.renderModel());
    }

    @Test
    public void checkIncorrectSuccess() {
        String name = "name" + new Random().nextInt();
        String token = "token" + new Random().nextInt();
        LocalDateTime expected = LocalDateTime.now();
        AuthenticationUserResponseModelImpl subj = new AuthenticationUserResponseModelImpl();
        UserData data = subj.success();
        data.writeUserName(name);
        data.writeToken(token);
        data.writeTokenExpirationDate(expected);

        assertEquals(ResponseEntity.ok(new Authentication().accessToken(new Token().value(token)
                        .expirationDateTime(expected.atZone(ZoneId.systemDefault()).toOffsetDateTime())).username(name)),
                subj.renderModel());
    }

    @Test
    public void fake() {
        AuthenticationResponsePresenter.Fake subj = new AuthenticationResponsePresenter.Fake();
        subj.incorrectPassword();
        subj.userNotFound();
        subj.success();
        subj.renderModel();
    }
}
