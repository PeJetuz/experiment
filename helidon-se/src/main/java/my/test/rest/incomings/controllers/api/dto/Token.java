package my.test.rest.incomings.controllers.api.dto;

import java.time.OffsetDateTime;


public class Token {

    private String value;
    private OffsetDateTime expirationDateTime;

    /**
     * Default constructor.
     */
    public Token() {
        // JSON-B / Jackson
    }

    /**
     * Create Token.
     *
     * @param value value
     * @param expirationDateTime expirationDateTime
     */
    public Token(
            String value,
            OffsetDateTime expirationDateTime
    ) {
        this.value = value;
        this.expirationDateTime = expirationDateTime;
    }


    /**
     * Get value
     *
     * @return value
     */
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Get expirationDateTime
     *
     * @return expirationDateTime
     */
    public OffsetDateTime getExpirationDateTime() {
        return expirationDateTime;
    }

    public void setExpirationDateTime(OffsetDateTime expirationDateTime) {
        this.expirationDateTime = expirationDateTime;
    }

    /**
     * Create a string representation of this pojo.
     **/
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Token {\n");

        sb.append("    value: ").append(toIndentedString(value)).append("\n");
        sb.append("    expirationDateTime: ").append(toIndentedString(expirationDateTime)).append("\n");
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

