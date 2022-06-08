package entities;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The type Jaxbxml handler.
 */
public class XmlHelper {
    public static void marshall(Store store, File selectedFile, Boolean isUpload, Boolean isUpdate, Boolean isBrickStore) {
        try  {
        DocumentBuilderFactory docBuildFactory = DocumentBuilderFactory.newInstance();
        docBuildFactory.setNamespaceAware(true);
        DocumentBuilder docBuilder = docBuildFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        //TODO: test dit stukje grondig
        Element rootElement = doc.createElement("INVENTORY");
        doc.appendChild(rootElement);

        if (isBrickStore) {
            Element newRootElement = doc.createElement("BrickStoreXML");
            newRootElement.appendChild(doc.getFirstChild());
        }
        //

        for (BsxItem item : store.getStore().get(0).getInventory()) {
            if (item.getLotId() == 0 && isUpdate) {
                throw new IllegalArgumentException(
                        "This file misses lotIDs for one or more items, "
                                + "therefore it cannot be updated, because it was not uploaded");
            }

            Element itemElement = doc.createElement("ITEM");
            rootElement.appendChild(itemElement);

            for (Field field : item.getClass().getDeclaredFields()) {
                field.setAccessible(true); // to allow the access of member attributes
                Object attribute = field.get(item);
                if(isUpdate){
                    if(field.getName().equals("LOTID") || field.getName().equals("PRICE")){
                        Element itemType = doc.createElement(field.getName().toUpperCase());
                        itemType.appendChild(doc.createTextNode(String.valueOf(attribute)));
                        itemElement.appendChild(itemType);

                        System.out.println(field.getName() + "=" + attribute);
                    }
                } else {
                    if (attribute != null && attribute != "" && !attribute.equals(0) && !attribute.equals(0.0) && !field.getName().equalsIgnoreCase("ITEMTYPENAME")) {
                        Element itemType = doc.createElement(field.getName().toUpperCase());
                        itemType.appendChild(doc.createTextNode(String.valueOf(attribute)));
                        itemElement.appendChild(itemType);

                        System.out.println(field.getName() + "=" + attribute);
                    }
                }
            }
        }

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
        } catch (TransformerException | ParserConfigurationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }




    /*
    public static void updateMarshal(Store store, String outputDirectory, String fileName) {
        try {
            File finalOutputDirectory = new File(outputDirectory);
            if(!finalOutputDirectory.exists()) finalOutputDirectory.mkdir();
            File outputFile = new File(finalOutputDirectory, fileName);
            DocumentBuilderFactory docBuildFactory = DocumentBuilderFactory.newInstance();
            docBuildFactory.setNamespaceAware(true);
            DocumentBuilder docBuilder = docBuildFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            // root element
            Element rootElement = doc.createElement("INVENTORY");
            doc.appendChild(rootElement);

            for (BsxItem item : store.getStore().get(0).getInventory()) {
                if (item.getLotId() == 0) {
                    throw new IllegalArgumentException(
                        "This file misses lotIDs for one or more items, "
                            + "therefore it cannot be updated, because it was not uploaded");
                }

                // ITEM element
                Element itemElement = doc.createElement("ITEM");
                rootElement.appendChild(itemElement);

                // LOTID element
                Element lotId = doc.createElement("LOTID");
                lotId.appendChild(doc.createTextNode(String.valueOf(item.getLotId())));
                itemElement.appendChild(lotId);

                // PRICE element
                Element price = doc.createElement("PRICE");
                price.appendChild(doc.createTextNode(String.valueOf(item.getPrice())));
                itemElement.appendChild(price);
            }

            // write the content into xml file
            write(doc, outputFile);
        } catch (ParserConfigurationException | DOMException e) {
            e.printStackTrace();
        }
    }

    public static void uploadMarshall(Store store, File selectedFile) {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            docBuilderFactory.setNamespaceAware(true);
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            // root element
            Element rootElement = doc.createElement("INVENTORY");
            doc.appendChild(rootElement);

            for (BsxItem item : store.getStore().get(0).getInventory()) {
                if (item.getLotId() != 0) {
                    throw new IllegalArgumentException(
                        "This file contains lotIDs for one or more items, therefore it "
                            + "cannot be uploaded, because it was already uploaded in the past");
                }

                Element itemElement = doc.createElement("ITEM");
                rootElement.appendChild(itemElement);

                for (Field field : item.getClass().getDeclaredFields()) {
                    field.setAccessible(true); // to allow the access of member attributes
                    Object attribute = field.get(item);
                    if (attribute != null && attribute != "" && !attribute.equals(0)
                        && !attribute.equals(0.0) && !field.getName()
                        .equalsIgnoreCase("ITEMTYPENAME")) {
                        Element itemType = doc.createElement(field.getName().toUpperCase());
                        itemType.appendChild(doc.createTextNode(String.valueOf(attribute)));
                        itemElement.appendChild(itemType);

                        System.out.println(field.getName() + "=" + attribute);
                    }
                }
            }
            write(doc, selectedFile);
        } catch (ParserConfigurationException  | DOMException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static void write(Document doc, File selectedFile){
        try{
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
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }


     */
    /**
     * Unmarshal list.
     *
     * @param importFile the import file
     * @return List of inventory items
     */
    public static Store unmarshal(File importFile) {
        List<BsxItem> bsxItems = new ArrayList<>();
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(importFile);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("Item");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    bsxItems.add(new BsxItem(
                        element.getElementsByTagName("ItemID").item(0).getTextContent(), //vereist
                        element.getElementsByTagName("ItemTypeID").item(0).getTextContent(),
                        //vereist
                        element.getElementsByTagName("ColorID").item(0) != null ? Integer.parseInt(
                            element.getElementsByTagName("ColorID").item(0).getTextContent()) : 0,
                        //vereist
                        element.getElementsByTagName("ItemTypeName").item(0) != null
                            ? element.getElementsByTagName("ItemTypeName").item(0).getTextContent()
                            : "",
                        element.getElementsByTagName("CategoryID").item(0) != null
                            ? Integer.parseInt(
                            element.getElementsByTagName("CategoryID").item(0).getTextContent())
                            : 0,
                        element.getElementsByTagName("Qty").item(0) != null ? Integer.parseInt(
                            element.getElementsByTagName("Qty").item(0).getTextContent()) : 0,
                        //vereist
                        element.getElementsByTagName("Price").item(0) != null ? Double.parseDouble(
                            element.getElementsByTagName("Price").item(0).getTextContent()) : 0,
                        //vereist
                        element.getElementsByTagName("Condition").item(0).getTextContent(),
                        //vereist
                        element.getElementsByTagName("Sale").item(0) != null ? Integer.parseInt(
                            element.getElementsByTagName("Sale").item(0).getTextContent()) : 0,
                        element.getElementsByTagName("Cost").item(0) != null ? Double.parseDouble(
                            element.getElementsByTagName("Cost").item(0).getTextContent()) : 0,
                        element.getElementsByTagName("Remarks").item(0) != null
                            ? element.getElementsByTagName("Remarks").item(0).getTextContent()
                            : "",
                        element.getElementsByTagName("LotID").item(0) != null ? Integer.parseInt(
                            element.getElementsByTagName("LotID").item(0).getTextContent()) : 0,
                        element.getElementsByTagName("Subcondition").item(0) != null
                            ? element.getElementsByTagName("Subcondition").item(0).getTextContent()
                            : "",
                        element.getElementsByTagName("Bulk").item(0) != null ? Integer.parseInt(
                            element.getElementsByTagName("Bulk").item(0).getTextContent()) : 0,
                        element.getElementsByTagName("Description").item(0) != null
                            ? element.getElementsByTagName("Description").item(0).getTextContent()
                            : "", element.getElementsByTagName("Extended").item(0) != null
                        ? element.getElementsByTagName("Extended").item(0).getTextContent() : "",
                        element.getElementsByTagName("BuyerUsername").item(0) != null
                            ? element.getElementsByTagName("BuyerUsername").item(0)
                            .getTextContent() : "",
                        element.getElementsByTagName("Stockroom").item(0) != null
                            ? element.getElementsByTagName("Stockroom").item(0).getTextContent()
                            : "", element.getElementsByTagName("StockroomID").item(0) != null
                        ? element.getElementsByTagName("StockroomID").item(0).getTextContent()
                        : "", element.getElementsByTagName("Retain").item(0) != null
                        ? element.getElementsByTagName("Retain").item(0).getTextContent() : "",
                        element.getElementsByTagName("myWeight").item(0) != null
                            ? Double.parseDouble(
                            element.getElementsByTagName("myWeight").item(0).getTextContent()) : 0,
                        element.getElementsByTagName("TQ1").item(0) != null ? Integer.parseInt(
                            element.getElementsByTagName("TQ1").item(0).getTextContent()) : 0,
                        element.getElementsByTagName("TP1").item(0) != null ? Double.parseDouble(
                            element.getElementsByTagName("TP1").item(0).getTextContent()) : 0,
                        element.getElementsByTagName("TQ2").item(0) != null ? Integer.parseInt(
                            element.getElementsByTagName("TQ2").item(0).getTextContent()) : 0,
                        element.getElementsByTagName("TP2").item(0) != null ? Double.parseDouble(
                            element.getElementsByTagName("TP2").item(0).getTextContent()) : 0,
                        element.getElementsByTagName("TQ3").item(0) != null ? Integer.parseInt(
                            element.getElementsByTagName("TQ3").item(0).getTextContent()) : 0,
                        element.getElementsByTagName("TP3").item(0) != null ? Double.parseDouble(
                            element.getElementsByTagName("TP3").item(0).getTextContent()) : 0));
                }
            }
        } catch (IOException | NumberFormatException | ParserConfigurationException | DOMException
                 | SAXException e) {
            e.printStackTrace();
        }
        List<Inventory> storelist = new ArrayList<>();
        Inventory inventory = new Inventory(bsxItems);
        storelist.add(inventory);
        return new Store(storelist);
    }
}
