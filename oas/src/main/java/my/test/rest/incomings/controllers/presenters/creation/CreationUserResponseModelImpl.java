package my.test.rest.incomings.controllers.presenters.creation;

import java.time.LocalDateTime;
import java.time.ZoneId;
import my.test.authorization.rules.CreationUserResponsePresenter;
import my.test.authorization.domain.api.CreationUserResponseFactory.UserData;
import my.test.rest.incomings.controllers.api.dto.Authentication;
import my.test.rest.incomings.controllers.api.dto.Token;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class CreationUserResponseModelImpl implements CreationUserResponsePresenter, UserData {

    private final Authentication authentication;
    private ResponseEntity<Authentication> model;

    public CreationUserResponseModelImpl() {
        authentication = new Authentication().accessToken(new Token());
        model = ResponseEntity.ok(authentication);
    }

    @Override
    public void userAlreadyExists() {
        model = ResponseEntity.status(FORBIDDEN).build();
    }

    @Override
    public void invalidUserNameField() {
        model = ResponseEntity.status(FORBIDDEN).build();
    }

    @Override
    public void invalidPasswordHashField() {
        model = ResponseEntity.status(FORBIDDEN).build();
    }

    @Override
    public UserData success() {
        return this;
    }

    @Override
    public void writeUserName(String name) {
        authentication.username(name);
    }

    @Override
    public void writeToken(String token) {
        authentication.getAccessToken().value(token);
    }

    @Override
    public void writeTokenExpirationDate(LocalDateTime expirationDateTime) {
        authentication.getAccessToken()
                .expirationDateTime(expirationDateTime.atZone(ZoneId.systemDefault()).toOffsetDateTime());
    }

    @Override
    public ResponseEntity<Authentication> renderModel() {
        return model;
    }
}
