package my.test.authorization.store;

import my.test.authorization.domain.api.api.store.User;
import my.test.authorization.domain.api.api.store.UserBuilder;


public class UserMockBuilderImpl implements UserBuilder {

    @Override
    public User createUser(String userName, String passwordHash) {
        return new UserMock(userName, passwordHash);
    }
}
