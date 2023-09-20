package my.test.authorization.rules.creation.validator;

public interface Validator {

    Validator TRUE = new TrueValidator();

    boolean validate();

    final class TrueValidator implements Validator {

        @Override
        public boolean validate() {
            return true;
        }
    }

    final class FalseValidator implements Validator {

        @Override
        public boolean validate() {
            return false;
        }
    }
}
