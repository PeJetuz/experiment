package my.test.authorization.rules;

import my.test.authorization.domain.api.AuthenticationResponseFactory;
import my.test.rest.incomings.controllers.AuthenticationResponseModel;
import my.test.rest.incomings.controllers.api.dto.Authentication;
import org.springframework.http.ResponseEntity;

public interface AuthenticationResponsePresenter extends AuthenticationResponseFactory,
        AuthenticationResponseModel {

    final class Fake implements AuthenticationResponsePresenter {

        @Override
        public ResponseEntity<Authentication> renderModel() {
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
