package my.test.authorization.rules;

import my.test.rest.incomings.controllers.AuthenticationResponseModel;


public interface AuthenticationResponsePresenter extends
        AuthenticationResponseBuilder,
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
        public void passwordFailed() {
            this.incorrectPassword = true;
        }

        @Override
        public UserInfoBuilder success() {
            return null;
        }
    }
}
