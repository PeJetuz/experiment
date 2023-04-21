package my.test.rest.incomings.controllers.presenters.create;

import java.time.LocalDateTime;
import my.test.rest.incomings.controllers.api.dto.Authentication;
import my.test.rest.incomings.controllers.api.dto.Token;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class NewUserPresenterModelImpl implements NewUserPresenterModel {

    private final String userName;
    private ResponseEntity<Authentication> model;
    private String token;
    private LocalDateTime expirationDateTime;

    public NewUserPresenterModelImpl(String userName) {
        this.userName = userName;
    }

    @Override
    public void initUserAlreadyExistsResponseModel() {
        model = ResponseEntity.status(FORBIDDEN).build();
    }

    @Override
    public void writeToken(String token) {
        this.token = token;
    }

    @Override
    public void writeExpirationDateTime(LocalDateTime expirationDateTime) {
        this.expirationDateTime = expirationDateTime;
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
    public ResponseEntity<Authentication> renderModel() {
        if (model != null) {
            return model;
        } else {
            return ResponseEntity.ok(new Authentication().accessToken(new Token().value(token)
                    .expirationDateTime(expirationDateTime)).username(userName));
        }
    }
}
