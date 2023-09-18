package my.test.authorization.domain.api;

public interface PolicyFactory {

    AuthenticationPolicy buildAuthenticatePolicy(UserInfo userInfo, AuthenticationResponseFactory responseFactory);

    CreationPolicy buildCreationPolicy(UserInfo userInfo, CreationUserResponseFactory responseFactory);

    record Fake(AuthenticationPolicy authenticationPolicy, CreationPolicy creationPolicy) implements
            PolicyFactory {

        @Override
        public AuthenticationPolicy buildAuthenticatePolicy(UserInfo userInfo,
                AuthenticationResponseFactory responseFactory) {
            return authenticationPolicy;
        }

        @Override
        public CreationPolicy buildCreationPolicy(UserInfo userInfo, CreationUserResponseFactory responseFactory) {
            return creationPolicy;
        }
    }
}
