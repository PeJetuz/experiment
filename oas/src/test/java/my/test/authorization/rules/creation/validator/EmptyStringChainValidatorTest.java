package my.test.authorization.rules.creation.validator;

import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmptyStringChainValidatorTest {

    @Test
    public void validateNullString() {
        AtomicBoolean action = new AtomicBoolean(false);
        EmptyStringChainValidator subj = new EmptyStringChainValidator(null, () -> {
            action.set(true);
        }, null);

        assertFalse(subj.validate());
        assertTrue(action.get());
    }

    @Test
    public void validateEmptyString() {
        AtomicBoolean action = new AtomicBoolean(false);
        EmptyStringChainValidator subj = new EmptyStringChainValidator("", () -> {
            action.set(true);
        }, null);

        assertFalse(subj.validate());
        assertTrue(action.get());
    }

    @Test
    public void validateString() {
        AtomicBoolean action = new AtomicBoolean(false);
        EmptyStringChainValidator subj = new EmptyStringChainValidator("any", () -> {
            action.set(true);
        }, Validator.TRUE);

        assertTrue(subj.validate());
        assertFalse(action.get());
    }
}
