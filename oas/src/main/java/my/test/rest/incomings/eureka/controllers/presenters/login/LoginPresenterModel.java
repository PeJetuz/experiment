package my.test.rest.incomings.eureka.controllers.presenters.login;

import my.test.rest.incomings.eureka.controllers.dto.Authentication;
import my.test.rest.incomings.eureka.rules.LoginPresenter;
import org.springframework.http.ResponseEntity;

public interface LoginPresenterModel extends LoginPresenter {

    /**
     * build model from entered data
     *
     * @return model for controller
     */
    ResponseEntity<Authentication> renderModel();
}
