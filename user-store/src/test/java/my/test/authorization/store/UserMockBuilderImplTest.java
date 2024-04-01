package my.test.authorization.store;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserMockBuilderImplTest {

    @Test
    public void createNewUser() {
        UserMockFactoryImpl builder = new UserMockFactoryImpl();

        assertNotNull(builder.createNewUser(null, null));
    }

    @Test
    public void createAuthenticatedUser() {
        UserMockFactoryImpl builder = new UserMockFactoryImpl();

        assertNotNull(builder.createAuthenticatedUser(null, null));
    }
}
