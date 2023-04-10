package my.test.authorization.servicebus;

import my.test.authorization.domain.api.servicebus.LoginEventTransmitter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginEventTransmitterBuilderImplTest {

    @Test
    public void createLoginEventTransmitter() {
        LoginEventTransmitterBuilderImpl subj = new LoginEventTransmitterBuilderImpl();
        assertTrue(subj.createLoginEventTransmitter(null) instanceof LoginEventTransmitter);
    }
}
