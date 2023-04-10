package my.test.authorization.domain.api;

public interface PolicyBuilder {

    /**
     * Create policy instance
     *
     * @param userName - name of user
     * @param passwordHash - password hash
     */
    LoginPolicy buildLoginPolicy(String userName, String passwordHash);

    /**
     * Create policy instance
     *
     * @param userName - name of user
     * @param passwordHash - password hash
     */
    CreatePolicy buildCreatePolicy(String userName, String passwordHash);
}
