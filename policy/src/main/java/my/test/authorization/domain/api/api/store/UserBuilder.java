package my.test.authorization.domain.api.api.store;

public interface UserBuilder {

    /**
     * Create implementation of store service
     *
     * @param userName - user name
     * @param passwordHash - password hash
     */
    User createUser(String userName, String passwordHash);
}
