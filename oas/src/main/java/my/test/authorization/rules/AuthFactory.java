package my.test.authorization.rules;

public interface AuthFactory {

    LoginProcess createLoginProcess(LoginPresenter presenter, String userName, String passwordHash);

    CreateProcess createNewUserProcess(CreatePresenter presenter, String userName, String passwordHash);
}
