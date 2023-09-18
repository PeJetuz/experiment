package my.test.rest.incomings.controllers;

import my.test.rest.incomings.controllers.api.dto.Authentication;
import org.springframework.http.ResponseEntity;

public interface AuthenticationResponseModel {

    ResponseEntity<Authentication> renderModel();

    record Fake(ResponseEntity<Authentication> renderModel) implements AuthenticationResponseModel {

    }
}
