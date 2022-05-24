package entities;

/**
 * The type Bsx item.
 */
public class BsxItem {

    private final String itemId;
    private final String itemType;
    private final int color;
    private String itemTypeName;
    private final int categoryId;
    private final int qty;
    private double price;
    private final String condition;
    private final int sale;
    private final double myCost;
    private final String remarks;
    private final int lotId;
    private final String subCondition;
    private final int bulk;
    private final String description;
    private final String extended;
    private final String buyerUsername;
    private final String stockroom;
    private final String stockroomId;
    private final String retain;
    private final double myWeight;
    private final int tq1;
    private final double tp1;
    private final int tq2;
    private final double tp2;
    private final int tq3;
    private final double tp3;

    /**
     * Instantiates a new Bsx item.
     *
     * @param itemId        the item id
     * @param itemTypeId    the item type id
     * @param colorId       the color id
     * @param itemTypeName  the item type name
     * @param categoryId    the category id
     * @param quantity      the quantity
     * @param price         the price
     * @param condition     the condition
     * @param sale          the sale
     * @param myCost        the my cost
     * @param remarks       the remarks
     * @param lotId         the lot id
     * @param subCondition  the sub condition
     * @param bulk          the bulk
     * @param description   the description
     * @param extended      the extended
     * @param buyerUsername the buyer username
     * @param stockroom     the stockroom
     * @param stockroomId   the stockroom id
     * @param retain        the retain
     * @param myWeight      the my weight
     * @param tq1           the tq 1
     * @param tp1           the tp 1
     * @param tq2           the tq 2
     * @param tp2           the tp 2
     * @param tq3           the tq 3
     * @param tp3           the tp 3
     */
    public BsxItem(
        String itemId, String itemTypeId, int colorId, String itemTypeName, int categoryId,
        int quantity, double price, String condition, int sale, double myCost, String remarks,
        int lotId, String subCondition, int bulk, String description, String extended,
        String buyerUsername, String stockroom, String stockroomId, String retain, double myWeight,
        int tq1, double tp1, int tq2, double tp2, int tq3, double tp3
    ) {
        this.itemId = itemId;
        this.itemType = itemTypeId;
        this.color = colorId;
        this.itemTypeName = itemTypeName;
        this.categoryId = categoryId;
        this.qty = quantity;
        this.price = price;
        this.condition = condition;
        this.sale = sale;
        this.myCost = myCost;
        this.remarks = remarks;
        this.lotId = lotId;
        this.subCondition = subCondition;
        this.bulk = bulk;
        this.description = description;
        this.extended = extended;
        this.buyerUsername = buyerUsername;
        this.stockroom = stockroom;
        this.stockroomId = stockroomId;
        this.retain = retain;
        this.myWeight = myWeight;
        this.tq1 = tq1;
        this.tp1 = tp1;
        this.tq2 = tq2;
        this.tp2 = tp2;
        this.tq3 = tq3;
        this.tp3 = tp3;
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
     * Gets item type.
     *
     * @return the item type
     */
    public String getItemType() {
        return itemType;
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
     * Gets item type name.
     *
     * @return the item type name
     */
    public String getItemTypeName() {
        return itemTypeName;
    }

    /**
     * Gets category id.
     *
     * @return the category id
     */
    public int getCategoryId() {
        return categoryId;
    }

    /**
     * Gets qty.
     *
     * @return the qty
     */
    public int getQty() {
        return qty;
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
     * Gets condition.
     *
     * @return the condition
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Gets sale.
     *
     * @return the sale
     */
    public int getSale() {
        return sale;
    }

    /**
     * Gets my cost.
     *
     * @return the my cost
     */
    public double getMyCost() {
        return myCost;
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
     * Gets lot id.
     *
     * @return the lot id
     */
    public int getLotId() {
        return lotId;
    }

    /**
     * Gets sub condition.
     *
     * @return the sub condition
     */
    public String getSubCondition() {
        return subCondition;
    }

    /**
     * Gets bulk.
     *
     * @return the bulk
     */
    public int getBulk() {
        return bulk;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets extended.
     *
     * @return the extended
     */
    public String getExtended() {
        return extended;
    }

    /**
     * Gets buyer username.
     *
     * @return the buyer username
     */
    public String getBuyerUsername() {
        return buyerUsername;
    }

    /**
     * Gets stockroom.
     *
     * @return the stockroom
     */
    public String getStockroom() {
        return stockroom;
    }

    /**
     * Gets stockroom id.
     *
     * @return the stockroom id
     */
    public String getStockroomId() {
        return stockroomId;
    }

    /**
     * Gets retain.
     *
     * @return the retain
     */
    public String getRetain() {
        return retain;
    }

    /**
     * Gets my weight.
     *
     * @return the my weight
     */
    public double getMyWeight() {
        return myWeight;
    }

    /**
     * Gets tq 1.
     *
     * @return the tq 1
     */
    public int getTq1() {
        return tq1;
    }

    /**
     * Gets tp 1.
     *
     * @return the tp 1
     */
    public double getTp1() {
        return tp1;
    }

    /**
     * Gets tq 2.
     *
     * @return the tq 2
     */
    public int getTq2() {
        return tq2;
    }

    /**
     * Gets tp 2.
     *
     * @return the tp 2
     */
    public double getTp2() {
        return tp2;
    }

    /**
     * Gets tq 3.
     *
     * @return the tq 3
     */
    public int getTq3() {
        return tq3;
    }

    /**
     * Gets tp 3.
     *
     * @return the tp 3
     */
    public double getTp3() {
        return tp3;
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
     * Sets item type name.
     *
     * @param itemTypeName the item type name
     */
    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }
}
