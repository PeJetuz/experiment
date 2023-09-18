package my.test.rest.incomings.controllers.presenters.creation;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Random;
import my.test.authorization.rules.CreationUserResponsePresenter;
import my.test.authorization.domain.api.CreationUserResponseFactory.UserData;
import my.test.rest.incomings.controllers.api.dto.Authentication;
import my.test.rest.incomings.controllers.api.dto.Token;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.FORBIDDEN;

public class CreationUserResponseModelImplTest {

    @Test
    public void userAlreadyExists() {
        CreationUserResponseModelImpl subj = new CreationUserResponseModelImpl();
        subj.userAlreadyExists();

        assertEquals(ResponseEntity.status(FORBIDDEN).build(), subj.renderModel());
    }

    @Test
    public void invalidUserNameField() {
        CreationUserResponseModelImpl subj = new CreationUserResponseModelImpl();
        subj.invalidUserNameField();

        assertEquals(ResponseEntity.status(FORBIDDEN).build(), subj.renderModel());
    }

    @Test
    public void invalidPasswordHashField() {
        CreationUserResponseModelImpl subj = new CreationUserResponseModelImpl();
        subj.invalidPasswordHashField();

        assertEquals(ResponseEntity.status(FORBIDDEN).build(), subj.renderModel());
    }

    @Test
    public void success() {
        String name = "name" + new Random().nextInt();
        String token = "token" + new Random().nextInt();
        LocalDateTime expected = LocalDateTime.now();
        CreationUserResponseModelImpl subj = new CreationUserResponseModelImpl();
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
        CreationUserResponsePresenter.Fake subj = new CreationUserResponsePresenter.Fake();
        subj.userAlreadyExists();
        subj.invalidUserNameField();
        subj.invalidPasswordHashField();
        subj.success();
        subj.renderModel();
    }
}
