package BSX;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BrickStoreXML")
public class BrickStoreXML {

    @XmlElement(name = "Inventory", type = Inventory.class)
    private List<Inventory> BrickStoreXML = new ArrayList<>();

    public BrickStoreXML() {}

    public BrickStoreXML(List<Inventory> brickStoreXML) {
        BrickStoreXML = brickStoreXML;
    }

    public List<Inventory> getBrickStoreXML() {
        return BrickStoreXML;
    }

    public void setBrickStoreXML(List<Inventory> brickStoreXML) {
        BrickStoreXML = brickStoreXML;
    }
}