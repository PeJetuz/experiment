package my.test.authorization.servicebus;

import org.junit.jupiter.api.Test;

public class LogonEventTransmitterMockTest {

    @Test
    public void sendUserLogonEvent() {
        LogonEventTransmitterMock subj = new LogonEventTransmitterMock(null, null, null);
        subj.sendUserLogonEvent(null);
    }

}
