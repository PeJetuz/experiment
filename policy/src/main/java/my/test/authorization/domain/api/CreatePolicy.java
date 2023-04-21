package my.test.authorization.domain.api;

import java.time.LocalDateTime;
import java.util.function.Consumer;

public interface CreatePolicy {


    void createNewUser();

    boolean isNewUserCreatedSuccessfully();

    void writeTokenAndLastRefreshDateTime(Consumer<String> token, Consumer<LocalDateTime> lastRefreshDateTime);
}
