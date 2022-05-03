package BSX;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Item")
@XmlType(propOrder = {"remarks", "lotID"})
//@XmlType(propOrder = { "itemId", "itemType", "color", "category", "qty", "price", "condition", "sale", "remarks", "myCost"})
public class BsxItem {
    /*
    @XmlElement(name = "ITEMID")
    private String itemId;

    @XmlElement(name = "ITEMTYPE")
    private String itemType;

    @XmlElement(name = "COLOR")
    private int color;

    @XmlElement(name = "CATEGORY")
    private int category;

    @XmlElement(name = "QTY")
    private int qty;

    @XmlElement(name = "PRICE")
    private double price;

    @XmlElement(name = "CONDITION")
    private String condition;

    @XmlElement(name = "SALE")
    private int sale;

    @XmlElement(name = "MYCOST")
    private double myCost;

     */
    @XmlElement(name = "LotID")
    private int lotID;

    @XmlElement(name = "Remarks")
    private String remarks;

    public BsxItem() {
    }
//<ITEM>
//  <ITEMID>25975pb02</ITEMID>
//  <ITEMTYPE>P</ITEMTYPE>
//  <COLOR>1</COLOR>
//  <CATEGORY>943</CATEGORY>
//  <QTY>2</QTY>
//  <PRICE>1.702</PRICE>
//  <CONDITION>N</CONDITION>
//  <SALE>20</SALE>
//  <REMARKS>Z52</REMARKS>
//  <MYCOST>0.615</MYCOST>
// </ITEM>
// <ITEM>
//  <ITEMID>3710</ITEMID>
//  <ITEMTYPE>P</ITEMTYPE>
//  <COLOR>156</COLOR>
//  <CATEGORY>26</CATEGORY>
//  <QTY>9</QTY>
//  <PRICE>0.072</PRICE>
//  <CONDITION>N</CONDITION>
//  <SALE>20</SALE>
//  <REMARKS>Z53</REMARKS>
// </ITEM>
    public BsxItem(int lotID, String remarks) {
        this.lotID = lotID;
        this.remarks = remarks;
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
