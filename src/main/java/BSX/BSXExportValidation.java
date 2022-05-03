package BSX;

import com.google.common.collect.Lists;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BSXExportValidation {
    private static final int amountOfDigitsWanted = 4;
    private static final String nameOfEndXML = "uploadToStore";


    public static void main(String[] args) throws Exception {
        //inladen bsx bestand
        //verkrijgen prijsdata via bricklink api
        //bepalen van nieuwe prijs

        List<Inventory> store = new ArrayList<>();

        try {
            store = JAXBXMLHandler.unmarshal(new File("src/main/resources/Store.bsx"));
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        for (BsxItem item : store.get(0).getInventory()) {
            StringBuilder itemRemark;
            if(item.getRemarks() != null){
                itemRemark = new StringBuilder(item.getRemarks().toUpperCase(Locale.ROOT));
                if(itemRemark.toString().matches("Z\\d+")){
                    String[] splitRemark = itemRemark.toString().split("Z");
                    itemRemark = new StringBuilder(splitRemark[1]);
                    int digitCount = splitRemark[1].replaceAll("\\D", "").length();
                    int zerosToBeAdded = amountOfDigitsWanted - digitCount;
                    if(zerosToBeAdded == 0) {continue;}
                    if(zerosToBeAdded > 0){
                        for(int i = 0; i < zerosToBeAdded; i++){
                            itemRemark.insert(0, "0");
                        }
                    } else {
                        int zerosToBeRemoved = -zerosToBeAdded;
                        int amountOfZerosPresentInRemark = itemRemark.length() - itemRemark.toString().replaceAll("0", "").length();
                        if(zerosToBeRemoved < amountOfZerosPresentInRemark){
                            itemRemark = new StringBuilder(itemRemark.substring(zerosToBeRemoved, itemRemark.length()));
                        } else {
                            throw new Exception("amountOfDigitsWanted is lower than number in remark of LotID "+ item.getLotID()+", program is terminated");
                        }
                    }
                    itemRemark.insert(0, "Z");
                    item.setRemarks(itemRemark.toString());
                }
            }
        }
        List<List<BsxItem>> partitions = Lists.partition(store.get(0).getInventory(), 1000);
        for (int i = 0; i < partitions.size(); i++) {
            try {
                JAXBXMLHandler.marshal(partitions.get(i), new File("src/main/resources/"+ nameOfEndXML + i +".xml"));
            } catch (IOException | JAXBException e) {
                e.printStackTrace();
            }
        }
    }

}
