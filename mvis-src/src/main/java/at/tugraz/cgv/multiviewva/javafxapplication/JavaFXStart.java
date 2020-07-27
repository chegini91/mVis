/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.javafxapplication;

import at.tugraz.cgv.multiviewva.utility.InfiniteStreamRecognize;
import at.tugraz.cgv.multiviewva.controllers.ContextMenuController;
import at.tugraz.cgv.multiviewva.controllers.MainFXController;
import at.tugraz.cgv.multiviewva.utility.GraphicUtility;
import at.tugraz.cgv.multiviewva.utility.SearchUtility;
import at.tugraz.cgv.multiviewva.utility.speechUtility;
import java.io.IOException;
import java.util.prefs.Preferences;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 *
 * @author mchegini
 */
public class JavaFXStart extends Application {

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        MainFXController mainController = fxmlLoader.getController();
        Scene scene = new Scene(root);
        GraphicUtility.stage = stage;
        stage.setScene(scene);
        stage.setTitle("mVis");
//        root.getChildrenUnmodifiable().add(circ)
//        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent event) {
//                if (event.getCode() == KeyCode.ENTER) {
//                    SearchUtility.dataModel.updateAL();
//                    SearchUtility.parentController.getLabelPaneController().update();
//                }
//            }
//        });
//        scene.getStylesheets().add("path/stylesheet.css");
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
//        scene.getStylesheets().add(ButtonDemo.class.getResource("/css/jfoenix-components.css").toExternalForm());
        //true full screen
        //stage.setFullScreen(true);
//        initilizeControllers();

        stage.show();
        //set icon
        stage.getIcons().add(new Image("/cgv.png"));

        //init number for intervals serach
        SearchUtility.init();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

//        TuioFX tuiofx = new TuioFX(stage, Configuration.ioS());
//        tuiofx.enableMTWidgets(true);
//        tuiofx.start();
//        stage.setFullScreen(true);
        addChangeListeners(stage);

        //lef menu stage
        Stage leftMenu = new Stage();
        FXMLLoader fxmlLoaderLeft = new FXMLLoader(getClass().getResource("/fxml/ContextMenu.fxml"));
        Parent leftParent = (Parent) fxmlLoaderLeft.load();
        ContextMenuController leftController = fxmlLoaderLeft.getController();
        leftController.setStage(leftMenu);
        leftController.setRightSide(false);
        leftController.setMainFXController(mainController);

        Scene leftMenuScene = new Scene(leftParent);

        leftMenu.setScene(leftMenuScene);
        leftMenu.initStyle(StageStyle.UTILITY);
        leftMenu.setAlwaysOnTop(true);
//        leftMenu.show();
        mainController.setLeftMenuController(leftController);

        //right lens menu
        if (GraphicUtility.doubleUser) {

            Stage rightMenu = new Stage();
            FXMLLoader fxmlLoaderRight = new FXMLLoader(getClass().getResource("/fxml/ContextMenu.fxml"));
            Parent rightParent = (Parent) fxmlLoaderRight.load();
            ContextMenuController rightController = fxmlLoaderRight.getController();
            rightController.setRightSide(true);
            rightController.setMainFXController(mainController);
            rightController.setStage(rightMenu);
            Scene rightMenuScene = new Scene(rightParent);
            rightMenu.setScene(rightMenuScene);
            rightMenu.initStyle(StageStyle.UTILITY);
            rightMenu.setAlwaysOnTop(true);
//            rightMenu.show();
            mainController.setRightMenuController(rightController);
        }

        //sound recognition 

    }

    /**
     *
     * @author mchegini Just an example of how to access controllers from main
     * class for the further use
     */
    private void initilizeControllers() throws IOException {
//        FXMLLoader ScatterPlotLoader = FXMLLoader.load(getClass().getResource("/fxml/ScatterPlot.fxml"));
//        ScatterPlotController sp = ScatterPlotLoader.getController();
    }

    /**
     * change listeners for stage in the config file using Java Preference API
     */
    private void addChangeListeners(Stage stage) {
        Preferences prefs = Preferences.userNodeForPackage(JavaFXStart.class);

        stage.setWidth(prefs.getDouble("stageWidthProperty", 1200));
        stage.setHeight(prefs.getDouble("stageHeightProperty", 800));
        stage.setX(prefs.getDouble("stageXProperty", 0));
        stage.setY(prefs.getDouble("stageYProperty", 0));

        stage.setMaximized(prefs.getBoolean("isWindowMaximized", false));

        stage.maximizedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                Preferences prefs = Preferences.userNodeForPackage(JavaFXStart.class);
                prefs.putBoolean("isWindowMaximized", t1);
            }
        });

        stage.xProperty().addListener((obs, oldVal, newVal) -> {
            prefs.putDouble("stageXProperty", (double) newVal);
        });

        stage.yProperty().addListener((obs, oldVal, newVal) -> {
            prefs.putDouble("stageYProperty", (double) newVal);
        });

        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            prefs.putDouble("stageWidthProperty", (double) newVal);
        });

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            prefs.putDouble("stageHeightProperty", (double) newVal);
        });
    }

}
