package my.test.authorization.domain.api.store;

public interface AuthenticateUser {

    AuthenticationResult authenticate();

    enum AuthenticationResult {
        SUCCESS,
        UNSUCCESS
    }

    record Fake(AuthenticationResult authenticationResult) implements AuthenticateUser {

        @Override
        public AuthenticationResult authenticate() {
            return authenticationResult;
        }
    }
}
