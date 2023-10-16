package my.test.rest.incomings.controllers;

public interface AuthenticationResponseModel {

    void send();

    final class Fake implements AuthenticationResponseModel {

        @Override
        public void send() {
        }
    }
}
