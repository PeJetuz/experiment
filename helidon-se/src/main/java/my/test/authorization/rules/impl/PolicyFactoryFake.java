package my.test.authorization.rules.impl;

import my.test.authorization.domain.api.AuthenticationPolicy;
import my.test.authorization.domain.api.AuthenticationResponseFactory;
import my.test.authorization.domain.api.CreationPolicy;
import my.test.authorization.domain.api.CreationUserResponseFactory;
import my.test.authorization.domain.api.PolicyFactory;
import my.test.authorization.domain.api.UserInfo;

public class PolicyFactoryFake implements PolicyFactory {

    @Override
    public AuthenticationPolicy buildAuthenticatePolicy(UserInfo userInfo,
            AuthenticationResponseFactory responseFactory) {
        return null;
    }

    @Override
    public CreationPolicy buildCreationPolicy(UserInfo userInfo, CreationUserResponseFactory responseFactory) {
        return null;
    }
}
