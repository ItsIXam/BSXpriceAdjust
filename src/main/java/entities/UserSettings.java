package entities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

/**
 * The type User settings.
 */
public class UserSettings {

    private final File saveLocation;
    private final String consumerKey;
    private final String consumerSecret;
    private final String tokenValue;
    private final String tokenSecret;

    /**
     * Instantiates a new User settings.
     *
     * @param saveLocation   the save location
     * @param consumerKey    the consumer key
     * @param consumerSecret the consumer secret
     * @param tokenValue     the token value
     * @param tokenSecret    the token secret
     */
    public UserSettings(
        File saveLocation, String consumerKey, String consumerSecret, String tokenValue,
        String tokenSecret
    ) {
        this.saveLocation = saveLocation;
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.tokenValue = tokenValue;
        this.tokenSecret = tokenSecret;
    }

    /**
     * Gets consumer key.
     *
     * @return the consumer key
     */
    public String getConsumerKey() {
        return consumerKey;
    }

    /**
     * Gets consumer secret.
     *
     * @return the consumer secret
     */
    public String getConsumerSecret() {
        return consumerSecret;
    }

    /**
     * Gets token value.
     *
     * @return the token value
     */
    public String getTokenValue() {
        return tokenValue;
    }

    /**
     * Gets token secret.
     *
     * @return the token secret
     */
    public String getTokenSecret() {
        return tokenSecret;
    }

    /**
     * Load user credentials user settings.
     *
     * @param fileToLoad the file to load
     * @return the user settings
     */
    public static UserSettings loadUserCredentials(File fileToLoad) {
        UserSettings userSettings = null;
        try (InputStream input = new FileInputStream(fileToLoad)) {

            Properties prop = new Properties();
            prop.load(input);

            userSettings = new UserSettings(fileToLoad, prop.getProperty("consumerKey"),
                prop.getProperty("consumerSecret"), prop.getProperty("tokenValue"),
                prop.getProperty("tokenSecret"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return userSettings;
    }

    /**
     * Save user credentials.
     */
    public void saveUserCredentials() {
        //kan ervan uitgaan dat saveLocation bestaat.
        //Wordt gecontrolleerd in mainAppView.java > start()
        try (OutputStream output = new FileOutputStream(
            this.saveLocation + "\\userSettings.properties")) {

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

    /**
     * Verify user credentials.
     *
     * @return true if credentials are valid, false if not
     */
    public Boolean verifyUserCredentials() {
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(this.consumerKey,
            this.consumerSecret);
        consumer.setTokenWithSecret(this.tokenValue, this.tokenSecret);
        BsxItem testItem = new BsxItem("122c01", "", 11, "Part", 0, 1, 1d, "U", 0, 0d, "", 0, "", 0,
            "", "", "", "", "", "", 0d, 0, 0, 0, 0, 0, 0);
        double testPrice = BsxMain.requestItemPrice(testItem, consumer);

        return testPrice != 0d;
    }

}
