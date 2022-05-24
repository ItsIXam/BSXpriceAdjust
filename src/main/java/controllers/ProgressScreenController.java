package controllers;

import entities.BsxMain;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

/**
 * The type Progress screen controller.
 */
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

    /**
     * Initialize.
     */
    @FXML
    public void initialize() {
        BsxMain.bsxMain(this);
    }

    /**
     * On close.
     */
    @FXML
    public void onClose() {
        Stage stage = (Stage) progressBar.getScene().getWindow();
        stage.close();
    }

    /**
     * Gets percentage completed label.
     *
     * @return the percentage completed label
     */
    public Label getPercentageCompletedLabel() {
        return percentageCompletedLabel;
    }

    /**
     * Gets progress bar.
     *
     * @return the progress bar
     */
    public ProgressBar getProgressBar() {
        return progressBar;
    }

    /**
     * Gets adjust price label.
     *
     * @return the adjust price label
     */
    public Label getAdjustPriceLabel() {
        return adjustPriceLabel;
    }

    /**
     * Gets close button.
     *
     * @return the close button
     */
    public Button getCloseButton() {
        return closeButton;
    }

    /**
     * Gets done label.
     *
     * @return the done label
     */
    public Label getDoneLabel() {
        return doneLabel;
    }
}
