package controllers;

import entities.BsxMain;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;

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
    @FXML
    private HBox toFileButtonBox;

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

    public void setButtonToFile() {
        Button buttonToFile = new Button();
        SVGPath path = new SVGPath();
        path.setContent("resources/Icons/folder_open_black_24dp.svg");
        buttonToFile.setGraphic(path);
        buttonToFile.setAlignment(Pos.CENTER);
        toFileButtonBox.getChildren().add(buttonToFile);
        buttonToFile.setOnAction(e -> {
            try {
                Desktop.getDesktop().open(new File(BsxMain.getFile().getParent()+"\\priceAdjustedFiles"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

    }
}
