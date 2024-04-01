package my.test.rest.incomings.controllers.presenters.authentication;

import io.helidon.common.http.Http.Status;
import io.helidon.webserver.ServerResponse;
import jakarta.json.bind.Jsonb;
import java.time.LocalDateTime;
import java.time.ZoneId;
import my.test.authorization.rules.AuthenticationResponsePresenter;
import my.test.rest.incomings.controllers.api.dto.Authentication;
import my.test.rest.incomings.controllers.api.dto.Token;
import my.test.rest.incomings.controllers.presenters.ErrorMessage;

public class AuthenticationUserResponseModelImpl implements AuthenticationResponsePresenter,
        UserInfoBuilder {

    private final Authentication authentication;
    private final ServerResponse response;

    private final Jsonb jsonb;
    private Runnable send;

    public AuthenticationUserResponseModelImpl(ServerResponse response, Jsonb jsonb) {
        this.response = response;
        this.jsonb = jsonb;
        this.authentication = new Authentication();
        this.authentication.setAccessToken(new Token());
        this.send = () -> {
            String returnObject = jsonb.toJson(authentication);
            response.status(Status.OK_200).send(returnObject);
        };
    }

    @Override
    public void userNotFound() {
        this.send = () -> {
            String returnObject = jsonb.toJson(new ErrorMessage("User not found"));
            response.status(Status.UNAUTHORIZED_401).send(returnObject);
        };
    }

    @Override
    public void passwordFailed() {
        this.send = () -> {
            String returnObject = jsonb.toJson(new ErrorMessage("Invalid password"));
            response.status(Status.UNAUTHORIZED_401).send(returnObject);
        };
    }

    @Override
    public UserInfoBuilder success() {
        return this;
    }

    @Override
    public void writeUserName(String name) {
        authentication.setUsername(name);
    }

    @Override
    public void writeToken(String token) {
        authentication.getAccessToken().setValue(token);
    }

    @Override
    public void writeTokenExpirationDate(LocalDateTime expiration) {
        authentication.getAccessToken()
                .setExpirationDateTime(
                        expiration.atZone(ZoneId.systemDefault()).toOffsetDateTime());
    }

    @Override
    public void send() {
        send.run();
    }
}
