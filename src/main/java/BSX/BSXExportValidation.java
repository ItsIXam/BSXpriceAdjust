package BSX;

import Config.ConfigurationProperty;
import Price.PriceResponse;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.http.client.HttpResponseException;
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
import java.io.*;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * The type Bsx export validation.
 */
public class BSXExportValidation {
    private static final String nameOfEndXML = "uploadToStore";
    private static final String baseUrl = "https://api.bricklink.com/api/store/v1/";
    private static final int HardLimitRequest = 2000;
    private static final RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
    private static final CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();

    private static int requestCounter = 0;


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

        Instant modifiedDate = Instant.ofEpochMilli(new File("src/main/resources/config.properties").lastModified());
        Instant aDayAgo = ZonedDateTime.now().minusDays(1).toInstant();

        //inladen request teller
        try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {

            Properties prop = new Properties();
            prop.load(input);

            if (modifiedDate.isAfter(aDayAgo)) {
                requestCounter = Integer.parseInt(prop.getProperty("requestCounter"));
            } else {
                prop.clear();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

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
        saveRequestCounter(requestCounter);

    }

    /**
     * Method to request Bricklink Price data
     * @param url formatted url including parameters
     * @param consumer Oauth consumer
     * @return http response casted in PriceResponse class
     * @throws IOException status code exception
     */
    private static PriceResponse bricklinkPriceDataRequest(String url, OAuthConsumer consumer) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        HttpRequestBase httpRequest = new HttpGet(url);
        PriceResponse res;
        //ondertekenen oauth request
        try {
            consumer.sign(httpRequest);
        } catch (OAuthMessageSignerException | OAuthExpectationFailedException | OAuthCommunicationException e) {
            throw new RuntimeException(e);
        }

        try (CloseableHttpResponse httpResponse = execute(requestCounter, httpRequest)) {
            if (httpResponse.getStatusLine().getStatusCode() >= 300) {
                throw new HttpResponseException(httpResponse.getStatusLine().getStatusCode(), httpResponse.getStatusLine().getReasonPhrase());
            } else {
                res = mapper.readValue(EntityUtils.toString(httpResponse.getEntity()), PriceResponse.class);
                EntityUtils.consume(httpResponse.getEntity());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        requestCounter++;
        return res;

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

    private static void saveRequestCounter(int counter) {
        try (OutputStream output = new FileOutputStream("src/main/resources/config.properties")) {
            Properties prop = new Properties();
            // set the properties value
            prop.setProperty("requestCounter", String.valueOf(counter));
            // save properties to project root folder
            prop.store(output, null);
            System.out.println("request counter now is at " + counter);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
