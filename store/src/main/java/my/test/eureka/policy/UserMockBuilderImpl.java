package my.test.eureka.policy;

import my.test.eureka.policy.store.User;
import my.test.eureka.policy.store.UserBuilder;

public class UserMockBuilderImpl implements UserBuilder {

    @Override
    public User createUser(String userName, String passwordHash) {
        return new UserMock(userName, passwordHash);
    }
}
