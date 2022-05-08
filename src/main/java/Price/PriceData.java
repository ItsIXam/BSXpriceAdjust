package Price;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Price data.
 */
@JsonIgnoreProperties({"min_price", "max_price", "qty_avg_price", "price_detail", "currency_code"})
public class PriceData {
    private Item item;
    private String new_or_used;
    private double avg_price;

    private int unit_quantity;

    private int total_quantity;

    /**
     * Instantiates a new Price data.
     */
    public PriceData() {
    }

    /**
     * Gets item.
     *
     * @return the item
     */
    @JsonProperty("item")
    public Item getItem() {
        return item;
    }

    /**
     * Sets item.
     *
     * @param item the item
     */
    @JsonProperty("item")
    public void setItem(Item item) {
        this.item = item;
    }

    /**
     * Gets new or used.
     *
     * @return the new or used
     */
    @JsonProperty("new_or_used")
    public String getNew_or_used() {
        return new_or_used;
    }

    /**
     * Sets new or used.
     *
     * @param new_or_used the new or used
     */
    @JsonProperty("new_or_used")
    public void setNew_or_used(String new_or_used) {
        this.new_or_used = new_or_used;
    }

    /**
     * Gets avg price.
     *
     * @return the avg price
     */
    @JsonProperty("avg_price")
    public double getAvg_price() {
        return avg_price;
    }

    /**
     * Sets avg price.
     *
     * @param avg_price the avg price
     */
    @JsonProperty("avg_price")
    public void setAvg_price(double avg_price) {
        this.avg_price = avg_price;
    }

    /**
     * Gets unit quantity.
     *
     * @return the unit quantity
     */
    @JsonProperty("unit_quantity")
    public double getUnit_quantity() {
        return unit_quantity;
    }

    /**
     * Sets unit quantity.
     *
     * @param unit_quantity the unit quantity
     */
    @JsonProperty("unit_quantity")
    public void setUnit_quantity(int unit_quantity) {
        this.unit_quantity = unit_quantity;
    }

    /**
     * Gets total quantity.
     *
     * @return the total quantity
     */
    @JsonProperty("total_quantity")
    public double getTotal_quantity() {
        return total_quantity;
    }

    /**
     * Sets total quantity.
     *
     * @param total_quantity the total quantity
     */
    @JsonProperty("total_quantity")
    public void setTotal_quantity(int total_quantity) {
        this.total_quantity = total_quantity;
    }

}
