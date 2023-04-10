package my.test.authorization.domain.api.store;

public interface UserBuilder {

    /**
     * Create instance of user store service
     *
     * @param userName - name of user
     * @param passwordHash - password hash
     */
    User createUser(String userName, String passwordHash);
}
