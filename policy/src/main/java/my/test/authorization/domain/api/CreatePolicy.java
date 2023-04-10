package my.test.authorization.domain.api;

import java.time.LocalDateTime;
import java.util.function.Consumer;

public interface CreatePolicy {

    /**
     * Create new user
     *
     * @return true if the user created successful
     */
    boolean createNewUser();

    /**
     * Write token and expiration time
     *
     * @param token - token consumer
     * @param lastRefreshDateTime - login expiration time consumer
     */
    void writeTokenAndLastRefreshDateTime(Consumer<String> token, Consumer<LocalDateTime> lastRefreshDateTime);
}
