package my.test.authorization.domain.api.store;

import java.time.LocalDateTime;
import java.util.function.Consumer;

public interface NewUser {

    boolean isUserExists();

    void createNewUser(String token);

    boolean isNewUserCreatedSuccessfully();

    void writeLastRefreshDateTime(Consumer<LocalDateTime> lastRefreshDateTime);

    void writeToken(Consumer<String> tokenConsumer);
}
