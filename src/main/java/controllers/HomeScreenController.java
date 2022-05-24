package controllers;

import entities.BsxMain;
import entities.UserSettings;
import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import views.MainAppView;

/**
 * The type Home screen controller.
 */
public class HomeScreenController {

    private final FileChooser fileChooser = new FileChooser();
    private final File userCredentialsFile = new File(
        "C:\\Users\\Max\\AppData\\Roaming\\bsxPriceAdjust\\userSettings.properties");
    private final ToggleGroup group = new ToggleGroup();
    //private Stage stage;
    private File file;

    @FXML
    private MenuItem settingsMenuItem;
    @FXML
    private Label fileText;
    @FXML
    private Label errorLabel;
    @FXML
    private RadioButton updateButton;
    @FXML
    private RadioButton uploadButton;
    @FXML
    private Button startButton;


    /**
     * Set up the environment with the saved settings.
     */
    @FXML
    public void initialize() {
        if (!userCredentialsFile.exists()) {
            userCredentialsUpdate(true,
                "User credentials not found. Please fill in bricklink credentials\n "
                    + "(Edit > Settings > credentials)");
        } else {
            UserSettings.loadUserCredentials(userCredentialsFile);
        }
        MainAppView.getPrimaryStage().setTitle("BSX price adjust");
        updateButton.setToggleGroup(group);
        updateButton.setSelected(true);
        uploadButton.setToggleGroup(group);
    }


    @FXML
    private void onLoadFile() {
        fileChooser.setTitle("Open BSX File");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("BrickStore Document (BSX)", "*.bsx")
        );
        file = fileChooser.showOpenDialog(MainAppView.getPrimaryStage());
        if (file != null) {
            fileText.setText(String.valueOf(file.getAbsoluteFile()));
            BsxMain.setFile(file);
        }
    }

    @FXML
    private void onStart() {
        RadioButton selectedRadioButton = (RadioButton) group.getSelectedToggle();
        if (selectedRadioButton.getText().equals("Upload")) {
            BsxMain.setIsUpload(true);
        }
        if (file != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(
                    ProgressScreenController.class.getResource("/Scenes/ProgressScreen.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setTitle("Adjusting prices");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else {
            setErrorText("Please select a file");
        }
    }

    @FXML
    private void onClose() {
        Stage stage = (Stage) fileText.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onSettings() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(
                ProgressScreenController.class.getResource("/Scenes/SettingsScreen.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Settings");
            stage.setScene(scene);

            stage.setOnHidden(event -> {
                event.consume();
                if (userCredentialsFile.exists()) {
                    userCredentialsUpdate(false, "");
                }
                stage.close();
            });
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setErrorText(String text) {
        errorLabel.setText(text);
        errorLabel.setTextFill(Color.color(1, 0, 0));
    }

    /**
     * Updates ui elements on the home screen.
     *
     * @param startButtonToggle the start button toggle
     * @param errorText         the error text
     */
    public void userCredentialsUpdate(Boolean startButtonToggle, String errorText) {
        startButton.setDisable(startButtonToggle);
        setErrorText(errorText);
    }
}
