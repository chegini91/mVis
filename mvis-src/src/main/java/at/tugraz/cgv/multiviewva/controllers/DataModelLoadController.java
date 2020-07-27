package at.tugraz.cgv.multiviewva.controllers;

import at.tugraz.cgv.multiviewva.utility.DataLoadUtility;
import at.tugraz.cgv.multiviewva.utility.SearchUtility;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DataModelLoadController implements Initializable {

    //stage of the application
    private Stage stage;

    //stage of loadData menu
    private Stage loadStage;

    @FXML
    private ToggleGroup delimiterGroup;

    @FXML
    private Button cancelB;

    @FXML
    private CheckBox labellingDemo;

    @FXML
    private Button loadB;

    @FXML
    private CheckBox pacoordGaze;


    private MainFXController mainFXController;

    public void setMainFXController(MainFXController mainFXController) {
        this.mainFXController = mainFXController;
    }

    public void setLoadStage(Stage loadStage) {
        this.loadStage = loadStage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if(SearchUtility.parentController.getGazeListener().isOnGazeParcoord()){
            pacoordGaze.setSelected(true);
        }

        labellingDemo.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                DataLoadUtility.labellingDemo = t1;
            }
        });

        pacoordGaze.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                SearchUtility.parentController.getGazeListener().setOnGazeParcoord(t1);
            }
        });


        delimiterGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (delimiterGroup.getSelectedToggle() != null) {
                    switch (((RadioButton) delimiterGroup.getSelectedToggle()).getId()) {
                        case "tab":
                            DataLoadUtility.delimiter = "\t";
                            break;
                        case "comma":
                            DataLoadUtility.delimiter = ",";
                            break;
                        case "semicolon":
                            DataLoadUtility.delimiter = ";";
                            break;
                    }

                }
            }

        });

        loadB.setOnMouseClicked(mouseEvent -> {
            mainFXController.loadData();
        });


        cancelB.setOnMouseClicked(mouseEvent -> {
            loadStage.close();
        });

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setVisible(boolean show) {
        if (!show) {
            stage.hide();
        } else {
            stage.show();
        }
    }
}
