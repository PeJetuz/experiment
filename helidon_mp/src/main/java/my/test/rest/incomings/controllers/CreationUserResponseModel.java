package my.test.rest.incomings.controllers;

import jakarta.ws.rs.core.Response;

public interface CreationUserResponseModel {

    Response renderModel();

    record Fake(Response renderModel) implements CreationUserResponseModel {

    }
}
