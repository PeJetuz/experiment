package my.test.authorization.rules;

/**
 * Interface to create a user from a controller
 */
public interface NewUserInteractor {

    /**
     * Create new user
     *
     * @return true if the user created successful
     */
    boolean createNewUser();
}
