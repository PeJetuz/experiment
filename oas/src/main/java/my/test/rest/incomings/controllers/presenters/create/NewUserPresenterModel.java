package my.test.rest.incomings.controllers.presenters.create;

import my.test.rest.incomings.controllers.api.dto.Authentication;
import my.test.authorization.rules.NewUserPresenter;
import org.springframework.http.ResponseEntity;

public interface NewUserPresenterModel extends NewUserPresenter {

    /**
     * build model from entered data
     *
     * @return model for controller
     */
    ResponseEntity<Authentication> renderModel();
}
