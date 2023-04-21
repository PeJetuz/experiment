package my.test.authorization.domain.api.store;

public interface UserBuilder {

    LoginUser createLoginUser(String userName, String passwordHash);

    NewUser createNewUser(String userName, String passwordHash);

    LoginUser createGuestUser();
}
