package my.test.authorization.store;

import my.test.authorization.domain.api.AuthenticationResponseFactory;
import my.test.authorization.domain.api.CreationUserResponseFactory;
import my.test.authorization.domain.api.UserInfo;
import my.test.authorization.domain.api.store.AuthenticateUser;
import my.test.authorization.domain.api.store.UserBuilder;


public class UserMockBuilderImpl implements UserBuilder {

    @Override
    public my.test.authorization.domain.api.store.NewUser createNewUser(UserInfo userInfo,
            CreationUserResponseFactory responseFactory) {
        return new NewUserImpl(new UserStoreInMemory(), userInfo, responseFactory);
    }

    @Override
    public AuthenticateUser createAuthenticatedUser(UserInfo userInfo, AuthenticationResponseFactory responseFactory) {
        return new AuthenticatedUserImpl(new UserStoreInMemory(), userInfo, responseFactory);
    }
}
