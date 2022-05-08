package BSX.decorator;

import BSX.BsxItem;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Item")
public class BsxItemPriceDecorator {
    private BsxItem bsxItem;

    public BsxItemPriceDecorator() {
    }

    public BsxItemPriceDecorator(BsxItem bsxItem) {
        this.bsxItem = bsxItem;
    }

    @XmlElement(name = "LotId")
    public int getLotId() {
        return bsxItem.getLotID();
    }

    public void setLotId(int id) {
    }

    @XmlElement(name = "LotId")
    public double getPrice(){
        return bsxItem.getPrice();
    }

    public void setPrice(int price){

    }

}
