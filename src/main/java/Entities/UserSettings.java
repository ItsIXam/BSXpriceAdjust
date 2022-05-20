package Entities;

import java.io.File;

public class UserSettings {

    private File saveLocation;
    private String consumerKey;
    private String consumerSecret;
    private String tokenValue;
    private String tokenSecret;

    public UserSettings(File saveLocation) {
        this.saveLocation = saveLocation;
    }

    public UserSettings(File saveLocation, String consumerKey, String consumerSecret, String tokenValue, String tokenSecret) {
        this.saveLocation = saveLocation;
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.tokenValue = tokenValue;
        this.tokenSecret = tokenSecret;
    }
/*
    public static void loadUserData(){
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

 */
}
