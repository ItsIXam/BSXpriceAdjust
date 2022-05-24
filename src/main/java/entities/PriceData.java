package entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Price data.
 */
@JsonIgnoreProperties({"min_price", "max_price", "qty_avg_price", "price_detail", "currency_code"})
public class PriceData {

    private Item item;
    private String condition;
    private double avgPrice;
    private int unitQuantity;
    private int totalQuantity;

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
    public String getCondition() {
        return condition;
    }

    /**
     * Sets new or used.
     *
     * @param condition the new or used
     */
    @JsonProperty("new_or_used")
    public void setCondition(String condition) {
        this.condition = condition;
    }

    /**
     * Gets avg price.
     *
     * @return the avg price
     */
    @JsonProperty("avg_price")
    public double getAvgPrice() {
        return avgPrice;
    }

    /**
     * Sets avg price.
     *
     * @param avgPrice the avg price
     */
    @JsonProperty("avg_price")
    public void setAvgPrice(double avgPrice) {
        this.avgPrice = avgPrice;
    }

    /**
     * Gets unit quantity.
     *
     * @return the unit quantity
     */
    @JsonProperty("unit_quantity")
    public double getUnitQuantity() {
        return unitQuantity;
    }

    /**
     * Sets unit quantity.
     *
     * @param unitQuantity the unit quantity
     */
    @JsonProperty("unit_quantity")
    public void setUnitQuantity(int unitQuantity) {
        this.unitQuantity = unitQuantity;
    }

    /**
     * Gets total quantity.
     *
     * @return the total quantity
     */
    @JsonProperty("total_quantity")
    public double getTotalQuantity() {
        return totalQuantity;
    }

    /**
     * Sets total quantity.
     *
     * @param totalQuantity the total quantity
     */
    @JsonProperty("total_quantity")
    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

}
