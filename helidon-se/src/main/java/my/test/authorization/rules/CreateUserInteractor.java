package my.test.authorization.rules;

import my.test.rest.incomings.controllers.CreationUserResponseModel;

/**
 * Interface to creation a user from a controller
 */
public interface CreateUserInteractor {

    /**
     * Create new user
     *
     * @return true if the user created successful
     */
    CreationUserResponseModel createNewUserAndGetPresenter();

    record Fake(CreationUserResponseModel createNewUserAndGetPresenter) implements CreateUserInteractor {

    }
}
