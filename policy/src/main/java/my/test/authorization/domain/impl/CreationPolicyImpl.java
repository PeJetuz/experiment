package my.test.authorization.domain.impl;

import my.test.authorization.domain.api.CreationPolicy;
import my.test.authorization.domain.api.CreationUserResponseFactory;
import my.test.authorization.domain.api.UserInfo;
import my.test.authorization.domain.api.store.NewUser;
import my.test.authorization.domain.api.store.NewUser.CreationResult;
import my.test.authorization.domain.api.store.UserBuilder;

public class CreationPolicyImpl implements CreationPolicy {

    private final UserBuilder userBuilder;
    private final UserInfo userInfo;
    private final CreationUserResponseFactory responseFactory;

    public CreationPolicyImpl(UserBuilder userBuilder, UserInfo userInfo, CreationUserResponseFactory responseFactory) {
        this.userBuilder = userBuilder;
        this.userInfo = userInfo;
        this.responseFactory = responseFactory;
    }

    @Override
    public void create() {
        NewUser user = userBuilder.createNewUser(userInfo, responseFactory);
        if (success(user)) {
            //createEventTransmitter.sendUserCreatedEvent(LocalDateTime.now().toString());
        }
    }

    private boolean success(NewUser user) {
        return user.create() == CreationResult.SUCCESS;
    }
}
