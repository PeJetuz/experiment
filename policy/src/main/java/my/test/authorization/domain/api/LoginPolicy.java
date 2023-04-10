package my.test.authorization.domain.api;


import java.time.LocalDateTime;
import java.util.function.Consumer;

public interface LoginPolicy {

    String GUEST = "Guest";
    String GUEST_PASSWORD_HASH = "passwordHash";

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
