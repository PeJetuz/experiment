package my.test.authorization.domain.api;

import java.time.LocalDateTime;
import my.test.authorization.domain.api.CreationUserResponseFactory.UserData;
import org.junit.jupiter.api.Test;

public class CreationUserResponseFactoryFakeTest {

    @Test
    public void fake() {
        CreationUserResponseFactory.Fake subj = new CreationUserResponseFactory.Fake();
        subj.invalidUserNameField();
        subj.invalidPasswordHashField();
        subj.userAlreadyExists();
        UserData data = subj.success();
        data.writeTokenExpirationDate(LocalDateTime.now());
        data.writeUserName("name");
        data.writeToken("token");
    }
}
