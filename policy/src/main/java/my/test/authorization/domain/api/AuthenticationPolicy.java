package my.test.authorization.domain.api;

public interface AuthenticationPolicy {

    void authenticate();

    record Fake() implements AuthenticationPolicy {

        @Override
        public void authenticate() {

        }
    }
}
