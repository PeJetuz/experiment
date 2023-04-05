package my.test.rest.incomings.eureka.rules;

public interface AuthFactory {

    LoginProcess createLoginProcess(LoginPresenter presenter, String userName, String passwordHash);

    CreateProcess createNewUserProcess(CreatePresenter presenter, String userName, String passwordHash);
}
