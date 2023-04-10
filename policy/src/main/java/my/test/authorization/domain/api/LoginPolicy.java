package my.test.authorization.domain.api;


import java.time.LocalDateTime;
import java.util.function.Consumer;

/**
 * Interface for login user
 */
public interface LoginPolicy {


    /**
     * It's implement business logic login process
     */
    void loginUser();

    /**
     * check user login
     *
     * @return true if the user loaded successfully
     */
    boolean isLoginSuccess();


    /**
     * Write token and expiration time
     *
     * @param token - token consumer
     * @param lastRefreshDateTime - expiration time consumer
     */
    void writeTokenAndLastRefreshDateTime(Consumer<String> token, Consumer<LocalDateTime> lastRefreshDateTime);
}
