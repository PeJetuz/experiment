package my.test.authorization.servicebus;

import my.test.authorization.domain.api.servicebus.LoginEventTransmitter;
import my.test.authorization.domain.api.servicebus.LoginEventTransmitterBuilder;

public class LoginEventTransmitterBuilderImpl implements LoginEventTransmitterBuilder {

    @Override
    public LoginEventTransmitter createLoginEventTransmitter(String userName) {
        return new LoginEventTransmitterImpl(userName);
    }
}
