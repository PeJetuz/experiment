package my.test.authorization.domain.api;

public interface CreationPolicy {

    void create();

    record Fake() implements CreationPolicy {

        @Override
        public void create() {

        }
    }
}
