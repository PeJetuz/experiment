package my.test.rest.incomings.controllers.presenters.authentication;

import java.time.LocalDateTime;
import java.time.ZoneId;
import my.test.authorization.domain.api.AuthenticationResponseFactory.UserData;
import my.test.authorization.rules.AuthenticationResponsePresenter;
import my.test.rest.incomings.controllers.api.dto.Authentication;
import my.test.rest.incomings.controllers.api.dto.Token;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class AuthenticationUserResponseModelImpl implements AuthenticationResponsePresenter, UserData {

    private final Authentication authentication;
    private ResponseEntity<Authentication> model;

    public AuthenticationUserResponseModelImpl() {
        authentication = new Authentication().accessToken(new Token());
        model = ResponseEntity.ok(authentication);
    }

    @Override
    public void userNotFound() {
        model = ResponseEntity.status(UNAUTHORIZED).build();
    }

    @Override
    public void incorrectPassword() {
        model = ResponseEntity.status(UNAUTHORIZED).build();
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
