package my.test.authorization.domain.api;


import java.time.LocalDateTime;
import java.util.function.Consumer;

/**
 * Interface for login user
 */
public interface LoginPolicy {


    void loginUser();

    boolean isLoginSuccess();


    void writeTokenAndLastRefreshDateTime(Consumer<String> token, Consumer<LocalDateTime> lastRefreshDateTime);
}
