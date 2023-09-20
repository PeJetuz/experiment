package my.test.rest.incomings.controllers.presenters.authentication;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.time.LocalDateTime;
import java.time.ZoneId;
import my.test.authorization.domain.api.AuthenticationResponseFactory.UserData;
import my.test.authorization.rules.AuthenticationResponsePresenter;
import my.test.rest.incomings.controllers.api.dto.Authentication;
import my.test.rest.incomings.controllers.api.dto.Token;

public class AuthenticationUserResponseModelImpl implements AuthenticationResponsePresenter, UserData {

    private final Authentication authentication;
    private Response model;

    public AuthenticationUserResponseModelImpl() {
        authentication = new Authentication().accessToken(new Token());
        model = Response.status(Status.OK).entity(authentication).build();
    }

    @Override
    public void userNotFound() {
        model = Response.status(Status.UNAUTHORIZED).entity("User not found").build();
    }

    @Override
    public void incorrectPassword() {
        model = Response.status(Status.UNAUTHORIZED).entity("Invalid password").build();
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
