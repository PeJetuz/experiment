package my.test.authorization.rules;

import my.test.authorization.domain.api.CreationUserResponseFactory;
import my.test.rest.incomings.controllers.CreationUserResponseModel;

public interface CreationUserResponsePresenter extends CreationUserResponseFactory, CreationUserResponseModel {

    final class Fake implements CreationUserResponsePresenter {

        public boolean userAlreadyExists;
        public boolean invalidUserNameField;
        public boolean invalidPasswordHashField;

        @Override
        public void send() {
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
