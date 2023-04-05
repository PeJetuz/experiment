package my.test.eureka.policy;

import java.time.LocalDateTime;
import java.util.Random;
import org.junit.jupiter.api.Test;

import static my.test.eureka.policy.LoginPolicy.GUEST;
import static my.test.eureka.policy.LoginPolicy.GUEST_PASSWORD_HASH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserMockTest {

    @Test
    public void validatePasswordHash() {
        String name = "Vasya";
        String passwordHash = "passwordHash";
        UserMock user = new UserMock(name);
        assertTrue(user.validatePasswordHash(passwordHash));
        assertFalse(user.validatePasswordHash(passwordHash + passwordHash));
    }

    @Test
    public void validatePasswordHashNotFound() {
        String name = "Vasya";
        UserMock user = new UserMock(name);
        user.loadUser();
        assertFalse(user.validatePasswordHash(null));
    }

    @Test
    public void validatePasswordHashWOUser() {
        UserMock user = new UserMock(null, null);
        assertFalse(user.validatePasswordHash(null));
    }

    @Test
    public void isUserLoaded() {
        String name = "Vasya";
        String passwordHash = "passwordHash";
        UserMock user = new UserMock(name, passwordHash);
        user.loadUser();
        assertTrue(user.isUserLoaded());
    }

    @Test
    public void createGuestUser() {
        UserMock user = new UserMock(null);
        assertEquals(GUEST, user.whatIsYourName());
        assertEquals(GUEST_PASSWORD_HASH, user.whatIsYourPasswordHash());
    }

    @Test
    public void isUserLoadedNotFound() {
        String name = "Vasya";
        UserMock user = new UserMock(name, null);
        user.loadUser();
        assertFalse(user.isUserLoaded());
    }

    @Test
    public void isGuestUserLoaded() {
        UserMock user = new UserMock(GUEST, GUEST_PASSWORD_HASH);
        user.loadUser();
        assertTrue(user.isUserLoaded());
    }

    @Test
    public void emptyUserLoaded() {
        UserMock user = new UserMock("", "");
        user.loadUser();
        assertFalse(user.isUserLoaded());
    }

    @Test
    public void isUserExists() {
        String name = "Vasya";
        String passwordHash = "passwordHash";
        UserMock user = new UserMock(name, passwordHash);
        assertTrue(user.isUserExists());
    }

    @Test
    public void createNewUser() {
        String name = "Vasya1";
        String passwordHash = "passwordHash1";
        UserMock user = new UserMock(name, passwordHash);
        assertFalse(user.isUserExists());
        assertTrue(user.createNewUser(null));
        assertTrue(user.isUserLoaded());
    }

    @Test
    public void createNewUserFailed() {
        String name = "Vasya";
        String passwordHash = "passwordHash";
        UserMock user = new UserMock(name, passwordHash);
        assertFalse(user.createNewUser(null));
    }

    @Test
    public void updateExpirationDateTime() throws InterruptedException {
        String name = "Vasya";
        String passwordHash = "passwordHash";
        UserMock user = new UserMock(name);
        LocalDateTimeConsumer localDateTimeConsumer = new LocalDateTimeConsumer();
        assertTrue(user.validatePasswordHash(passwordHash));
        LocalDateTime expectedTime = user.updateExpirationDateTime();
        Thread.sleep(1L);
        LocalDateTime updateLastUpdateTime = user.updateExpirationDateTime();
        user.writeExpirationDateTime(localDateTimeConsumer::setLocalDateTime);
        assertTrue(expectedTime.isBefore(updateLastUpdateTime));
        assertEquals(updateLastUpdateTime, localDateTimeConsumer.localDateTime);
    }

    @Test
    public void updateExpirationDateTimeAndToken() throws InterruptedException {
        Random random = new Random();
        String name = "Vasya";
        String passwordHash = "passwordHash";
        String token1 = "" + random.nextLong();
        String token2 = "" + random.nextLong();
        UserMock user = new UserMock(name);
        StringConsumer tokenConsumer = new StringConsumer();
        assertTrue(user.validatePasswordHash(passwordHash));
        LocalDateTime expectedTime = user.updateExpirationDateTimeAndToken(token1);
        user.writeToken(tokenConsumer::setString);
        assertEquals(token1, tokenConsumer.string);
        Thread.sleep(1L);
        LocalDateTime updateLastUpdateTime = user.updateExpirationDateTimeAndToken(token2);
        user.writeToken(tokenConsumer::setString);
        assertEquals(token2, tokenConsumer.string);
        assertTrue(expectedTime.isBefore(updateLastUpdateTime));
    }

    @Test
    public void isExpirationDateTimeBefore() {
        String name = "Vasya";
        UserMock user = new UserMock(name);
        LocalDateTimeConsumer localDateTimeConsumer = new LocalDateTimeConsumer();
        user.writeExpirationDateTime(localDateTimeConsumer::setLocalDateTime);
        LocalDateTime expirationDateTime = localDateTimeConsumer.localDateTime;
        assertTrue(user.isExpirationDateTimeBefore(expirationDateTime.plusNanos(1)));
        assertFalse(user.isExpirationDateTimeBefore(expirationDateTime));
    }

    private class StringConsumer {

        public String string;

        public void setString(String string) {
            this.string = string;
        }
    }

    private class LocalDateTimeConsumer {

        public LocalDateTime localDateTime;

        public void setLocalDateTime(LocalDateTime localDateTime) {
            this.localDateTime = localDateTime;
        }
    }
}
