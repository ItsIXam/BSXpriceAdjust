package config;

/**
 * The enum Configuration property.
 */
public enum ConfigurationProperty {
    /**
     * Directory configuration property.
     */
    DIRECTORY("D:/Documenten/Max/MXbricks/PriceParser/src/main/resources/"),
    /**
     * Consumer key configuration property.
     */
    CONSUMER_KEY("E1312D0B9E254849B373DDB9C044F557"),
    /**
     * Consumer secret configuration property.
     */
    CONSUMER_SECRET("D6080B581F5345A2AA179C3E79779AB8"),
    /**
     * Token value configuration property.
     */
    TOKEN_VALUE("12C0E8EB2B464F1C852DDDA75712E776"),
    /**
     * Token secret configuration property.
     */
    TOKEN_SECRET("233D5E2EB7D14305A974EB48DC3F273C");

    private final String propertyName;

    ConfigurationProperty(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * Gets property name.
     *
     * @return the property name
     */
    public String getPropertyName() {
        return propertyName;
    }
}
