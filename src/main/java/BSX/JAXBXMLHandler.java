package BSX;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * The type Jaxbxml handler.
 */
public class JAXBXMLHandler {


    /**
     * Marshal.
     *
     * @param inventory list of BsxItem
     * @param selectedFile the file name it needs to wright to
     * @throws IOException   the io exception
     * @throws JAXBException the jaxb exception
     */
    public static void marshal(List<BsxItem> inventory, File selectedFile)
            throws IOException, JAXBException {
        JAXBContext context;
        BufferedWriter writer = null;
        writer = new BufferedWriter(new FileWriter(selectedFile));
        context = JAXBContext.newInstance(BrickStoreXML.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        m.marshal(new Inventory(inventory), writer);
        writer.close();
    }

    /**
     * Unmarshal list.
     *
     * @param importFile the import file
     * @return List of inventory items
     * @throws JAXBException the jaxb exception
     */
    public static List<Inventory> unmarshal(File importFile) throws JAXBException {
        BrickStoreXML store = new BrickStoreXML();

        JAXBContext context = JAXBContext.newInstance(BrickStoreXML.class);
        Unmarshaller um = context.createUnmarshaller();
        store = (BrickStoreXML) um.unmarshal(importFile);

        return store.getBrickStoreXML();
    }
}
