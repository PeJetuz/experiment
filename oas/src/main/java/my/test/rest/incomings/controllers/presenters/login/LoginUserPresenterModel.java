package my.test.rest.incomings.controllers.presenters.login;

import my.test.rest.incomings.controllers.api.dto.Authentication;
import my.test.authorization.rules.LoginUserPresenter;
import org.springframework.http.ResponseEntity;

public interface LoginUserPresenterModel extends LoginUserPresenter {

    /**
     * build model from entered data
     *
     * @return model for controller
     */
    ResponseEntity<Authentication> renderModel();
}
