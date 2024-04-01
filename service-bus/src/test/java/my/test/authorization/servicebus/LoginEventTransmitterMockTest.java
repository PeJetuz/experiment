package my.test.authorization.servicebus;

import org.junit.jupiter.api.Test;

public class LoginEventTransmitterMockTest {

    @Test
    public void sendUserLogonEvent() {
        LoginEventTransmitterMock subj = new LoginEventTransmitterMock(null, null, null);
        subj.sendUserLogonEvent(null);
    }

}
