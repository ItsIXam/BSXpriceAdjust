package Views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;


/**
 * The type Bsx export validation.
 */
public class MainAppView extends Application {


    private static Stage primaryStage;

    /**
     * Launch the application.
     * @param args optional launch parameters
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Method used to get the primary stage of the application (used for changing scenes).
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Method used to create the home page code view.
     */
    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        primaryStage.setMinWidth(520);
        primaryStage.setMinHeight(420);
        primaryStage.setTitle("Lecture Lounge home screen");

        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/Scenes/HomeScreen.fxml");
        loader.setLocation(xmlUrl);
        AnchorPane root = loader.load();

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
