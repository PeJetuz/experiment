package my.test.authorization.rules.creation.validator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidatorTest {

    @Test
    public void testTrueValidator() {
        Validator.TrueValidator subj = new Validator.TrueValidator();
        assertTrue(subj.validate());
    }

    @Test
    public void testFalseValidator() {
        Validator.FalseValidator subj = new Validator.FalseValidator();
        assertFalse(subj.validate());
    }
}
