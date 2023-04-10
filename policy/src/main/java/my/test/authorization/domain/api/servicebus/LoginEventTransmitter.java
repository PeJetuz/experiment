package my.test.authorization.domain.api.servicebus;

/**
 * Interface to send a Service Bus logon event
 */
public interface LoginEventTransmitter {

    /**
     * Dispatches a logon event to Service Bus
     */
    void sendUserLoginEvent();
}
