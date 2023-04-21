package my.test.authorization.domain.impl;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import my.test.authorization.domain.api.CreatePolicy;
import my.test.authorization.domain.api.store.NewUser;
import my.test.authorization.domain.api.store.UserBuilder;

public class CreatePolicyImpl implements CreatePolicy {

    private final NewUser user;

    public CreatePolicyImpl(UserBuilder userBuilder, String userName, String passwordHash) {
        user = userBuilder.createNewUser(userName, passwordHash);
    }

    @Override
    public void createNewUser() {
        if (!user.isUserExists()) {
            user.createNewUser("token");
        }
    }

    @Override
    public boolean isNewUserCreatedSuccessfully() {
        return user.isNewUserCreatedSuccessfully();
    }

    @Override
    public void writeTokenAndLastRefreshDateTime(Consumer<String> token,
            Consumer<LocalDateTime> lastRefreshDateTime) {
        user.writeLastRefreshDateTime(lastRefreshDateTime);
        user.writeToken(token);
    }
}
