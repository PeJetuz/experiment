package my.test.rest.incomings.controllers.presenters.creation;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.time.LocalDateTime;
import java.time.ZoneId;
import my.test.authorization.domain.api.CreationUserResponseFactory.UserData;
import my.test.authorization.rules.CreationUserResponsePresenter;
import my.test.rest.incomings.controllers.api.dto.Authentication;
import my.test.rest.incomings.controllers.api.dto.Token;

public class CreationUserResponseModelImpl implements CreationUserResponsePresenter, UserData {

    private final Authentication authentication;
    private Response model;

    public CreationUserResponseModelImpl() {
        authentication = new Authentication().accessToken(new Token());
        model = Response.status(Status.OK).entity(authentication).build();
    }

    @Override
    public void userAlreadyExists() {
        model = Response.status(Response.Status.FORBIDDEN).entity("User already exists").build();
    }

    @Override
    public void invalidUserNameField() {
        model = Response.status(Response.Status.FORBIDDEN).entity("Invalid user name").build();
    }

    @Override
    public void invalidPasswordHashField() {
        model = Response.status(Response.Status.FORBIDDEN).entity("Invalid user password").build();
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
    public Response renderModel() {
        return model;
    }
}
