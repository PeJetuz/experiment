package my.test.authorization.rules;

import jakarta.ws.rs.core.Response;
import my.test.authorization.domain.api.AuthenticationResponseFactory;
import my.test.rest.incomings.controllers.AuthenticationResponseModel;


public interface AuthenticationResponsePresenter extends AuthenticationResponseFactory,
        AuthenticationResponseModel {

    final class Fake implements AuthenticationResponsePresenter {

        @Override
        public Response renderModel() {
            return null;
        }

        @Override
        public void userNotFound() {

        }

        @Override
        public void incorrectPassword() {

        }

        @Override
        public UserData success() {
            return null;
        }
    }
}
