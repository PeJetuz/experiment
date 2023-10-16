package my.test.rest.incomings.controllers;

public interface CreationUserResponseModel {

    void send();

    final class Fake implements CreationUserResponseModel {

        @Override
        public void send() {
        }
    }
}
