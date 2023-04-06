package my.test.authorization.rules;

import java.time.LocalDateTime;

public interface CreatePresenter {

    /**
     * Write the token
     */
    void writeToken(String token);

    /**
     * Write the expiration time
     */
    void writeExpirationDateTime(LocalDateTime expirationDateTime);

    /**
     * Set presenter state that user already exists
     */
    void initUserAlreadyExistsResponseModel();

    /**
     * Set presenter state that userName is incorrect
     */
    void invalidUserNameField();

    /**
     * Set presenter state that passwordHash is incorrect
     */
    void invalidPasswordHashField();
}
