package my.test.authorization.domain.impl;

import my.test.authorization.domain.api.AuthenticationPolicy;
import my.test.authorization.domain.api.UserInfo;
import my.test.authorization.domain.api.servicebus.LogonEventTransmitter;
import my.test.authorization.domain.api.store.AuthenticateUser;
import my.test.authorization.domain.api.store.AuthenticateUser.AuthenticationResult;
import my.test.authorization.domain.api.store.UserBuilder;
import org.junit.jupiter.api.Test;

public class AuthenticationPolicyImplTest {

    @Test
    public void unsuccessAuthenticate() {
        AuthenticationPolicyImpl subj = new AuthenticationPolicyImpl(
                new UserBuilder.Fake(new AuthenticateUser.Fake(AuthenticationResult.UNSUCCESS), null),
                createUserInfo(), null, null);
        subj.authenticate();
    }

    private UserInfo createUserInfo() {
        return new UserInfo(null, null);
    }

    @Test
    public void successAuthenticate() {
        AuthenticationPolicyImpl subj = new AuthenticationPolicyImpl(
                new UserBuilder.Fake(new AuthenticateUser.Fake(AuthenticationResult.SUCCESS), null),
                createUserInfo(), null, new LogonEventTransmitter.Fake());

        subj.authenticate();
    }

    @Test
    public void fake() {
        AuthenticationPolicy.Fake subj = new AuthenticationPolicy.Fake();
        subj.authenticate();
    }
}
