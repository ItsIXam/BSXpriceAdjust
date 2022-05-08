package Config;

public enum ConfigurationProperty {
    DIRECTORY("D:/Documenten/Max/MXbricks/PriceParser/src/main/resources/"),
    CONSUMER_KEY("E1312D0B9E254849B373DDB9C044F557"),
    CONSUMER_SECRET("D6080B581F5345A2AA179C3E79779AB8"),
    TOKEN_VALUE("12C0E8EB2B464F1C852DDDA75712E776"),
    TOKEN_SECRET("233D5E2EB7D14305A974EB48DC3F273C");

    private final String propertyName;

    ConfigurationProperty(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
