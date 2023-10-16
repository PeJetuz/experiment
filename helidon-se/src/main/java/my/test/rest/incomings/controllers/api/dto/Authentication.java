package my.test.rest.incomings.controllers.api.dto;

import java.util.ArrayList;
import java.util.List;


public class Authentication {

    private Token accessToken;
    private Token refreshToken;
    private String username;
    private String firstName;
    private String lastName;
    private List<String> roles = new ArrayList<>();

    /**
     * Default constructor.
     */
    public Authentication() {
        // JSON-B / Jackson
    }

    /**
     * Create Authentication.
     *
     * @param accessToken accessToken
     * @param refreshToken refreshToken
     * @param username Логин пользователя
     * @param firstName Имя пользователя
     * @param lastName Фамилия пользователя
     * @param roles Роли пользователя (допустимые для использования обладающие максимальным уровнем)
     */
    public Authentication(
            Token accessToken,
            Token refreshToken,
            String username,
            String firstName,
            String lastName,
            List<String> roles
    ) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
    }


    /**
     * Get accessToken
     *
     * @return accessToken
     */
    public Token getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(Token accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Get refreshToken
     *
     * @return refreshToken
     */
    public Token getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(Token refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
     * Логин пользователя
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Имя пользователя
     *
     * @return firstName
     */
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Фамилия пользователя
     *
     * @return lastName
     */
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Роли пользователя (допустимые для использования обладающие максимальным уровнем)
     *
     * @return roles
     */
    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Authentication {\n");

        sb.append("    accessToken: ").append(toIndentedString(accessToken)).append("\n");
        sb.append("    refreshToken: ").append(toIndentedString(refreshToken)).append("\n");
        sb.append("    username: ").append(toIndentedString(username)).append("\n");
        sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
        sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
        sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces (except the first line).
     */
    private static String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

