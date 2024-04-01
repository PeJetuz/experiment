package my.test.authorization.store;

import my.test.authorization.domain.api.CreationUserResponseBuilder;
import my.test.authorization.domain.api.UserInfo;
import my.test.authorization.domain.api.store.AuthenticateUser;
import my.test.authorization.domain.api.store.NewUser;
import my.test.authorization.domain.api.store.UserFactory;


public class UserMockFactoryImpl implements UserFactory {

    @Override
    public NewUser createNewUser(UserInfo info,
            CreationUserResponseBuilder rbuilder) {
        return new NewUserImpl(new UserStoreInMemory(), info, rbuilder);
    }

    @Override
    public AuthenticateUser createAuthenticatedUser(UserInfo info, AuthenticationResponseBuilder rbuilder) {
        return new AuthenticatedUserImpl(new UserStoreInMemory(), info, rbuilder);
    }
}
