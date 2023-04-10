package my.test.authorization.domain.api.store;

import java.time.LocalDateTime;
import java.util.function.Consumer;

/**
 * Interface for load and update user information
 */
public interface User {

    String GUEST = "Guest";
    String GUEST_PASSWORD_HASH = "passwordHash";

    /**
     * Validate password hash when user load without password
     *
     * @param passwordHash - password hash
     */
    boolean validatePasswordHash(String passwordHash);

    void loadUser();

    boolean isUserLoaded();

    /**
     * Update expiration date time for user
     *
     * @return - new time
     */
    LocalDateTime updateLastRefreshDateTime();

    /**
     * Check existing user
     *
     * @return if user exists
     */
    boolean isUserExists();

    /**
     * Create new user
     */
    boolean createNewUser(String token);

    /**
     * Write expiration date and time
     *
     * @param lastRefreshDateTime - consumer
     */
    void writeLastRefreshDateTime(Consumer<LocalDateTime> lastRefreshDateTime);

    /**
     * Compare expiration date time with argument
     */
    boolean isLastRefreshDateTime(LocalDateTime compareTo);

    /**
     * Update expiration date time and token
     */
    LocalDateTime updateLastRefreshDateTimeAndToken(String token);

    /**
     * Write token
     */
    void writeToken(Consumer<String> tokenConsumer);
}
