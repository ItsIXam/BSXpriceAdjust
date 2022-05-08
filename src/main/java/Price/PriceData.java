package Price;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({"min_price", "max_price", "qty_avg_price", "price_detail", "currency_code"})
public class PriceData {
    private Item item;
    private String new_or_used;
    private double avg_price;

    private int unit_quantity;

    private int total_quantity;

    public PriceData() {
    }

    @JsonProperty("item")
    public Item getItem() {
        return item;
    }

    @JsonProperty("item")
    public void setItem(Item item) {
        this.item = item;
    }

    @JsonProperty("new_or_used")
    public String getNew_or_used() {
        return new_or_used;
    }

    @JsonProperty("new_or_used")
    public void setNew_or_used(String new_or_used) {
        this.new_or_used = new_or_used;
    }

    @JsonProperty("avg_price")
    public double getAvg_price() {
        return avg_price;
    }

    @JsonProperty("avg_price")
    public void setAvg_price(double avg_price) {
        this.avg_price = avg_price;
    }

    @JsonProperty("unit_quantity")
    public double getUnit_quantity() {
        return unit_quantity;
    }

    @JsonProperty("unit_quantity")
    public void setUnit_quantity(int unit_quantity) {
        this.unit_quantity = unit_quantity;
    }

    @JsonProperty("total_quantity")
    public double getTotal_quantity() {
        return total_quantity;
    }

    @JsonProperty("total_quantity")
    public void setTotal_quantity(int total_quantity) {
        this.total_quantity = total_quantity;
    }

}
