package my.test.authorization.store;

import java.time.LocalDateTime;
import my.test.authorization.domain.api.CreationUserResponseBuilder;
import my.test.authorization.domain.api.CreationUserResponseBuilder.UserInfoBuilder;
import my.test.authorization.domain.api.UserInfo;
import my.test.authorization.domain.api.store.NewUser;
import my.test.authorization.domain.validator.Validator;
import my.test.authorization.store.UserStore.AuthInfoValue;

public class NewUserImpl implements NewUser {

    private final UserStore userStore;
    private final UserInfo userInfo;
    private final CreationUserResponseBuilder responseFactory;

    /**
     * Validator chain.
     */
    private final Validator validatorch;


    public NewUserImpl(UserStore userStore, UserInfo userInfo, CreationUserResponseBuilder responseFactory) {
        this.userStore = userStore;
        this.userInfo = userInfo;
        this.responseFactory = responseFactory;
        /*
        new EmptyStringChainValidator(userInfo.name(), responseBuilder::emptyName,
                        new EmptyStringChainValidator(userInfo.passwordHash(),
                                responseBuilder::emptyPassword, Validator.TRUE))
         */
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
        responseFactory.userExists();
    }

    private void buildSuccessOperation(AuthInfoValue authInfoValue) {
        UserInfoBuilder userInfoBuilder = responseFactory.success();
        userInfoBuilder.writeUserName(authInfoValue.name());
        userInfoBuilder.writeToken(authInfoValue.token());
        userInfoBuilder.writeTokenExpirationDate(authInfoValue.lastRefreshDateTime());
    }
}
