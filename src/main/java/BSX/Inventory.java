package BSX;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Inventory")
public class Inventory {

    @XmlElement(name = "Item", type = BsxItem.class)
    private List<BsxItem> inventory = new ArrayList<>();

    public Inventory() {}

    public Inventory(List<BsxItem> books) {
        this.inventory = books;
    }

    public List<BsxItem> getInventory() {
        return inventory;
    }

    public void setInventory(List<BsxItem> inventory) {
        this.inventory = inventory;
    }
}