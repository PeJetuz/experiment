package my.test.authorization.rules;

import io.helidon.webserver.ServerResponse;

public interface ResponseFactory {

    AuthenticationResponsePresenter createAuthenticationResponsePresenter(ServerResponse response);

    CreationUserResponsePresenter createCreationUserResponsePresenter(ServerResponse response);

    record Fake(AuthenticationResponsePresenter createAuthenticationResponsePresenter,
                CreationUserResponsePresenter createCreationUserResponsePresenter) implements ResponseFactory {

        public Fake() {
            this(new AuthenticationResponsePresenter.Fake(), new CreationUserResponsePresenter.Fake());
        }

        @Override
        public AuthenticationResponsePresenter createAuthenticationResponsePresenter(ServerResponse response) {
            return createAuthenticationResponsePresenter;
        }

        @Override
        public CreationUserResponsePresenter createCreationUserResponsePresenter(ServerResponse response) {
            return createCreationUserResponsePresenter;
        }
    }
}
