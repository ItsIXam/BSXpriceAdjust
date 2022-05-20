package Entities;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Meta.
 */
public class Meta {
    private int code;
    private String message;
    private String description;

    /**
     * Instantiates a new Meta.
     */
    public Meta() {
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    @JsonProperty("code")
    public int getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    @JsonProperty("code")
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     */
    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }
}
