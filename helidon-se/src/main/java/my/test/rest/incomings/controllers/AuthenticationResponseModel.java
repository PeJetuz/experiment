package my.test.rest.incomings.controllers;

public interface AuthenticationResponseModel {

    void send();

    final class Fake implements AuthenticationResponseModel {

        public int sendCount = 0;

        @Override
        public void send() {
            sendCount++;
        }
    }
}
