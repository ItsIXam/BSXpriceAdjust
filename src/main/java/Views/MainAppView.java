package Views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;


/**
 * The type Bsx export validation.
 */
public class MainAppView extends Application {
    private static final File appdataBSXDirectory = new File(System.getenv("APPDATA")+"\\bsxPriceAdjust");
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
        if(!appdataBSXDirectory.exists()) {
            try {
                Files.createDirectory(appdataBSXDirectory.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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
