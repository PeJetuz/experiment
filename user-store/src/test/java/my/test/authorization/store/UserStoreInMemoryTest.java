package my.test.authorization.store;

import java.time.LocalDateTime;
import java.util.Random;
import my.test.authorization.store.UserStore.AuthInfoValue;
import org.junit.jupiter.api.Test;

import static my.test.authorization.store.UserStore.GUEST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UserStoreInMemoryTest {

    private Random random = new Random();

    @Test
    public void createUser() {
        UserStoreInMemory subj = new UserStoreInMemory();
        AuthInfoValue authInfoValue = createEmptyAuthInfoValue();

        AuthInfoValue result = subj.createUser(authInfoValue);

        assertEquals(authInfoValue, result);
    }

    private AuthInfoValue createEmptyAuthInfoValue() {
        String name = "name" + random.nextLong();
        return new AuthInfoValue(name, null, null, null);
    }

    @Test
    public void createUserAlreadyExists() {
        UserStoreInMemory subj = new UserStoreInMemory();
        AuthInfoValue authInfoValue = createEmptyAuthInfoValue();

        subj.createUser(authInfoValue);
        AuthInfoValue result = subj.createUser(authInfoValue);

        assertEquals(UserStore.USER_ALREADY_EXISTS, result);
    }

    @Test
    public void findUserByName() {
        UserStoreInMemory subj = new UserStoreInMemory();
        AuthInfoValue authInfoValue = createAuthInfoValue();

        AuthInfoValue createResult = subj.createUser(authInfoValue);
        AuthInfoValue findResult = subj.findUserByName(authInfoValue.name());

        assertEquals(authInfoValue, createResult);
        assertEquals(authInfoValue, findResult);
    }

    private AuthInfoValue createAuthInfoValue() {
        String name = "name" + random.nextLong();
        String password = "password" + random.nextLong();
        String token = "token" + random.nextLong();
        LocalDateTime expirationTime = LocalDateTime.now();
        return new AuthInfoValue(name, password, expirationTime, token);
    }


    @Test
    public void updateUserInfo() {
        UserStoreInMemory subj = new UserStoreInMemory();
        AuthInfoValue authInfoValue = createAuthInfoValue();
        AuthInfoValue updatedInfoValue = createUpdatedAuthInfoValue(authInfoValue);

        AuthInfoValue createResult = subj.createUser(authInfoValue);
        subj.updateUserInfo(updatedInfoValue);
        AuthInfoValue findResult = subj.findUserByName(authInfoValue.name());

        assertEquals(authInfoValue, createResult);
        assertEquals(updatedInfoValue, findResult);
    }

    private AuthInfoValue createUpdatedAuthInfoValue(AuthInfoValue authInfoValue) {
        return new AuthInfoValue(authInfoValue.name(), "password" + random.nextLong(),
                LocalDateTime.now().minusDays(10), "token" + random.nextLong());
    }

    @Test
    public void isGuestUserExists() {
        UserStoreInMemory subj = new UserStoreInMemory();
        assertNotEquals(UserStore.USER_NOF_FOUND, subj.findUserByName(GUEST));
    }

    @Test
    public void isVasyaUserExists() {
        UserStoreInMemory subj = new UserStoreInMemory();
        assertNotEquals(UserStore.USER_NOF_FOUND, subj.findUserByName("Vasya"));
    }

    @Test
    public void isEmptyUserExists() {
        UserStoreInMemory subj = new UserStoreInMemory();
        assertEquals(UserStore.USER_NOF_FOUND, subj.findUserByName(""));
    }
}
