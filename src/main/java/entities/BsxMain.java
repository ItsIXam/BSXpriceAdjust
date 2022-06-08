package entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.ProgressScreenController;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
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

/**
 * The type Bsx main.
 */
public class BsxMain {

    private static final String nameOfEndXML = "updateStore";
    private static final String baseUrl = "https://api.bricklink.com/api/store/v1/";
    private static final int HardLimitRequest = 4800;
    private static final RequestConfig requestConfig = RequestConfig.custom()
        .setCookieSpec(CookieSpecs.STANDARD).build();
    private static final CloseableHttpClient client = HttpClients.custom()
        .setDefaultRequestConfig(requestConfig).build();
    private static final File appdataBSXDirectory = new File(
        System.getenv("APPDATA") + "\\bsxPriceAdjust");
    private static final File userCredentialsFile = new File(
        appdataBSXDirectory + "\\userSettings.properties");
    private static final File requestCounterFile = new File(
        appdataBSXDirectory + "\\config.properties");
    private static final File outputDirectory = new File ("src/main/resources/updateStore0.xml");
    private static int requestCounter = 0;
    private static UserSettings userSettings;
    private static boolean isUpload = false;
    private static boolean isUpdate = false;
    private static boolean isBrickStore = false;
    private static File file;

    /**
     * Bsx main.
     *
     * @param progressController the progress controller
     */
    public static void bsxMain(ProgressScreenController progressController) {
        if (userCredentialsFile.exists()) {
            userSettings = UserSettings.loadUserCredentials(userCredentialsFile);
        }
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(userSettings.getConsumerKey(),
            userSettings.getConsumerSecret());
        consumer.setTokenWithSecret(userSettings.getTokenValue(), userSettings.getTokenSecret());

        Store store = loadBsx(file); //inladen bsx bestand

        final ProgressBar pb = progressController.getProgressBar();
        final Label adjustPriceLabel = progressController.getAdjustPriceLabel();
        final Label percentageCompletedLabel = progressController.getPercentageCompletedLabel();

        // separate non-FX thread
        // runnable for that thread
        new Thread(() -> {
            //verkrijgen prijsdata via bricklink api
            for (BsxItem item : store.getStore().get(0).getInventory()) {
                int itemCount = store.getStore().get(0).getInventory().indexOf(item) + 1;
                int inventorySize = store.getStore().get(0).getInventory().size();

                item.setPrice(requestItemPrice(item, consumer));

                final double percentageCompleted = itemCount * 100d / inventorySize;
                // update ProgressIndicator on FX thread
                Platform.runLater(() -> {
                    adjustPriceLabel.setText(
                        "Adjusting price of item " + itemCount + " out of " + inventorySize);
                    percentageCompletedLabel.setText(
                        (percentageCompleted * 10) / 10 + "% completed");
                    pb.setProgress(percentageCompleted / 100);
                });
            }
            Platform.runLater(() -> {
                progressController.getDoneLabel().setText("Done, Prices are adjusted");
                progressController.getCloseButton().setDisable(false);
                progressController.setButtonToFile ();
            });
        }).start();
        //Schrijven naar nieuw update xml bestand

        writeBsx(store);
        System.out.println("Done");
    }

    private static Store loadBsx(File file) {
        return XmlHelper.unmarshal(file);
    }

    private static void writeBsx(Store store) {
        if(!outputDirectory.exists()) outputDirectory.mkdir();

        List<List<BsxItem>> partitions = partitionList(store.getStore().get(0).getInventory());
        for (int i = 0; i < partitions.size(); i++) {
            String fileName = file.getParent() + "/" + nameOfEndXML + i;
            File outputFile = new File(outputDirectory, fileName);
            XmlHelper.marshall(store, outputFile, isUpload, isUpdate, isBrickStore);
        }
    }

    /**
     * Request item price double.
     *
     * @param item     the item
     * @param consumer the consumer
     * @return the double
     */
    static double requestItemPrice(BsxItem item, OAuthConsumer consumer) {
        double newPrice = 0;

        //rare bricklink api correctie
        if (item.getItemTypeName().equals("Minifigure")) {
            item.setItemTypeName("minifig");
        }

        //samenstellen url request string
        String urlSold =
            baseUrl + "items/" + item.getItemTypeName().toLowerCase() + "/" + item.getItemId()
                + "/price?guide_type=sold&new_or_used=" + item.getCondition()
                + "&region=europe&color_id=" + item.getColor();
        String urlStock =
            baseUrl + "items/" + item.getItemTypeName().toLowerCase() + "/" + item.getItemId()
                + "/price?guide_type=Stock&new_or_used=" + item.getCondition()
                + "&region=europe&color_id=" + item.getColor();

        // response voor items in stock en verkochte items
        PriceResponse soldResponse = bricklinkPriceDataRequest(urlSold, consumer);
        PriceResponse stockResponse = bricklinkPriceDataRequest(urlStock, consumer);

        if (soldResponse.getMeta().getCode() == 200) {
            double stockSoldRatio =
                stockResponse.getPriceData().getTotalQuantity() / soldResponse.getPriceData()
                    .getTotalQuantity();

            if (stockSoldRatio < 3) {
                newPrice = soldResponse.getPriceData().getAvgPrice() + 0.01;
            } else if (stockSoldRatio > 3 & stockSoldRatio < 5) {
                newPrice = soldResponse.getPriceData().getAvgPrice();
            } else if (stockSoldRatio > 5) {
                newPrice = soldResponse.getPriceData().getAvgPrice() - 0.01;
            }
            newPrice = Math.round(newPrice * 1000d) / 1000d;
        }
        return newPrice;
    }

    private static PriceResponse bricklinkPriceDataRequest(String url, OAuthConsumer consumer) {
        if (requestCounterFile.exists()) {
            requestCounter = loadRequestCounter();
        }
        ObjectMapper mapper = new ObjectMapper();
        HttpRequestBase httpRequest = new HttpGet(url);
        PriceResponse res;
        //ondertekenen oauth request
        try {
            consumer.sign(httpRequest);
        } catch (OAuthMessageSignerException | OAuthExpectationFailedException
                 | OAuthCommunicationException e) {
            throw new RuntimeException(e);
        }

        try (CloseableHttpResponse httpResponse = execute(requestCounter, httpRequest)) {
            if (httpResponse.getStatusLine().getStatusCode() >= 300) {
                throw new HttpResponseException(httpResponse.getStatusLine().getStatusCode(),
                    httpResponse.getStatusLine().getReasonPhrase());
            } else {
                res = mapper.readValue(EntityUtils.toString(httpResponse.getEntity()),
                    PriceResponse.class);
                EntityUtils.consume(httpResponse.getEntity());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        requestCounter++;
        saveRequestCounter(requestCounter);
        return res;

    }

    /**
     * Execute http request and keep track of a daily request limit.
     *
     * @param counter     the counter
     * @param httpRequest the http request
     * @return closeable http response
     * @throws Exception daily request limit exception
     */
    public static CloseableHttpResponse execute(int counter, HttpRequestBase httpRequest)
        throws Exception {
        CloseableHttpResponse result = null;
        if (counter < HardLimitRequest) {
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
     * Method to partition any list.
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

    private static int loadRequestCounter() {
        try (InputStream input = new FileInputStream(requestCounterFile)) {
            Instant modifiedDate = Instant.ofEpochMilli(requestCounterFile.lastModified())
                .truncatedTo(ChronoUnit.DAYS);
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

    private static void saveRequestCounter(int counter) {
        try (OutputStream output = new FileOutputStream(requestCounterFile)) {
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

    public static File getFile() {
        return file;
    }

    /**
     * Sets file.
     *
     * @param file the file
     */
    public static void setFile(File file) {
        BsxMain.file = file;
    }

    /**
     * Sets is upload.
     *
     * @param isUpload the is upload
     */
    public static void setIsUpload(boolean isUpload) {
        BsxMain.isUpload = isUpload;
    }

    public static void setIsUpdate(boolean isUpdate) {
        BsxMain.isUpdate = isUpdate;
    }

    public static void setIsBrickStore(boolean isBrickStore) {
        BsxMain.isBrickStore = isBrickStore;
    }
}
