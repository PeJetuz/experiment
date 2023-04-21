package my.test.authorization.store;

import my.test.authorization.domain.api.store.LoginUser;
import my.test.authorization.domain.api.store.NewUser;
import my.test.authorization.domain.api.store.UserBuilder;


public class UserMockBuilderImpl implements UserBuilder {

    @Override
    public LoginUser createLoginUser(String userName, String passwordHash) {
        return new LoginUserMock(new UserStoreMock(), userName, passwordHash);
    }

    @Override
    public NewUser createNewUser(String userName, String passwordHash) {
        return new NewUserMock(new UserStoreMock(), userName, passwordHash);
    }

    @Override
    public LoginUser createGuestUser() {
        return LoginUserMock.createGuestUser(new UserStoreMock());
    }
}
