/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.controllers;

import at.tugraz.cgv.multiviewva.utility.InfiniteStreamRecognize;
import at.tugraz.cgv.multiviewva.utility.SearchUtility;
import at.tugraz.cgv.multiviewva.utility.speechUtility;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author mohch
 */
public class ConsolePaneController implements Initializable {

    @FXML
    private VBox vbox;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    ScrollPane scrollPane = new ScrollPane();

    private TextArea consoleTextField = new TextArea();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vbox.setStyle("-fx-background-color:WHITE");
        anchorPane.setStyle("-fx-background-color:WHITE");
        scrollPane.setStyle("-fx-background-color:transparent;");
        scrollPane.setContent(vbox);

        consoleTextField.textProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue,
                    Object newValue) {
                consoleTextField.setScrollTop(Double.MAX_VALUE); //this will scroll to the bottom
                //use Double.MIN_VALUE to scroll to the top
            }

        });

        vbox.getChildren().add(consoleTextField);
        initSpeechRec();
    }
    
    public void appendTextFromConsole(String text){
        consoleTextField.appendText(text);
    }

    public void initSpeechRec() {

        Task<Void> executeAppTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (speechUtility.speechRecOn) {
                    InfiniteStreamRecognize.infiniteStreamingRecognize();
                }
                return null;
            }
        };

        executeAppTask.setOnSucceeded(e -> {
            /* code to execute when task completes normally */
        });

        executeAppTask.setOnFailed(e -> {
            Throwable problem = executeAppTask.getException();
            /* code to execute if task throws exception */
        });

        executeAppTask.setOnCancelled(e -> {
            /* task was cancelled */
        });

        Thread thread = new Thread(executeAppTask);
        thread.start();
    }

}
