package my.test.authorization.servicebus;

import my.test.authorization.domain.api.servicebus.LoginEventTransmitter;

public class LoginEventTransmitterImpl implements LoginEventTransmitter {

    private final String userName;

    public LoginEventTransmitterImpl(String userName) {
        this.userName = userName;
    }

    @Override
    public void sendUserLoginEvent() {

    }
}
