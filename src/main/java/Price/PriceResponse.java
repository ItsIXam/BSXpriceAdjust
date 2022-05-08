package Price;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PriceResponse {
    private Meta meta;
    private PriceData data;

    public PriceResponse() {
    }

    @JsonProperty("meta")
    public Meta getMeta() {
        return meta;
    }

    @JsonProperty("meta")
    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    @JsonProperty("data")
    public PriceData getPriceData() {
        return data;
    }

    @JsonProperty("data")
    public void setPriceData(PriceData data) {
        this.data = data;
    }
}
