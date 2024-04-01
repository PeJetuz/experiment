package my.test.authorization.store;

import my.test.authorization.store.UserStore.Fake;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserStoreDummyTest {

    @Test
    public void findUser() {
        UserStore.Fake store = new Fake(UserStore.USER_NOF_FOUND);
        assertEquals(UserStore.USER_NOF_FOUND, store.findUserByName(null));
    }

    @Test
    public void createUser() {
        UserStore.Fake store = new Fake(UserStore.USER_ALREADY_EXISTS);
        assertEquals(UserStore.USER_ALREADY_EXISTS, store.createUser(null));
    }

    @Test
    public void update() {
        UserStore.Fake store = new Fake(UserStore.USER_NOF_FOUND);
        store.updateUserInfo(null);
    }
}
