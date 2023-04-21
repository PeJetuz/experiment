package my.test.authorization.domain.api.store;

import java.time.LocalDateTime;
import java.util.function.Consumer;

public interface LoginUser {

    void loadUser();

    boolean isUserLoaded();

    void updateLastRefreshDateTimeAndToken(String token);

    void updateLastRefreshDateTime();

    void writeLastRefreshDateTime(Consumer<LocalDateTime> lastRefreshDateTimeConsumer);

    void writeToken(Consumer<String> tokenConsumer);

    boolean isLastRefreshDateTimeBefore(LocalDateTime dateBefore);
}
