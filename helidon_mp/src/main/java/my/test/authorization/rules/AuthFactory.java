package my.test.authorization.rules;

public interface AuthFactory {

    CreateUserInteractor createNewUserInteractor(String userName, String passwordHash,
            CreationUserResponsePresenter presenter);

    AuthenticateUserInteractor createAuthenticateUserInteractor(String userName, String passwordHash,
            AuthenticationResponsePresenter presenter);

    final class Fake implements AuthFactory {

        private final AuthenticateUserInteractor authenticateUserInteractor;
        private final CreateUserInteractor createUserInteractor;

        public Fake(AuthenticateUserInteractor authenticateUserInteractor, CreateUserInteractor createUserInteractor) {
            this.authenticateUserInteractor = authenticateUserInteractor;
            this.createUserInteractor = createUserInteractor;
        }

        @Override
        public CreateUserInteractor createNewUserInteractor(String userName, String passwordHash,
                CreationUserResponsePresenter presenter) {
            return createUserInteractor;
        }

        @Override
        public AuthenticateUserInteractor createAuthenticateUserInteractor(String userName, String passwordHash,
                AuthenticationResponsePresenter presenter) {
            return authenticateUserInteractor;
        }
    }
}
