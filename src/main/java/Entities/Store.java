package Entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Brick store xml.
 */

public class Store {


    private List<Inventory> store;

    /**
     * Instantiates a new Brick store xml.
     *
     */
    public Store(List<Inventory> store) {
        this.store = store;
    }

    /**
     * Gets brick store xml.
     *
     * @return the brick store xml
     */
    public List<Inventory> getStore() {
        return store;
    }

    public void setStore(List<Inventory> store) {
        this.store = store;
    }
}