package my.test.authorization.rules.creation.validator;

import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmptyStringChainValidatorTest {

    @Test
    public void validateNormalValue() {
        final AtomicBoolean nextValidatorExecCounter = new AtomicBoolean(false);
        EmptyStringChainValidator subj = new EmptyStringChainValidator("random", null, () -> {
            nextValidatorExecCounter.set(true);
            return true;
        });
        assertTrue(subj.validate());
        assertTrue(nextValidatorExecCounter.get());
    }

    @Test
    public void validateNullValue() {
        final AtomicBoolean nextValidatorExecCounter = new AtomicBoolean(false);
        EmptyStringChainValidator subj = new EmptyStringChainValidator(null, () -> {
            nextValidatorExecCounter.set(true);
        }, null);
        assertFalse(subj.validate());
        assertTrue(nextValidatorExecCounter.get());
    }

    @Test
    public void validateEmptyValue() {
        final AtomicBoolean nextValidatorExecCounter = new AtomicBoolean(false);
        EmptyStringChainValidator subj = new EmptyStringChainValidator("", () -> {
            nextValidatorExecCounter.set(true);
        }, null);
        assertFalse(subj.validate());
        assertTrue(nextValidatorExecCounter.get());
    }
}
