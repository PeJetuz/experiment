package my.test.authorization.rules;

import my.test.authorization.domain.api.CreationUserResponseFactory;
import my.test.rest.incomings.controllers.CreationUserResponseModel;
import my.test.rest.incomings.controllers.api.dto.Authentication;
import org.springframework.http.ResponseEntity;

public interface CreationUserResponsePresenter extends CreationUserResponseFactory, CreationUserResponseModel {

    final class Fake implements CreationUserResponsePresenter {

        public boolean userAlreadyExists;
        public boolean invalidUserNameField;
        public boolean invalidPasswordHashField;

        @Override
        public ResponseEntity<Authentication> renderModel() {
            return null;
        }

        @Override
        public void userAlreadyExists() {
            userAlreadyExists = true;
        }

        @Override
        public void invalidUserNameField() {
            invalidUserNameField = true;
        }

        @Override
        public void invalidPasswordHashField() {
            invalidPasswordHashField = true;
        }

        @Override
        public UserData success() {
            return null;
        }
    }
}
