package my.test.rest.incomings.controllers.api.dto;


public class TokenPair   {

    private Token accessToken;
    private Token refreshToken;

    /**
     * Default constructor.
     */
    public TokenPair() {
    // JSON-B / Jackson
    }

    /**
     * Create TokenPair.
     *
     * @param accessToken accessToken
     * @param refreshToken refreshToken
     */
    public TokenPair(
        Token accessToken, 
        Token refreshToken
    ) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }



    /**
     * Get accessToken
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
     * @return refreshToken
     */
    public Token getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(Token refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
      * Create a string representation of this pojo.
    **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class TokenPair {\n");
        
        sb.append("    accessToken: ").append(toIndentedString(accessToken)).append("\n");
        sb.append("    refreshToken: ").append(toIndentedString(refreshToken)).append("\n");
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

