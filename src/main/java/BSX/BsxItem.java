package BSX;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Item")
@XmlType(propOrder = {"itemId", "itemType", "color", "price", "condition", "lotID", "remarks"})
//@XmlType(propOrder = { "itemId", "itemType", "color", "category", "qty", "price", "condition", "sale", "remarks", "myCost"})
public class BsxItem {

    @XmlElement(name = "ItemID")
    private String itemId;

    @XmlElement(name = "ItemTypeID")
    private String itemType;

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

    public BsxItem() {
    }
    public BsxItem(int lotID, double price, int color) {
        this.price = price;
        this.lotID = lotID;
        this.color = color;
    }
    public BsxItem(String itemId, String itemType, int color, double price, String condition, int lotID, String remarks) {
        this.itemId = itemId;
        this.itemType = itemType;
        this.color = color;
        this.price = price;
        this.condition = condition;
        this.lotID = lotID;
        this.remarks = remarks;
    }

    public String getItemId() {
        return itemId;
    }

    public String getItemType() {
        return itemType;
    }

    public int getColor() {
        return color;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCondition() {
        return condition;
    }

    public int getLotID() {
        return lotID;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
