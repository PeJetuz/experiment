package my.test.authorization.domain.api.impl;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import my.test.authorization.domain.api.api.CreatePolicy;
import my.test.authorization.domain.api.api.store.User;
import my.test.authorization.domain.api.api.store.UserBuilder;

public class CreatePolicyImpl implements CreatePolicy {

    private final User user;

    public CreatePolicyImpl(UserBuilder userBuilder, String userName, String passwordHash) {
        user = userBuilder.createUser(userName, passwordHash);
    }

    @Override
    public boolean createNewUser() {
        if (!user.isUserExists()) {
            return user.createNewUser("token");
        }
        return false;
    }

    @Override
    public void writeTokenAndExpirationDateTime(Consumer<String> token, Consumer<LocalDateTime> expirationDateTime) {
        user.writeExpirationDateTime(expirationDateTime);
        user.writeToken(token);
    }
}
