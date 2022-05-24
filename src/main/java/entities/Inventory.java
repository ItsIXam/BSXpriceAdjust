package entities;

import java.util.List;

/**
 * The type Inventory.
 */
public class Inventory {

    private List<BsxItem> inventory;

    /**
     * Instantiates a new Inventory.
     *
     * @param items the books
     */
    public Inventory(List<BsxItem> items) {
        this.inventory = items;
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