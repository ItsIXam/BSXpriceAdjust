package BSX;

import javax.xml.bind.annotation.*;

/**
 * The type Bsx item.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Item")
@XmlType(propOrder = {"itemId", "color", "itemTypeName", "price", "condition", "lotID", "remarks"})
//@XmlType(propOrder = { "itemId", "itemType", "color", "category", "qty", "price", "condition", "sale", "remarks", "myCost"})
public class BsxItem {

    @XmlElement(name = "ItemID")
    private String itemId;

    @XmlElement(name = "ItemTypeName")
    private String itemTypeName;

    @XmlElement(name = "ColorID")
    private int color;

    @XmlElement(name = "Price")
    private double price;

    @XmlElement(name = "Condition")
    private String condition;

    @XmlElement(name = "LotID")
    private int lotID;

    @XmlElement(name = "Remarks")
    private String remarks;

    /*
    @XmlElement(name = "CATEGORY")
    private int category;

    @XmlElement(name = "QTY")
    private int qty;

    @XmlElement(name = "SALE")
    private int sale;

    @XmlElement(name = "MYCOST")
    private double myCost;

     */

    /**
     * Instantiates a new Bsx item.
     */
    public BsxItem() {
    }

    /**
     * Instantiates a new Bsx item.
     *
     * @param lotID the lot id
     * @param price the price
     * @param color the color
     */
    public BsxItem(int lotID, double price, int color) {
        this.price = price;
        this.lotID = lotID;
        this.color = color;
    }

    /**
     * Instantiates a new Bsx item.
     *
     * @param itemId    the item id
     * @param ItemTypeName  the item type
     * @param color     the color
     * @param price     the price
     * @param condition the condition
     * @param lotID     the lot id
     * @param remarks   the remarks
     */
    public BsxItem(String itemId, String ItemTypeName, int color, double price, String condition, int lotID, String remarks) {
        this.itemId = itemId;
        this.itemTypeName = ItemTypeName;
        this.color = color;
        this.price = price;
        this.condition = condition;
        this.lotID = lotID;
        this.remarks = remarks;
    }

    /**
     * Gets item id.
     *
     * @return the item id
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * Gets item type name.
     *
     * @return the item type
     */
    public String getItemTypeName() {
        return itemTypeName;
    }

    /**
     * Sets item type name.
     *
     * @param itemTypeName the item type name
     */
    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }

    /**
     * Gets color.
     *
     * @return the color
     */
    public int getColor() {
        return color;
    }

    /**
     * Gets price.
     *
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets price.
     *
     * @param price the price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets condition.
     *
     * @return the condition
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Gets lot id.
     *
     * @return the lot id
     */
    public int getLotID() {
        return lotID;
    }

    /**
     * Gets remarks.
     *
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * Sets remarks.
     *
     * @param remarks the remarks
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
