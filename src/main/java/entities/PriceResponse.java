package entities;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Price response.
 */
public class PriceResponse {

    private Meta meta;
    private PriceData data;

    /**
     * Instantiates a new Price response.
     */
    public PriceResponse() {
    }

    /**
     * Gets meta.
     *
     * @return the meta
     */
    @JsonProperty("meta")
    public Meta getMeta() {
        return meta;
    }

    /**
     * Sets meta.
     *
     * @param meta the meta
     */
    @JsonProperty("meta")
    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    /**
     * Gets price data.
     *
     * @return the price data
     */
    @JsonProperty("data")
    public PriceData getPriceData() {
        return data;
    }

    /**
     * Sets price data.
     *
     * @param data the data
     */
    @JsonProperty("data")
    public void setPriceData(PriceData data) {
        this.data = data;
    }
}
