package my.test.eureka.policy.store;

import java.time.LocalDateTime;
import java.util.function.Consumer;

public interface User {

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
    LocalDateTime updateExpirationDateTime();

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
     * @param expirationDateTime - consumer
     */
    void writeExpirationDateTime(Consumer<LocalDateTime> expirationDateTime);

    /**
     * Compare expiration date time with argument
     */
    boolean isExpirationDateTimeBefore(LocalDateTime compareTo);

    /**
     * Update expiration date time and token
     */
    LocalDateTime updateExpirationDateTimeAndToken(String token);

    /**
     * Write token
     */
    void writeToken(Consumer<String> tokenConsumer);
}
