package BSX;

import Config.ConfigurationProperty;
import Price.Item;
import Price.PriceResponse;
import com.google.common.collect.Lists;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BSXExportValidation {
    private static final int amountOfDigitsWanted = 4;
    private static final String nameOfEndXML = "uploadToStore";
    private static final String baseUrl = "https://api.bricklink.com/api/store/v1/";
    private static final int HardLimitRequest = 4500;
    private static final RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
    private static final CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();


    public static void main(String[] args) throws Exception {
        List<Inventory> store = new ArrayList<>();
        List<Inventory> newStore = new ArrayList<>();
        newStore.add(new Inventory());
        int requestCounter = 0;

        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(ConfigurationProperty.CONSUMER_KEY.getPropertyName(), ConfigurationProperty.CONSUMER_SECRET.getPropertyName());
        consumer.setTokenWithSecret(ConfigurationProperty.TOKEN_VALUE.getPropertyName(), ConfigurationProperty.TOKEN_SECRET.getPropertyName());

        //inladen bsx bestand
        try {
            store = JAXBXMLHandler.unmarshal(new File("src/main/resources/Store.bsx"));
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        //verkrijgen prijsdata via bricklink api
        for (BsxItem item : store.get(0).getInventory()) {
            double newPrice = 0;

            //samenstellen url request string
            String urlSold = baseUrl + "items/part/" + item.getItemId() + "/price?guide_type=sold&new_or_used=" + item.getCondition() + "&region=europe&color_id=" +item.getColor();
            String urlStock = baseUrl + "items/part/" + item.getItemId() + "/price?guide_type=Stock&new_or_used=" + item.getCondition() + "&region=europe&color_id=" +item.getColor();

            // response voor items in stock en verkochte items
            PriceResponse soldResponse = bricklinkPriceDataRequest(urlSold, item, consumer);
            PriceResponse stockResponse = bricklinkPriceDataRequest(urlStock, item, consumer);

            double stockSoldRatio =  stockResponse.getPriceData().getTotal_quantity() / soldResponse.getPriceData().getTotal_quantity();

            if(stockSoldRatio < 3){
                newPrice = soldResponse.getPriceData().getAvg_price() + 0.01;
            } else if (stockSoldRatio > 5) {
                newPrice = soldResponse.getPriceData().getAvg_price() - 0.01;
            } else if (stockSoldRatio > 3 & stockSoldRatio < 5) {
                newPrice = soldResponse.getPriceData().getAvg_price();
            }
            double roundedPrice = Math.round(newPrice * 1000d) / 1000d;
            newStore.get(0).getInventory().add(new BsxItem(item.getLotID(), roundedPrice, item.getColor()));
        }

        //Schrijven naar nieuw update xml bestand
        List<List<BsxItem>> partitions = chopped(newStore.get(0).getInventory(), 1000);
        for(int i = 0; i < partitions.size(); i++) {
            try {
                JAXBXMLHandler.marshal(partitions.get(i), new File("src/main/resources/" + nameOfEndXML +  i +".xml"));
            } catch (IOException | JAXBException e) {
                e.printStackTrace();
            }
        }

    }

    private static PriceResponse bricklinkPriceDataRequest(String url, BsxItem item, OAuthConsumer consumer) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        HttpRequestBase httpRequest = new HttpGet(url);
        //ondertekenen oauth request
        consumer.sign(httpRequest);
        CloseableHttpResponse httpResponse = execute(0, httpRequest);

        if (httpResponse.getStatusLine().getStatusCode() != 200) {
            throw new Exception("blank");
        } else {
            return mapper.readValue(EntityUtils.toString(httpResponse.getEntity()), PriceResponse.class);
        }
    }

    public static CloseableHttpResponse execute(int counter, HttpRequestBase httpRequest) throws Exception {
        CloseableHttpResponse result = null;
        if(counter < HardLimitRequest){
            try {
                result = client.execute(httpRequest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new Exception("Daily request limit is reached");
        }
        return result;
    }

    private static <T> List<List<T>> chopped(List<T> list, final int L) {
        List<List<T>> parts = new ArrayList<List<T>>();
        final int N = list.size();
        for (int i = 0; i < N; i += L) {
            parts.add(new ArrayList<T>(
                    list.subList(i, Math.min(N, i + L)))
            );
        }
        return parts;
    }
}
