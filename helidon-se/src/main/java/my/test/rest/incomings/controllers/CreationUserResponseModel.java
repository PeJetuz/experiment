package my.test.rest.incomings.controllers;

public interface CreationUserResponseModel {

    void send();

    final class Fake implements CreationUserResponseModel {

        public int sendCount = 0;

        @Override
        public void send() {
            sendCount++;
        }
    }
}
