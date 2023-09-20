/*
 * Аутентификация
 * `Design First`, `Validated` 
 *
 * The version of the OpenAPI document: 1.0.0
 * Contact: my_test@mail.ru
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

package my.test.rest.incomings.controllers.api.dto;


public class AuthInfo  {
  
  private String userName;

 /**
   * MD5 hashed password
  **/
  private String passwordHash;

 /**
   * Get userName
   * @return userName
  **/
  public String getUserName() {
    return userName;
  }

  /**
    * Set userName
  **/
  public void setUserName(String userName) {
    this.userName = userName;
  }

  public AuthInfo userName(String userName) {
    this.userName = userName;
    return this;
  }

 /**
   * MD5 hashed password
   * @return passwordHash
  **/
  public String getPasswordHash() {
    return passwordHash;
  }

  /**
    * Set passwordHash
  **/
  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public AuthInfo passwordHash(String passwordHash) {
    this.passwordHash = passwordHash;
    return this;
  }


  /**
    * Create a string representation of this pojo.
  **/
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AuthInfo {\n");
    
    sb.append("    userName: ").append(toIndentedString(userName)).append("\n");
    sb.append("    passwordHash: ").append(toIndentedString(passwordHash)).append("\n");
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

