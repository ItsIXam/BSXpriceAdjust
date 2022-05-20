package Controllers;

import Entities.BsxMain;
import Views.MainAppView;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.io.File;

import static Entities.BsxMain.BSXMain;

@SuppressWarnings("rawtypes")
public class HomeScreenController {
    private final FileChooser fileChooser = new FileChooser();

    @FXML
    private ChoiceBox choiceBox;
    @FXML
    private Label fileText;


    /**
     * Set up the environment with the saved settings.
     */
    @FXML
    private void initialize() {
        MainAppView.getPrimaryStage().setTitle("BSX price adjust");
    }


    @FXML
    private void onLoadFile() {
        fileChooser.setTitle("Open BSX File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("BrickStore Document (BSX)", "*.bsx")
        );
        File file = fileChooser.showOpenDialog(MainAppView.getPrimaryStage());
        if(file != null){
            fileText.setText(String.valueOf(file.getAbsoluteFile()));
            BsxMain.setFile(file);
        }
    }

    @FXML
    private void onStart(){
        if(choiceBox.getValue().equals("Upload")) BsxMain.setIsUpload(true);
        BSXMain();
    }
}
