package my.test.authorization.rules;

import my.test.authorization.domain.api.CreationUserResponseBuilder;
import my.test.rest.incomings.controllers.CreationUserResponseModel;

public interface CreationUserResponsePresenter extends
        CreationUserResponseBuilder, CreationUserResponseModel {

    final class Fake implements CreationUserResponsePresenter {

        public boolean userAlreadyExists;
        public boolean invalidUserNameField;
        public boolean invalidPasswordHashField;

        @Override
        public void send() {
        }

        @Override
        public void userExists() {
            userAlreadyExists = true;
        }

        @Override
        public void emptyName() {
            invalidUserNameField = true;
        }

        @Override
        public void emptyPassword() {
            invalidPasswordHashField = true;
        }

        @Override
        public UserInfoBuilder success() {
            return null;
        }
    }
}
