package my.test.authorization.domain.api.servicebus;

/**
 * Interface for instantiating a Service Bus to send a logon event
 */
public interface LogonEventTransmitterBuilder {

    /**
     * Creates a service bus instance to dispatch a logon event
     *
     * @param userName - name of user
     */
    LogonEventTransmitter createLoginEventTransmitter(String userName);

    record Fake(LogonEventTransmitter createLoginEventTransmitter) implements LogonEventTransmitterBuilder {

        @Override
        public LogonEventTransmitter createLoginEventTransmitter(String userName) {
            return createLoginEventTransmitter;
        }
    }
}
