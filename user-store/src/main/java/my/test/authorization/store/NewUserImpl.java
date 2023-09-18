package my.test.authorization.store;

import java.time.LocalDateTime;
import my.test.authorization.domain.api.CreationUserResponseFactory;
import my.test.authorization.domain.api.CreationUserResponseFactory.UserData;
import my.test.authorization.domain.api.UserInfo;
import my.test.authorization.domain.api.store.NewUser;
import my.test.authorization.store.UserStore.AuthInfoValue;

public class NewUserImpl implements NewUser {

    private final UserStore userStore;
    private final UserInfo userInfo;
    private final CreationUserResponseFactory responseFactory;

    public NewUserImpl(UserStore userStore, UserInfo userInfo, CreationUserResponseFactory responseFactory) {
        this.userStore = userStore;
        this.userInfo = userInfo;
        this.responseFactory = responseFactory;
    }

    @Override
    public CreationResult create() {
        AuthInfoValue authInfoValue = userStore.createUser(new AuthInfoValue(userInfo.name(),
                userInfo.passwordHash(), LocalDateTime.now(), geterateToken()));
        if (userAlreadyExists(authInfoValue)) {
            buildUserAlreadyExists();
            return CreationResult.UNSUCCESS;
        } else {
            buildSuccessOperation(authInfoValue);
            return CreationResult.SUCCESS;
        }
    }

    private String geterateToken() {
        return userInfo.name() + LocalDateTime.now();
    }

    private boolean userAlreadyExists(AuthInfoValue authInfoValue) {
        return authInfoValue == UserStore.USER_ALREADY_EXISTS;
    }

    private void buildUserAlreadyExists() {
        responseFactory.userAlreadyExists();
    }

    private void buildSuccessOperation(AuthInfoValue authInfoValue) {
        UserData userDataBuilder = responseFactory.success();
        userDataBuilder.writeUserName(authInfoValue.name());
        userDataBuilder.writeToken(authInfoValue.token());
        userDataBuilder.writeTokenExpirationDate(authInfoValue.lastRefreshDateTime());
    }
}
