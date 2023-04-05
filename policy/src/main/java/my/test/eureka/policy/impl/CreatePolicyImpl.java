package my.test.eureka.policy.impl;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import my.test.eureka.policy.CreatePolicy;
import my.test.eureka.policy.store.User;
import my.test.eureka.policy.store.UserBuilder;

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
