package my.test.authorization.store;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Consumer;
import my.test.authorization.domain.api.store.NewUser;
import my.test.authorization.store.UserStore.AuthInfoValue;

public class NewUserMock implements NewUser {

    private final UserStore userStore;
    private final String userName;
    private final String passwordHash;

    private Optional<AuthInfoValue> authUserInfoValue;

    public NewUserMock(UserStore userStore, String userName, String passwordHash) {
        this.userStore = userStore;
        this.userName = userName;
        this.passwordHash = passwordHash;
        authUserInfoValue = Optional.empty();
    }

    @Override
    public boolean isUserExists() {
        return userStore.isUserExists(userName);
    }

    @Override
    public void createNewUser(String token) {
        AuthInfoValue newAuthInfoValue = new AuthInfoValue(userName, passwordHash, LocalDateTime.now(), token);
        if (userStore.createUserAndCheckSuccessOperation(newAuthInfoValue)) {
            authUserInfoValue = Optional.of(newAuthInfoValue);
        }
    }

    @Override
    public boolean isNewUserCreatedSuccessfully() {
        return authUserInfoValue.isPresent();
    }

    @Override
    public void writeLastRefreshDateTime(Consumer<LocalDateTime> lastRefreshDateTimeConsumer) {
        assert authUserInfoValue.isPresent() : "The user has not been created";
        lastRefreshDateTimeConsumer.accept(authUserInfoValue.get().lastRefreshDateTime());
    }

    @Override
    public void writeToken(Consumer<String> tokenConsumer) {
        assert authUserInfoValue.isPresent() : "The user has not been created";
        tokenConsumer.accept(authUserInfoValue.get().token());
    }

    protected Optional<AuthInfoValue> getAuthUserInfoValueForTestingOnly() {
        return authUserInfoValue;
    }

    protected void setAuthUserInfoValueForTestingOnly(Optional<AuthInfoValue> authUserInfoValue) {
        this.authUserInfoValue = authUserInfoValue;
    }
}
