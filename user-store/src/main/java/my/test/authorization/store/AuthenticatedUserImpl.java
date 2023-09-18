package my.test.authorization.store;

import java.time.LocalDateTime;
import my.test.authorization.domain.api.AuthenticationResponseFactory;
import my.test.authorization.domain.api.AuthenticationResponseFactory.UserData;
import my.test.authorization.domain.api.UserInfo;
import my.test.authorization.domain.api.store.AuthenticateUser;
import my.test.authorization.store.UserStore.AuthInfoValue;

public class AuthenticatedUserImpl implements AuthenticateUser {

    private static final int TOKEN_EXPIRATION_INTERVAL_MINUTES = 10;

    private final UserStore userStore;
    private final UserInfo userInfo;
    private final AuthenticationResponseFactory responseFactory;

    public AuthenticatedUserImpl(UserStore userStore, UserInfo userInfo,
            AuthenticationResponseFactory responseFactory) {
        this.userStore = userStore;
        this.userInfo = userInfo;
        this.responseFactory = responseFactory;
    }

    @Override
    public AuthenticationResult authenticate() {
        AuthInfoValue authInfoValue = userStore.findUserByName(userInfo.name());
        if (userFound(authInfoValue)) {
            if (passwordCorrect(authInfoValue)) {
                authInfoValue = refreshLastAccessTime(authInfoValue);
                buildSuccessOperation(authInfoValue);
                return AuthenticationResult.SUCCESS;
            } else {
                buildIncorrectPassword();
            }
        } else {
            buildUserNotFound();
        }

        return AuthenticationResult.UNSUCCESS;
    }

    private boolean userFound(AuthInfoValue authInfoValue) {
        return authInfoValue != UserStore.USER_NOF_FOUND;
    }

    private boolean passwordCorrect(AuthInfoValue authInfoValue) {
        return authInfoValue.passwordHash().equals(userInfo.passwordHash());
    }

    private AuthInfoValue refreshLastAccessTime(AuthInfoValue authInfoValue) {
        if (expired(authInfoValue.lastRefreshDateTime())) {
            authInfoValue = authInfoValue.updateLastRefreshDateTimeAndToken(LocalDateTime.now(), geterateToken());
        } else {
            authInfoValue = authInfoValue.updateLastRefreshDateTime(LocalDateTime.now());
        }
        userStore.updateUserInfo(authInfoValue);
        return authInfoValue;
    }

    private boolean expired(LocalDateTime lastRefreshDateTime) {
        return lastRefreshDateTime.plusMinutes(TOKEN_EXPIRATION_INTERVAL_MINUTES).isBefore(LocalDateTime.now());
    }

    private String geterateToken() {
        return userInfo.name() + LocalDateTime.now();
    }


    private void buildSuccessOperation(AuthInfoValue authInfoValue) {
        UserData userDataBuilder = responseFactory.success();
        userDataBuilder.writeUserName(authInfoValue.name());
        userDataBuilder.writeToken(authInfoValue.token());
        userDataBuilder.writeTokenExpirationDate(authInfoValue.lastRefreshDateTime());
    }

    private void buildIncorrectPassword() {
        responseFactory.incorrectPassword();
    }


    private void buildUserNotFound() {
        responseFactory.userNotFound();
    }
}
