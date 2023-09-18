package my.test.authorization.domain.api;

import java.time.LocalDateTime;
import my.test.authorization.domain.api.AuthenticationResponseFactory.UserData;
import org.junit.jupiter.api.Test;

public class AuthenticationResponseFactoryFakeTest {

    @Test
    public void fake() {
        AuthenticationResponseFactory.Fake subj = new AuthenticationResponseFactory.Fake();
        subj.userNotFound();
        subj.incorrectPassword();
        UserData data = subj.success();
        data.writeTokenExpirationDate(LocalDateTime.now());
        data.writeUserName("name");
        data.writeToken("token");
    }
}
