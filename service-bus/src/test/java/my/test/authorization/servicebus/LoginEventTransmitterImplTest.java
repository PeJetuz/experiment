package my.test.authorization.servicebus;

import org.junit.jupiter.api.Test;

public class LoginEventTransmitterImplTest {

    @Test
    public void sendUserLoginEvent() {
        LoginEventTransmitterImpl subj = new LoginEventTransmitterImpl(null);
        subj.sendUserLoginEvent();
    }
}
