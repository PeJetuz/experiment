package my.test.rest.incomings.eureka.rules;

import java.time.LocalDateTime;

public interface LoginPresenter {

    /**
     * Write token state
     */
    void writeToken(String token);

    /**
     * Write expiration date time
     */
    void writeExpirationDateTime(LocalDateTime expirationDateTime);

    /**
     * Set state of user not found
     */
    void initUserNotFoundResponseModel();
}
