package my.test.authorization.domain.api;

public interface PolicyBuilder {

    /**
     * Create policy implementation
     *
     * @param userName - user name
     * @param passwordHash - password hash
     */
    LoginPolicy buildLoginPolicy(String userName, String passwordHash);

    /**
     * Create policy implementation
     *
     * @param userName - user name
     * @param passwordHash - password hash
     */
    CreatePolicy buildCreatePolicy(String userName, String passwordHash);
}
