package my.test.rest.incomings.eureka.controllers.presenters.create;

import my.test.rest.incomings.eureka.controllers.dto.Authentication;
import my.test.rest.incomings.eureka.rules.CreatePresenter;
import org.springframework.http.ResponseEntity;

public interface CreatePresenterModel extends CreatePresenter {

    /**
     * build model from entered data
     *
     * @return model for controller
     */
    ResponseEntity<Authentication> renderModel();
}
