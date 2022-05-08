package BSX;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Inventory.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Inventory")
public class Inventory {

    @XmlElement(name = "Item", type = BsxItem.class)
    private List<BsxItem> inventory = new ArrayList<>();

    /**
     * Instantiates a new Inventory.
     */
    public Inventory() {}

    /**
     * Instantiates a new Inventory.
     *
     * @param books the books
     */
    public Inventory(List<BsxItem> books) {
        this.inventory = books;
    }

    /**
     * Gets inventory.
     *
     * @return the inventory
     */
    public List<BsxItem> getInventory() {
        return inventory;
    }

    /**
     * Sets inventory.
     *
     * @param inventory the inventory
     */
    public void setInventory(List<BsxItem> inventory) {
        this.inventory = inventory;
    }
}