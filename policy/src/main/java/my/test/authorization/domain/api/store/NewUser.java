package my.test.authorization.domain.api.store;

public interface NewUser {

    CreationResult create();

    enum CreationResult {
        SUCCESS,
        UNSUCCESS
    }

    record Fake(CreationResult creationResult) implements NewUser {

        @Override
        public CreationResult create() {
            return creationResult;
        }
    }
}
