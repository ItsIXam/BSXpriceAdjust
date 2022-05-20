package Controllers;

import Entities.BsxMain;
import Entities.UserSettings;
import Views.MainAppView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class HomeScreenController {
    private final FileChooser fileChooser = new FileChooser();
    private final ToggleGroup group = new ToggleGroup();
    private File file;
    private UserSettings settings;

    @FXML
    private Label fileText;
    @FXML
    private Label errorLabel;
    @FXML
    private RadioButton updateButton;
    @FXML
    private RadioButton uploadButton;



    /**
     * Set up the environment with the saved settings.
     */
    @FXML
    private void initialize() {
        settings = new UserSettings(new File(System.getenv("APPDATA")+"/Local/bsxPriceAdjust", "userSettings.properties"));
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
        if(file != null){
            fileText.setText(String.valueOf(file.getAbsoluteFile()));
            BsxMain.setFile(file);
        }
    }

    @FXML
    private void onStart(){
        RadioButton selectedRadioButton = (RadioButton) group.getSelectedToggle();
        if(selectedRadioButton.getText().equals("Upload")) BsxMain.setIsUpload(true);
        if(file != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(ProgressScreenController.class.getResource("/Scenes/ProgressScreen.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setTitle("Adjusting prices");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else {
            errorLabel.setText("Please select a file");
            errorLabel.setTextFill(Color.color(1,0,0));
        }
    }

    @FXML
    private void onClose(){
        Stage stage = (Stage) fileText.getScene().getWindow();
        stage.close();
    }
}
