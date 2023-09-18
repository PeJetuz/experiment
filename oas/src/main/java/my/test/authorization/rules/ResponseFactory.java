package my.test.authorization.rules;

public interface ResponseFactory {

    AuthenticationResponsePresenter createAuthenticationResponsePresenter();

    CreationUserResponsePresenter createCreationUserResponsePresenter();

    record Fake(AuthenticationResponsePresenter createAuthenticationResponsePresenter,
                CreationUserResponsePresenter createCreationUserResponsePresenter) implements ResponseFactory {

        public Fake() {
            this(new AuthenticationResponsePresenter.Fake(), new CreationUserResponsePresenter.Fake());
        }

    }
}
