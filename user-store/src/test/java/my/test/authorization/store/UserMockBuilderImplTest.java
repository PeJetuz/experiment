package my.test.authorization.store;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserMockBuilderImplTest {

    @Test
    public void createLoginUser() {
        UserMockBuilderImpl builder = new UserMockBuilderImpl();

        assertNotNull(builder.createLoginUser(null, null));
    }

    @Test
    public void createNewUser() {
        UserMockBuilderImpl builder = new UserMockBuilderImpl();

        assertNotNull(builder.createNewUser(null, null));
    }

    @Test
    public void createGuestUser() {
        UserMockBuilderImpl builder = new UserMockBuilderImpl();

        assertNotNull(builder.createGuestUser());
    }
}
