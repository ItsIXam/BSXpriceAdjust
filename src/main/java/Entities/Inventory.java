package Entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;


public class Inventory {


    private List<bsxItem> inventory;


    /**
     * Instantiates a new Inventory.
     *
     * @param items the books
     */
    public Inventory(List<bsxItem> items) {
        this.inventory = items;
    }

    /**
     * Gets inventory.
     *
     * @return the inventory
     */
    public List<bsxItem> getInventory() {
        return inventory;
    }

    /**
     * Sets inventory.
     *
     * @param inventory the inventory
     */
    public void setInventory(List<bsxItem> inventory) {
        this.inventory = inventory;
    }
}