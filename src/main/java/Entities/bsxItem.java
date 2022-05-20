package Entities;

public class bsxItem {
    private final String itemID;
    private final String itemType;
    private final int color;
    private String itemTypeName;
    private final int categoryID;
    private final int qty;
    private double price;
    private final String condition;
    private final int sale;
    private final double myCost;
    private final String remarks;
    private final int lotID;
    private final String subcondition;
    private final int bulk;
    private final String description;
    private final String extended;
    private final String buyerUsername;
    private final String stockroom;
    private final String stockroomID;
    private final String retain;
    private final double myWeight;
    private final int tq1;
    private final double tp1;
    private final int tq2;
    private final double tp2;
    private final int tq3;
    private final double tp3;

    public bsxItem(String itemID, String itemTypeID, int colorID, String itemTypeName, int categoryID, int quantity, double price, String condition, int sale, double myCost, String remarks, int lotID, String subcondition, int bulk, String description, String extended, String buyerUsername, String stockroom, String stockroomID, String retain, double myWeight, int tq1, double tp1, int tq2, double tp2, int tq3, double tp3) {
        this.itemID = itemID;
        this.itemType = itemTypeID;
        this.color = colorID;
        this.itemTypeName = itemTypeName;
        this.categoryID = categoryID;
        this.qty = quantity;
        this.price = price;
        this.condition = condition;
        this.sale = sale;
        this.myCost = myCost;
        this.remarks = remarks;
        this.lotID = lotID;
        this.subcondition = subcondition;
        this.bulk = bulk;
        this.description = description;
        this.extended = extended;
        this.buyerUsername = buyerUsername;
        this.stockroom = stockroom;
        this.stockroomID = stockroomID;
        this.retain = retain;
        this.myWeight = myWeight;
        this.tq1 = tq1;
        this.tp1 = tp1;
        this.tq2 = tq2;
        this.tp2 = tp2;
        this.tq3 = tq3;
        this.tp3 = tp3;
    }

    public String getItemID() {
        return itemID;
    }

    public String getItemType() {
        return itemType;
    }

    public int getColor() {
        return color;
    }

    public String getItemTypeName() {
        return itemTypeName;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public int getQty() {
        return qty;
    }

    public double getPrice() {
        return price;
    }

    public String getCondition() {
        return condition;
    }

    public int getSale() {
        return sale;
    }

    public double getMyCost() {
        return myCost;
    }

    public String getRemarks() {
        return remarks;
    }

    public int getLotID() {
        return lotID;
    }

    public String getSubcondition() {
        return subcondition;
    }

    public int getBulk() {
        return bulk;
    }

    public String getDescription() {
        return description;
    }

    public String getExtended() {
        return extended;
    }

    public String getBuyerUsername() {
        return buyerUsername;
    }

    public String getStockroom() {
        return stockroom;
    }

    public String getStockroomID() {
        return stockroomID;
    }

    public String getRetain() {
        return retain;
    }

    public double getMyWeight() {
        return myWeight;
    }

    public int getTq1() {
        return tq1;
    }

    public double getTp1() {
        return tp1;
    }

    public int getTq2() {
        return tq2;
    }

    public double getTp2() {
        return tp2;
    }

    public int getTq3() {
        return tq3;
    }

    public double getTp3() {
        return tp3;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }
}
