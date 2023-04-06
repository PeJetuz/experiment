package my.test.rest.incomings.eureka.controllers.presenters.login;

import java.time.LocalDateTime;
import my.test.rest.incomings.eureka.controllers.dto.Authentication;
import my.test.rest.incomings.eureka.controllers.dto.Token;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class LoginPresenterModelImpl implements LoginPresenterModel {

    private final String userName;
    private ResponseEntity<Authentication> model;
    private String token;
    private LocalDateTime expirationDateTime;

    public LoginPresenterModelImpl(String userName) {
        this.userName = userName;
    }

    @Override
    public void initUserNotFoundResponseModel() {
        model = ResponseEntity.status(UNAUTHORIZED).build();
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
    public ResponseEntity<Authentication> renderModel() {
        if (model != null) {
            return model;
        } else {
            return ResponseEntity.ok(new Authentication().accessToken(new Token().value(token)
                    .expirationDateTime(expirationDateTime)).username(userName));
        }
    }
}
