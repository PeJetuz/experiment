package my.test.rest.incomings.controllers.presenters.creation;

import io.helidon.common.http.Http.Status;
import io.helidon.webserver.ServerResponse;
import jakarta.json.bind.Jsonb;
import java.time.LocalDateTime;
import java.time.ZoneId;
import my.test.authorization.domain.api.CreationUserResponseFactory.UserData;
import my.test.authorization.rules.CreationUserResponsePresenter;
import my.test.rest.incomings.controllers.api.dto.Authentication;
import my.test.rest.incomings.controllers.api.dto.Token;
import my.test.rest.incomings.controllers.presenters.ErrorMessage;

public class CreationUserResponseModelImpl implements CreationUserResponsePresenter, UserData {

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
    public void userAlreadyExists() {
        this.send = () -> {
            String returnObject = jsonb.toJson(new ErrorMessage("User already exists"));
            response.status(Status.FORBIDDEN_403).send(returnObject);
        };
    }

    @Override
    public void invalidUserNameField() {
        this.send = () -> {
            String returnObject = jsonb.toJson(new ErrorMessage("Invalid user name"));
            response.status(Status.FORBIDDEN_403).send(returnObject);
        };
    }

    @Override
    public void invalidPasswordHashField() {
        this.send = () -> {
            String returnObject = jsonb.toJson(new ErrorMessage("Invalid user password"));
            response.status(Status.FORBIDDEN_403).send(returnObject);
        };
    }

    @Override
    public UserData success() {
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
    public void writeTokenExpirationDate(LocalDateTime expirationDateTime) {
        authentication.getAccessToken()
                .setExpirationDateTime(expirationDateTime.atZone(ZoneId.systemDefault()).toOffsetDateTime());
    }

    @Override
    public void send() {
        send.run();
    }
}
