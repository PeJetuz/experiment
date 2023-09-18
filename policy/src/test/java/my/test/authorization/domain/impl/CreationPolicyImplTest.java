package my.test.authorization.domain.impl;

import my.test.authorization.domain.api.CreationPolicy;
import my.test.authorization.domain.api.CreationUserResponseFactory;
import my.test.authorization.domain.api.UserInfo;
import my.test.authorization.domain.api.store.NewUser;
import my.test.authorization.domain.api.store.NewUser.CreationResult;
import my.test.authorization.domain.api.store.UserBuilder;
import org.junit.jupiter.api.Test;

public class CreationPolicyImplTest {

    @Test
    public void unsuccess() {
        CreationPolicyImpl subj = new CreationPolicyImpl(new UserBuilder.Fake(
                null, new NewUser.Fake(CreationResult.UNSUCCESS)),
                createUserInfo(), new CreationUserResponseFactory.Fake());

        subj.create();
    }

    @Test
    public void success() {
        CreationPolicyImpl subj = new CreationPolicyImpl(new UserBuilder.Fake(
                null, new NewUser.Fake(CreationResult.SUCCESS)),
                createUserInfo(), new CreationUserResponseFactory.Fake());

        subj.create();
    }

    private UserInfo createUserInfo() {
        return new UserInfo(null, null);
    }

    @Test
    public void fake() {
        CreationPolicy.Fake fake = new CreationPolicy.Fake();
        fake.create();
    }
}
