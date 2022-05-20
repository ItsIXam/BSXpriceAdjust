package Entities;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Jaxbxml handler.
 */
public class XMLhelper {


    /**
     * Marshal.
     *
     * @param selectedFile the file name it needs to wright to
     */
    public static void updateMarshal(Store store, File selectedFile) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setNamespaceAware(true);
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            // root element
            Element rootElement = doc.createElement("INVENTORY");
            doc.appendChild(rootElement);

            for (bsxItem item : store.getStore().get(0).getInventory()) {
                if(item.getLotID() == 0) { throw new IllegalArgumentException("This file misses lotIDs for one or more items, therefore it cannot be updated, because it was not uploaded");}

                // ITEM element
                Element itemElement = doc.createElement("ITEM");
                rootElement.appendChild(itemElement);

                // LOTID element
                Element lotID = doc.createElement("LOTID");
                lotID.appendChild(doc.createTextNode(String.valueOf(item.getLotID())));
                itemElement.appendChild(lotID);

                // PRICE element
                Element price = doc.createElement("PRICE");
                price.appendChild(doc.createTextNode(String.valueOf(item.getPrice())));
                itemElement.appendChild(price);
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(selectedFile);
            transformer.transform(source, result);

            // Output to console for testing
            StreamResult consoleResult = new StreamResult(System.out);
            transformer.transform(source, consoleResult);
        } catch (ParserConfigurationException | TransformerException | DOMException e) {
            e.printStackTrace();
        }
    }

    public static void uploadMarshall(Store store, File selectedFile){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setNamespaceAware(true);
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            // root element
            Element rootElement = doc.createElement("INVENTORY");
            doc.appendChild(rootElement);

            for (bsxItem item : store.getStore().get(0).getInventory()) {
                if(item.getLotID() != 0) { throw new IllegalArgumentException("This file contains lotIDs for one or more items, therefore it cannot be uploaded, because it was already uploaded in the past");}

                Element itemElement = doc.createElement("ITEM");
                rootElement.appendChild(itemElement);

                for (Field field : item.getClass().getDeclaredFields()) {
                    field.setAccessible(true); // to allow the access of member attributes
                    Object attribute = field.get(item);
                    if (attribute != null && attribute != "" && !attribute.equals(0) && !attribute.equals(0.0) && !field.getName().equalsIgnoreCase("ITEMTYPENAME") ) {
                        Element itemType = doc.createElement(field.getName().toUpperCase());
                        itemType.appendChild(doc.createTextNode(String.valueOf(attribute)));
                        itemElement.appendChild(itemType);

                        System.out.println(field.getName() + "=" + attribute);
                    }
                }
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(selectedFile);
            transformer.transform(source, result);

            // Output to console for testing
            StreamResult consoleResult = new StreamResult(System.out);
            transformer.transform(source, consoleResult);
        } catch (ParserConfigurationException | TransformerException | DOMException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Unmarshal list.
     *
     * @param importFile the import file
     * @return List of inventory items
     */
    public static Store unmarshal(File importFile) {
        List<bsxItem> bsxItems = new ArrayList<>();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(importFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("Item");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    bsxItems.add(new bsxItem(
                            eElement.getElementsByTagName("ItemID").item(0).getTextContent(), //vereist
                            eElement.getElementsByTagName("ItemTypeID").item(0).getTextContent(), //vereist
                            eElement.getElementsByTagName("ColorID").item(0) != null ? Integer.parseInt(eElement.getElementsByTagName("ColorID").item(0).getTextContent()) : 0, //vereist
                            eElement.getElementsByTagName("ItemTypeName").item(0) != null ? eElement.getElementsByTagName("ItemTypeName").item(0).getTextContent() : "",
                            //eElement.getElementsByTagName("ColorName").item(0) != null ? eElement.getElementsByTagName("ColorName").item(0).getTextContent() : "",
                            eElement.getElementsByTagName("CategoryID").item(0) != null ? Integer.parseInt(eElement.getElementsByTagName("CategoryID").item(0).getTextContent()) : 0,
                            //eElement.getElementsByTagName("CategoryName").item(0) != null ? eElement.getElementsByTagName("CategoryName").item(0).getTextContent() : "",
                            //eElement.getElementsByTagName("Status").item(0) != null ? eElement.getElementsByTagName("Status").item(0).getTextContent() : "",
                            eElement.getElementsByTagName("Qty").item(0) != null ? Integer.parseInt(eElement.getElementsByTagName("Qty").item(0).getTextContent()) : 0, //vereist
                            eElement.getElementsByTagName("Price").item(0) != null ? Double.parseDouble(eElement.getElementsByTagName("Price").item(0).getTextContent()) : 0, //vereist
                            eElement.getElementsByTagName("Condition").item(0).getTextContent(), //vereist
                            eElement.getElementsByTagName("Sale").item(0) != null ? Integer.parseInt(eElement.getElementsByTagName("Sale").item(0).getTextContent()) : 0,
                            eElement.getElementsByTagName("Cost").item(0) != null ? Double.parseDouble(eElement.getElementsByTagName("Cost").item(0).getTextContent()) : 0,
                            eElement.getElementsByTagName("Remarks").item(0) != null ? eElement.getElementsByTagName("Remarks").item(0).getTextContent() : "",
                            eElement.getElementsByTagName("LotID").item(0) != null ? Integer.parseInt(eElement.getElementsByTagName("LotID").item(0).getTextContent()) : 0,
                            eElement.getElementsByTagName("Subcondition").item(0) != null ? eElement.getElementsByTagName("Subcondition").item(0).getTextContent() : "",
                            eElement.getElementsByTagName("Bulk").item(0) != null ? Integer.parseInt(eElement.getElementsByTagName("Bulk").item(0).getTextContent()) : 0,
                            eElement.getElementsByTagName("Description").item(0) != null ? eElement.getElementsByTagName("Description").item(0).getTextContent() : "",
                            eElement.getElementsByTagName("Extended").item(0) != null ? eElement.getElementsByTagName("Extended").item(0).getTextContent() : "",
                            eElement.getElementsByTagName("BuyerUsername").item(0) != null ? eElement.getElementsByTagName("BuyerUsername").item(0).getTextContent() : "",
                            eElement.getElementsByTagName("Stockroom").item(0) != null ? eElement.getElementsByTagName("Stockroom").item(0).getTextContent() : "",
                            eElement.getElementsByTagName("StockroomID").item(0) != null ? eElement.getElementsByTagName("StockroomID").item(0).getTextContent() : "",
                            eElement.getElementsByTagName("Retain").item(0) != null ? eElement.getElementsByTagName("Retain").item(0).getTextContent() : "",
                            eElement.getElementsByTagName("myWeight").item(0) != null ? Double.parseDouble(eElement.getElementsByTagName("myWeight").item(0).getTextContent()) : 0,
                            eElement.getElementsByTagName("TQ1").item(0) != null ? Integer.parseInt(eElement.getElementsByTagName("TQ1").item(0).getTextContent()) : 0,
                            eElement.getElementsByTagName("TP1").item(0) != null ? Double.parseDouble(eElement.getElementsByTagName("TP1").item(0).getTextContent()) : 0,
                            eElement.getElementsByTagName("TQ2").item(0) != null ? Integer.parseInt(eElement.getElementsByTagName("TQ2").item(0).getTextContent()) : 0,
                            eElement.getElementsByTagName("TP2").item(0) != null ? Double.parseDouble(eElement.getElementsByTagName("TP2").item(0).getTextContent()) : 0,
                            eElement.getElementsByTagName("TQ3").item(0) != null ? Integer.parseInt(eElement.getElementsByTagName("TQ3").item(0).getTextContent()) : 0,
                            eElement.getElementsByTagName("TP3").item(0) != null ? Double.parseDouble(eElement.getElementsByTagName("TP3").item(0).getTextContent()) : 0
                    ));
                }
            }
        } catch (IOException | NumberFormatException | ParserConfigurationException | DOMException | SAXException e) {
            e.printStackTrace();
        }
        List<Inventory> storelist = new ArrayList<>();
        Inventory inventory = new Inventory(bsxItems);
        storelist.add(inventory);
        return new Store(storelist);
    }
}
