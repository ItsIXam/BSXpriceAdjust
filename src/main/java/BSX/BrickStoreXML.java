package BSX;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Brick store xml.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BrickStoreXML")
public class BrickStoreXML {

    @XmlElement(name = "Inventory", type = Inventory.class)
    private List<Inventory> BrickStoreXML = new ArrayList<>();

    /**
     * Instantiates a new Brick store xml.
     */
    public BrickStoreXML() {}

    /**
     * Instantiates a new Brick store xml.
     *
     * @param brickStoreXML the brick store xml
     */
    public BrickStoreXML(List<Inventory> brickStoreXML) {
        BrickStoreXML = brickStoreXML;
    }

    /**
     * Gets brick store xml.
     *
     * @return the brick store xml
     */
    public List<Inventory> getBrickStoreXML() {
        return BrickStoreXML;
    }

    /**
     * Sets brick store xml.
     *
     * @param brickStoreXML the brick store xml
     */
    public void setBrickStoreXML(List<Inventory> brickStoreXML) {
        BrickStoreXML = brickStoreXML;
    }
}