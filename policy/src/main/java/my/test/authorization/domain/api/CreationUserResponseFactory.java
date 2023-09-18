package my.test.authorization.domain.api;

import java.time.LocalDateTime;

public interface CreationUserResponseFactory {

    void userAlreadyExists();

    void invalidUserNameField();

    void invalidPasswordHashField();

    UserData success();

    interface UserData {

        void writeUserName(String name);

        void writeToken(String token);

        void writeTokenExpirationDate(LocalDateTime expirationDateTime);
    }

    final class Fake implements CreationUserResponseFactory, UserData {

        public boolean userAlreadyExists;
        public boolean invalidUserNameField;
        public boolean invalidPasswordHashField;
        public String name;
        public String token;
        public LocalDateTime expirationDateTime;

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
            return this;
        }

        @Override
        public void writeUserName(String name) {
            this.name = name;
        }

        @Override
        public void writeToken(String token) {
            this.token = token;
        }

        @Override
        public void writeTokenExpirationDate(LocalDateTime expirationDateTime) {
            this.expirationDateTime = expirationDateTime;
        }
    }
}
