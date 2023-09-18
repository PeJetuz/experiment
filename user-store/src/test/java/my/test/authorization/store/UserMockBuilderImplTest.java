package my.test.authorization.store;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserMockBuilderImplTest {

    @Test
    public void createNewUser() {
        UserMockBuilderImpl builder = new UserMockBuilderImpl();

        assertNotNull(builder.createNewUser(null, null));
    }

    @Test
    public void createAuthenticatedUser() {
        UserMockBuilderImpl builder = new UserMockBuilderImpl();

        assertNotNull(builder.createAuthenticatedUser(null, null));
    }
}
