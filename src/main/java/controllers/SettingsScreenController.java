package controllers;

import entities.UserSettings;
import java.io.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * The type Settings screen controller.
 */
public class SettingsScreenController {

    private final ObservableList<String> choiceBoxList = FXCollections.observableArrayList(
        "Sold amount", "Stock amount", "sold-over-stock ratio");
    private final File userSettingsFile = new File(
        System.getenv("APPDATA") + "\\bsxPriceAdjust\\userSettings.properties");
    @FXML
    private TextField consumerKeyField;
    @FXML
    private PasswordField consumerSecretField;
    @FXML
    private TextField tokenValueField;
    @FXML
    private PasswordField tokenSecretField;
    @FXML
    private Label errorLabel;
    @FXML
    private ChoiceBox<String> choiceboxName;

    /**
     * Initialize.
     */
    @FXML
    public void initialize() {
        if (userSettingsFile.exists()) {
            UserSettings loadedSettings = UserSettings.loadUserCredentials(userSettingsFile);
            consumerKeyField.setText(loadedSettings.getConsumerKey());
            consumerSecretField.setText(loadedSettings.getConsumerSecret());
            tokenValueField.setText(loadedSettings.getTokenValue());
            tokenSecretField.setText(loadedSettings.getTokenSecret());
        }
        choiceboxName.setItems(choiceBoxList);
    }

    /**
     * On save button pressed.
     */
    @FXML
    public void onSave() {
        String consumerKey = consumerKeyField.getText();
        String consumerSecret = consumerSecretField.getText();
        String tokenValue = tokenValueField.getText();
        String tokenSecret = tokenSecretField.getText();

        UserSettings userSettings = new UserSettings(
            new File(System.getenv("APPDATA") + "\\bsxPriceAdjust"), consumerKey, consumerSecret,
            tokenValue, tokenSecret);
        if (userSettings.verifyUserCredentials()) {
            userSettings.saveUserCredentials();
            setErrorText("Credentials saved", Color.color(0, 1, 0));
        } else {
            setErrorText("Invalid credentials", Color.color(1, 0, 0));
        }

    }

    /**
     * On close button pressed.
     */
    @FXML
    private void onClose() {
        Stage stage = (Stage) consumerKeyField.getScene().getWindow();
        stage.close();
    }

    /**
     * Method to set error text with color to an error label in settings ui.
     *
     * @param text the text on the error label
     * @param color the color of the error label
     */
    private void setErrorText(String text, Color color) {
        errorLabel.setText(text);
        errorLabel.setTextFill(color);
    }
}
