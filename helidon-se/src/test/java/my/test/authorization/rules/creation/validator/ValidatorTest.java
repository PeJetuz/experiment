package my.test.authorization.rules.creation.validator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidatorTest {

    @Test
    public void validatorTrueTest() {
        assertTrue(Validator.TRUE.validate());
    }

    @Test
    public void validatorFalseTest() {
        assertFalse((new Validator.FalseValidator()).validate());
    }

}
