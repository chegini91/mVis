package tugraz.ivis.parcoord;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("parcoord.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Parallel Coordinates Plot");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        ParcoordController parcoordController = loader.getController();
        parcoordController.setStage(primaryStage);
        primaryStage.setMinHeight(600.0);
        primaryStage.setMinWidth(800.0);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setHeight(primaryScreenBounds.getHeight() - 200);
        primaryStage.setWidth(primaryScreenBounds.getWidth() - 200);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
