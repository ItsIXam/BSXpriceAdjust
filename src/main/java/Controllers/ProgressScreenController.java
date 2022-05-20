package Controllers;

import Entities.BsxMain;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

public class ProgressScreenController {

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label adjustPriceLabel;

    @FXML
    private Label percentageCompletedLabel;

    @FXML
    private Label doneLabel;

    @FXML
    private Button closeButton;

    @FXML
    public void initialize(){
        BsxMain.BSXMain(this);
    }

    @FXML
    public void onClose(){
        Stage stage = (Stage) progressBar.getScene().getWindow();
        stage.close();
    }

    public Label getPercentageCompletedLabel() {
        return percentageCompletedLabel;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public Label getAdjustPriceLabel() {
        return adjustPriceLabel;
    }

    public Button getCloseButton() {
        return closeButton;
    }

    public Label getDoneLabel() {
        return doneLabel;
    }
}
