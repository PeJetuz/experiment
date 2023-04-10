package my.test.authorization.domain.api.servicebus;

/**
 * Interface for instantiating a Service Bus to send a login event
 */
public interface LoginEventTransmitterBuilder {

    /**
     * Creates a service bus instance to dispatch a login event
     *
     * @param userName - name of user
     */
    LoginEventTransmitter createLoginEventTransmitter(String userName);
}
