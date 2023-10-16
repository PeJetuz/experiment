package my.test.rest.incomings.controllers.api;

import my.test.rest.incomings.controllers.api.dto.AuthInfo;
import io.helidon.webserver.Handler;

import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import io.helidon.webserver.Service;

public interface AuthenticationService extends Service { 

    /**
     * A service registers itself by updating the routing rules.
     * @param rules the routing rules.
     */
    @Override
    default void update(Routing.Rules rules) {
        rules.put("/create", Handler.create(AuthInfo.class, this::create));
        rules.post("/login", Handler.create(AuthInfo.class, this::login));
        rules.get("/logout", this::logout);
        rules.get("/tokens/refresh", this::refreshTokens);
    }


    /**
     * PUT /create : Создание пользователя.
     * @param request the server request
     * @param response the server response
     * @param authInfo authInfo 
     */
    void create(ServerRequest request, ServerResponse response, AuthInfo authInfo);

    /**
     * POST /login : Логин пользователя.
     * @param request the server request
     * @param response the server response
     * @param authInfo authInfo 
     */
    void login(ServerRequest request, ServerResponse response, AuthInfo authInfo);

    /**
     * GET /logout : Логаут пользователя.
     * @param request the server request
     * @param response the server response
     */
    void logout(ServerRequest request, ServerResponse response);

    /**
     * GET /tokens/refresh : Обновление пары токенов.
     * @param request the server request
     * @param response the server response
     */
    void refreshTokens(ServerRequest request, ServerResponse response);

}
