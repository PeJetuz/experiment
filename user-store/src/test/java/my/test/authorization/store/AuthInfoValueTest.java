package my.test.authorization.store;

import java.time.LocalDateTime;
import my.test.authorization.store.UserStore.AuthInfoValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class AuthInfoValueTest {

    @Test
    public void updateLastRefreshDateTimeTest() throws InterruptedException {
        LocalDateTime expectedTime = LocalDateTime.now();
        LocalDateTime newTime = expectedTime.plusNanos(1L);
        AuthInfoValue expected = new AuthInfoValue("name", "passwordHash", expectedTime, "token");

        AuthInfoValue actual = expected.updateLastRefreshDateTime(newTime);

        assertNotEquals(expected, actual);
        assertEquals(expected.name(), actual.name());
        assertEquals(expected.passwordHash(), actual.passwordHash());
        assertEquals(expected.token(), actual.token());
        assertEquals(expectedTime, expected.lastRefreshDateTime());
        assertEquals(newTime, actual.lastRefreshDateTime());
    }

    @Test
    public void updateLastRefreshDateTimeAndTokenTest() throws InterruptedException {
        LocalDateTime expectedTime = LocalDateTime.now();
        LocalDateTime newTime = expectedTime.plusNanos(1L);
        String expectedToken = "token";
        String newToken = "new token";
        AuthInfoValue expected = new AuthInfoValue("name", "passwordHash", expectedTime, expectedToken);

        AuthInfoValue actual = expected.updateLastRefreshDateTimeAndToken(newTime, newToken);

        assertNotEquals(expected, actual);
        assertEquals(expected.name(), actual.name());
        assertEquals(expected.passwordHash(), actual.passwordHash());
        assertEquals(expectedToken, expected.token());
        assertEquals(newToken, actual.token());
        assertEquals(expectedTime, expected.lastRefreshDateTime());
        assertEquals(newTime, actual.lastRefreshDateTime());
    }
}
