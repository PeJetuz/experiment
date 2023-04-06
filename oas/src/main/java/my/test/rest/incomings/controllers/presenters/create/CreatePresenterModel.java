package my.test.rest.incomings.controllers.presenters.create;

import my.test.rest.incomings.controllers.api.dto.Authentication;
import my.test.authorization.rules.CreatePresenter;
import org.springframework.http.ResponseEntity;

public interface CreatePresenterModel extends CreatePresenter {

    /**
     * build model from entered data
     *
     * @return model for controller
     */
    ResponseEntity<Authentication> renderModel();
}
