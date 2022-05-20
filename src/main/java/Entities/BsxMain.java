package Entities;

import Config.ConfigurationProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BsxMain {
    private static final String nameOfEndXML = "updateStore";
    private static final String baseUrl = "https://api.bricklink.com/api/store/v1/";
    private static final int HardLimitRequest = 4800;
    private static final RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
    private static final CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
    private static int requestCounter = 0;

    private static boolean isUpload = false;
    private static File file;

    public static void BSXMain(){
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(ConfigurationProperty.CONSUMER_KEY.getPropertyName(), ConfigurationProperty.CONSUMER_SECRET.getPropertyName());
        consumer.setTokenWithSecret(ConfigurationProperty.TOKEN_VALUE.getPropertyName(), ConfigurationProperty.TOKEN_SECRET.getPropertyName());
        requestCounter = loadRequestCounter();  //inladen request counter

        Store store = loadBSX(file); //inladen bsx bestand
        //verkrijgen prijsdata via bricklink api
        for (bsxItem item : store.getStore().get(0).getInventory()) {
            item.setPrice(requestItemPrice(item, consumer));
        }
        //Schrijven naar nieuw update xml bestand
        writeBSX(store, isUpload);
        saveRequestCounter(requestCounter);
    }

    private static int loadRequestCounter(){
        try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {
            Instant modifiedDate = Instant.ofEpochMilli(new File("src/main/resources/config.properties").lastModified()).truncatedTo(ChronoUnit.DAYS);
            Instant today = Instant.now().truncatedTo(ChronoUnit.DAYS);
            Properties prop = new Properties();
            prop.load(input);

            if (modifiedDate.equals(today) || modifiedDate.isAfter(today)) {
                requestCounter = Integer.parseInt(prop.getProperty("requestCounter"));
            } else {
                prop.clear();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return requestCounter;
    }

    private static Store loadBSX(File file){
        return XMLhelper.unmarshal(file);
    }

    private static double requestItemPrice(bsxItem item, OAuthConsumer consumer){
        double newPrice = 0;

        //rare bricklink api correctie
        if(item.getItemTypeName().equals("Minifigure")) item.setItemTypeName("minifig");

        //samenstellen url request string
        String urlSold = baseUrl + "items/"+item.getItemTypeName().toLowerCase()+"/" + item.getItemID() + "/price?guide_type=sold&new_or_used=" + item.getCondition() + "&region=europe&color_id=" +item.getColor();
        String urlStock = baseUrl + "items/"+item.getItemTypeName().toLowerCase()+"/" + item.getItemID() + "/price?guide_type=Stock&new_or_used=" + item.getCondition() + "&region=europe&color_id=" +item.getColor();

        // response voor items in stock en verkochte items
        PriceResponse soldResponse = bricklinkPriceDataRequest(urlSold, consumer);
        PriceResponse stockResponse = bricklinkPriceDataRequest(urlStock, consumer);

        double stockSoldRatio =  stockResponse.getPriceData().getTotal_quantity() / soldResponse.getPriceData().getTotal_quantity();

        if(stockSoldRatio < 3){
            newPrice = soldResponse.getPriceData().getAvg_price() + 0.01;
        } else if (stockSoldRatio > 3 & stockSoldRatio < 5) {
            newPrice = soldResponse.getPriceData().getAvg_price();
        } else if (stockSoldRatio > 5) {
            newPrice = soldResponse.getPriceData().getAvg_price() - 0.01;
        }
        return Math.round(newPrice * 1000d) / 1000d;
    }

    private static PriceResponse bricklinkPriceDataRequest(String url, OAuthConsumer consumer) {
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
            saveRequestCounter(requestCounter);
            throw new Exception("Daily request limit is reached");
        }
        return result;
    }

    /**
     * Method to partition any list
     *
     * @param <T>  any type of list
     * @param list list to be partitioned
     * @return list of lists with size L
     */
    private static <T> List<List<T>> partitionList(List<T> list) {
        List<List<T>> parts = new ArrayList<>();
        final int N = list.size();
        for (int i = 0; i < N; i += 1000) {
            parts.add(new ArrayList<>(
                    list.subList(i, Math.min(N, i + 1000)))
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

    public static void setFile(File file){
        BsxMain.file = file;
    }

    public static void setIsUpload(boolean isUpload) {
        BsxMain.isUpload = isUpload;
    }

    private static void writeBSX(Store store, Boolean isUpload) {
        List<List<bsxItem>> partitions = partitionList(store.getStore().get(0).getInventory());
        for (int i = 0; i < partitions.size(); i++) {
            String fileName = file.getParent() +"/"+ nameOfEndXML + i;
            if (isUpload) {
                XMLhelper.uploadMarshall(store, new File(fileName + ".xml"));
            } else {
                XMLhelper.updateMarshal(store, new File(fileName + ".xml"));
            }
        }
    }
}
