package Entities;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

import java.io.*;
import java.nio.file.Files;
import java.util.Properties;

public class UserSettings {

    private final File saveLocation;
    private final String consumerKey;
    private final String consumerSecret;
    private final String tokenValue;
    private final String tokenSecret;

    public UserSettings(File saveLocation, String consumerKey, String consumerSecret, String tokenValue, String tokenSecret) {
        this.saveLocation = saveLocation;
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.tokenValue = tokenValue;
        this.tokenSecret = tokenSecret;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public static UserSettings loadUserCredentials(File fileToLoad){
        UserSettings userSettings = null;
        try (InputStream input = new FileInputStream(fileToLoad)) {

            Properties prop = new Properties();
            prop.load(input);

            userSettings = new UserSettings(fileToLoad, prop.getProperty("consumerKey"), prop.getProperty("consumerSecret"), prop.getProperty("tokenValue") , prop.getProperty("tokenSecret"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return userSettings;
    }

    public void saveUserCredentials(){
        if(!this.saveLocation.exists()) {
            try {
                Files.createDirectory(this.saveLocation.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try (OutputStream output = new FileOutputStream(this.saveLocation+"\\userSettings.properties")) {

            Properties prop = new Properties();
            // set the properties value

            prop.setProperty("consumerKey", String.valueOf(this.consumerKey));
            prop.setProperty("consumerSecret", String.valueOf(this.consumerSecret));
            prop.setProperty("tokenValue", String.valueOf(this.tokenValue));
            prop.setProperty("tokenSecret", String.valueOf(this.tokenSecret));

            // save properties to folder
            prop.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public Boolean verifyUserCredentials(){
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(this.consumerKey, this.consumerSecret);
        consumer.setTokenWithSecret(this.tokenValue, this.tokenSecret);
        bsxItem testItem = new bsxItem("122c01","",11,"Part",0,1,1d,"U", 0, 0d, "",0, "",0,"", "", "", "", "","",0d,0,0,0,0,0,0);
        double testPrice = BsxMain.requestItemPrice(testItem, consumer);

        return testPrice != 0d;
    }

}
