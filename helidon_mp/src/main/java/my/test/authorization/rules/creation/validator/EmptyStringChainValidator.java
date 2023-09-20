package my.test.authorization.rules.creation.validator;

public class EmptyStringChainValidator implements Validator {

    private final String subject;
    private final Runnable action;
    private final Validator next;

    public EmptyStringChainValidator(String subject, Runnable action, Validator next) {
        this.subject = subject;
        this.action = action;
        this.next = next;
    }

    @Override
    public boolean validate() {
        if (subject == null || subject.isBlank()) {
            action.run();
            return false;
        }
        return next.validate();
    }
}
