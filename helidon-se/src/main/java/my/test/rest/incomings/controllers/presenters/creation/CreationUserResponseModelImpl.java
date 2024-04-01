package my.test.rest.incomings.controllers.presenters.creation;

import io.helidon.common.http.Http.Status;
import io.helidon.webserver.ServerResponse;
import jakarta.json.bind.Jsonb;
import java.time.LocalDateTime;
import java.time.ZoneId;
import my.test.authorization.domain.api.CreationUserResponseBuilder.UserInfoBuilder;
import my.test.authorization.rules.CreationUserResponsePresenter;
import my.test.rest.incomings.controllers.api.dto.Authentication;
import my.test.rest.incomings.controllers.api.dto.Token;
import my.test.rest.incomings.controllers.presenters.ErrorMessage;

public class CreationUserResponseModelImpl implements CreationUserResponsePresenter,
        UserInfoBuilder {

    private final Authentication authentication;
    private final ServerResponse response;

    private final Jsonb jsonb;
    private Runnable send;

    public CreationUserResponseModelImpl(ServerResponse response, Jsonb jsonb) {
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
    public void userExists() {
        this.send = () -> {
            String returnObject = jsonb.toJson(new ErrorMessage("User already exists"));
            response.status(Status.FORBIDDEN_403).send(returnObject);
        };
    }

    @Override
    public void emptyName() {
        this.send = () -> {
            String returnObject = jsonb.toJson(new ErrorMessage("Invalid user name"));
            response.status(Status.FORBIDDEN_403).send(returnObject);
        };
    }

    @Override
    public void emptyPassword() {
        this.send = () -> {
            String returnObject = jsonb.toJson(new ErrorMessage("Invalid user password"));
            response.status(Status.FORBIDDEN_403).send(returnObject);
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
