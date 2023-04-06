package my.test.authorization.store;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserMockBuilderImplTest {

    @Test
    public void createUser() {
        UserMockBuilderImpl builder = new UserMockBuilderImpl();
        assertNotNull(builder.createUser(null, null));
    }
}
