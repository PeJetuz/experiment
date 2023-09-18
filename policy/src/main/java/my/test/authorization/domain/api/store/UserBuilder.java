package my.test.authorization.domain.api.store;

import my.test.authorization.domain.api.AuthenticationResponseFactory;
import my.test.authorization.domain.api.CreationUserResponseFactory;
import my.test.authorization.domain.api.UserInfo;

public interface UserBuilder {

    NewUser createNewUser(UserInfo userInfo, CreationUserResponseFactory responseFactory);

    AuthenticateUser createAuthenticatedUser(UserInfo userInfo, AuthenticationResponseFactory responseFactory);

    record Fake(AuthenticateUser authenticateUser, NewUser newUser) implements UserBuilder {

        @Override
        public NewUser createNewUser(UserInfo userInfo, CreationUserResponseFactory responseFactory) {
            return newUser;
        }

        @Override
        public AuthenticateUser createAuthenticatedUser(UserInfo userInfo,
                AuthenticationResponseFactory responseFactory) {
            return authenticateUser;
        }
    }
}
