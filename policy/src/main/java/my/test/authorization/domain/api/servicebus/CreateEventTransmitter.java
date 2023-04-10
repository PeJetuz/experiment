package my.test.authorization.domain.api.servicebus;

/**
 * Interface to send a Service Bus new user event
 */
public interface CreateEventTransmitter {

    /**
     * Dispatches a new user event to Service Bus
     */
    void sendNewUserEvent(String userName);
}
