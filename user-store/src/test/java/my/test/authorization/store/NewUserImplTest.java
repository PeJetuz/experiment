package my.test.authorization.store;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import my.test.authorization.domain.api.CreationUserResponseFactory;
import my.test.authorization.domain.api.UserInfo;
import my.test.authorization.domain.api.store.NewUser.CreationResult;
import my.test.authorization.store.UserStore.AuthInfoValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NewUserImplTest {

    private Random random = ThreadLocalRandom.current();

    @Test
    public void alreadyExists() {
        CreationUserResponseFactory.Fake factory = new CreationUserResponseFactory.Fake();
        NewUserImpl subj = new NewUserImpl(new UserStore.Fake(UserStore.USER_ALREADY_EXISTS), createEmptyUserInfo(),
                factory);

        CreationResult result = subj.create();

        assertEquals(CreationResult.UNSUCCESS, result);
        assertTrue(factory.userAlreadyExists);
    }

    private UserInfo createEmptyUserInfo() {
        return new UserInfo(null, null);
    }

    @Test
    public void success() {
        CreationUserResponseFactory.Fake factory = new CreationUserResponseFactory.Fake();
        String name = "name" + random.nextInt();
        String token = "token" + random.nextInt();
        LocalDateTime expirationTime = LocalDateTime.now();
        AuthInfoValue authInfoValue = new AuthInfoValue(name, null, expirationTime, token);
        NewUserImpl subj = new NewUserImpl(new UserStore.Fake(authInfoValue), createEmptyUserInfo(),
                factory);

        CreationResult result = subj.create();

        assertEquals(CreationResult.SUCCESS, result);
        assertEquals(name, factory.name);
        assertEquals(token, factory.token);
        assertEquals(expirationTime, factory.expirationDateTime);
    }
}
