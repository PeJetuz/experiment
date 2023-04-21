package my.test.authorization.rules;

public interface AuthFactory {

    LoginUserInteractor createLoginUserInteractor(LoginUserPresenter presenter, String userName, String passwordHash);

    NewUserInteractor createNewUserProcess(NewUserPresenter presenter, String userName, String passwordHash);
}
