package my.test.authorization.rules;

import my.test.authorization.domain.api.AuthenticationResponseFactory;
import my.test.rest.incomings.controllers.AuthenticationResponseModel;


public interface AuthenticationResponsePresenter extends AuthenticationResponseFactory,
        AuthenticationResponseModel {

    final class Fake implements AuthenticationResponsePresenter {
        public boolean incorrectPassword;
        public boolean userNotFound;

        @Override
        public void send() {
        }

        @Override
        public void userNotFound() {
            this.userNotFound = true;
        }

        @Override
        public void incorrectPassword() {
            this.incorrectPassword = true;
        }

        @Override
        public UserData success() {
            return null;
        }
    }
}
