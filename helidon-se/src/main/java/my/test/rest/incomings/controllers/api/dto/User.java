package my.test.rest.incomings.controllers.api.dto;

import java.util.ArrayList;
import java.util.List;



public class User   {

    private String username;
    private String firstName;
    private String lastName;
    private List<String> roles = new ArrayList<>();

    /**
     * Default constructor.
     */
    public User() {
    // JSON-B / Jackson
    }

    /**
     * Create User.
     *
     * @param username Логин пользователя
     * @param firstName Имя пользователя
     * @param lastName Фамилия пользователя
     * @param roles Роли пользователя (допустимые для использования обладающие максимальным уровнем)
     */
    public User(
        String username, 
        String firstName, 
        String lastName, 
        List<String> roles
    ) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
    }



    /**
     * Логин пользователя
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
        sb.append("class User {\n");
        
        sb.append("    username: ").append(toIndentedString(username)).append("\n");
        sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
        sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
        sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
    */
    private static String toIndentedString(Object o) {
        if (o == null) {
          return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

