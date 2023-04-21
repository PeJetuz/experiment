package my.test.authorization.store;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Consumer;
import my.test.authorization.domain.api.store.LoginUser;
import my.test.authorization.store.UserStore.AuthInfoValue;

import static my.test.authorization.store.UserStore.GUEST;
import static my.test.authorization.store.UserStore.GUEST_PASSWORD_HASH;


public class LoginUserMock implements LoginUser {

    private final UserStore userStore;
    private final String userName;
    private final String passwordHash;

    private Optional<AuthInfoValue> authUserInfoValue;

    public LoginUserMock(UserStore userStore, String userName, String passwordHash) {
        this.userStore = userStore;
        this.userName = userName;
        this.passwordHash = passwordHash;
        authUserInfoValue = Optional.empty();
    }

    public static LoginUserMock createGuestUser(UserStore userStore) {
        return new LoginUserMock(userStore, GUEST, GUEST_PASSWORD_HASH);
    }

    @Override
    public void loadUser() {
        Optional<AuthInfoValue> authInfoValue = userStore.findUserByName(userName);
        authInfoValue.ifPresent(v -> {
            if (v.passwordHash().equals(passwordHash)) {
                authUserInfoValue = authInfoValue;
            }
        });
    }

    @Override
    public boolean isUserLoaded() {
        return authUserInfoValue.isPresent();
    }

    @Override
    public void updateLastRefreshDateTimeAndToken(String token) {
        assert authUserInfoValue.isPresent() : "The user has not been loaded";
        AuthInfoValue updatedauthUserInfoValue = authUserInfoValue.get()
                .updateLastRefreshDateTimeAndToken(LocalDateTime.now(), token);
        userStore.updateUserInfo(updatedauthUserInfoValue);
        authUserInfoValue = Optional.of(updatedauthUserInfoValue);
    }

    @Override
    public void updateLastRefreshDateTime() {
        assert authUserInfoValue.isPresent() : "The user has not been loaded";
        AuthInfoValue updatedauthUserInfoValue = authUserInfoValue.get()
                .updateLastRefreshDateTime(LocalDateTime.now());
        userStore.updateUserInfo(updatedauthUserInfoValue);
        authUserInfoValue = Optional.of(updatedauthUserInfoValue);
    }

    @Override
    public void writeLastRefreshDateTime(Consumer<LocalDateTime> lastRefreshDateTimeConsumer) {
        assert authUserInfoValue.isPresent() : "The user has not been loaded";
        lastRefreshDateTimeConsumer.accept(authUserInfoValue.get().lastRefreshDateTime());
    }

    @Override
    public void writeToken(Consumer<String> tokenConsumer) {
        assert authUserInfoValue.isPresent() : "The user has not been loaded";
        tokenConsumer.accept(authUserInfoValue.get().token());
    }

    @Override
    public boolean isLastRefreshDateTimeBefore(LocalDateTime dateBefore) {
        assert authUserInfoValue.isPresent() : "The user has not been loaded";
        return authUserInfoValue.get().lastRefreshDateTime().isBefore(dateBefore);
    }

    protected String getUserNameForTestingOnly() {
        return userName;
    }

    protected String getUserPasswordHashForTestingOnly() {
        return passwordHash;
    }

    protected Optional<AuthInfoValue> getAuthUserInfoValueForTestingOnly() {
        return authUserInfoValue;
    }

    protected void setAuthUserInfoValueForTestingOnly(Optional<AuthInfoValue> authUserInfoValue) {
        this.authUserInfoValue = authUserInfoValue;
    }
}
