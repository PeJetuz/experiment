package my.test.authorization.domain.api;

import java.time.LocalDateTime;

public interface AuthenticationResponseFactory {

    void userNotFound();

    void incorrectPassword();

    UserData success();

    interface UserData {

        void writeUserName(String name);

        void writeToken(String token);

        void writeTokenExpirationDate(LocalDateTime expirationDateTime);
    }

    final class Fake implements AuthenticationResponseFactory, UserData {

        public boolean userNotFound;
        public boolean incorrectPassword;
        public String name;
        public String token;
        public LocalDateTime expirationDateTime;

        @Override
        public void userNotFound() {
            userNotFound = true;
        }

        @Override
        public void incorrectPassword() {
            incorrectPassword = true;
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
