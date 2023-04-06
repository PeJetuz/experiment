package my.test.authorization.domain.api.api;

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
     * @param expirationDateTime - expiration time consumer
     */
    void writeTokenAndExpirationDateTime(Consumer<String> token, Consumer<LocalDateTime> expirationDateTime);
}
