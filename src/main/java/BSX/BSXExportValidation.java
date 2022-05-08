package BSX;

import Config.ConfigurationProperty;
import Price.PriceResponse;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Bsx export validation.
 */
public class BSXExportValidation {
    private static final int amountOfDigitsWanted = 4;
    private static final String nameOfEndXML = "uploadToStore";
    private static final String baseUrl = "https://api.bricklink.com/api/store/v1/";
    private static final int HardLimitRequest = 4500;
    private static final RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
    private static final CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();


    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {
        List<Inventory> store = new ArrayList<>();
        List<Inventory> newStore = new ArrayList<>();
        newStore.add(new Inventory());

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
            PriceResponse soldResponse = bricklinkPriceDataRequest(urlSold, consumer);
            PriceResponse stockResponse = bricklinkPriceDataRequest(urlStock, consumer);

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
        List<List<BsxItem>> partitions = partitionList(newStore.get(0).getInventory(), 1000);
        for(int i = 0; i < partitions.size(); i++) {
            try {
                JAXBXMLHandler.marshal(partitions.get(i), new File("src/main/resources/" + nameOfEndXML +  i +".xml"));
            } catch (IOException | JAXBException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Method to request Bricklink Price data
     * @param url formatted url including parameters
     * @param consumer Oauth consumer
     * @return http response casted in PriceResponse class
     * @throws Exception status code exception
     */
    private static PriceResponse bricklinkPriceDataRequest(String url, OAuthConsumer consumer) throws Exception {
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

    /**
     * Execute http request and keep track of a daily request limit
     *
     * @param counter the counter
     * @param httpRequest the http request
     * @return closeable http response
     * @throws Exception daily request limit exception
     */
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

    /**
     * Method to partition any list
     * @param list list to be partitioned
     * @param L length of partition size, i.e. the length of the new lists
     * @return list of lists with size L
     * @param <T>
     */
    private static <T> List<List<T>> partitionList(List<T> list, final int L) {
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
