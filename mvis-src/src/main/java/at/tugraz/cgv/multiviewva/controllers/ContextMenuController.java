/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.controllers;

import at.tugraz.cgv.multiviewva.utility.BrushMngUtility;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TabPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import at.tugraz.cgv.multiviewva.utility.SearchUtility;

/**
 * FXML Controller class this is for controlling floating menu (Extended lens
 * menu)
 *
 * @author mchegini email: m.chegini@cgv.tugraz.at
 */
public class ContextMenuController implements Initializable {

    @FXML
    AnchorPane contextMenu;

    @FXML
    Slider regressionDegreeSlider;

    @FXML
    ToggleButton brushing;
    /**
     * slider for changing intersection of nodes between two area 0 means no
     * mutual record and 100 means similar
     */
    @FXML
    Slider searchSlider;

    @FXML
    ToggleButton closeMenu;

    @FXML
    ToggleButton doubleLens;

    @FXML
    Slider scaleStep;

    @FXML
    Slider moveStep;

    @FXML
    Slider sizeMax;

    @FXML
    Slider sizeMin;

    @FXML
    Slider maxModelDistance;

    @FXML
    Slider maxShapeDistance;

    @FXML
    TabPane tabPane;

    @FXML
    private void handleOnDoubleAction(Event event) {

    }

    @FXML
    private ToggleGroup visGroup;

    @FXML
    private ToggleGroup colorGroup;

    @FXML
    private void handleOnSearchAction(ActionEvent e) {
        mainFXController.getSpmController().getSploms().clearAllHeatMaps();
        mainFXController.getSpMainController().searchForLocalPatterns();
    }

    @FXML
    private void handleOnProcessAction(ActionEvent e) {
        mainFXController.getSpmController().getSploms().preProcessForSearch();
        mainFXController.getSpmController().getSploms().setPreProcessed(true);
    }

    @FXML
    private HBox hbox;

    @FXML
    private void handleOnBrushingAction(Event event) {
        BrushMngUtility.brushing = brushing.isSelected();
    }

    @FXML
    private void handleOnCloseAction(Event event) {
//        if (closeMenu.isSelected()) {
//            setVisible(true);
//        } else {
//            setVisible(false);
//            if (isRightSide()) {
//                mainFXController.getSpRightController().setContextMenuVisible(false);
//            } else {
//                mainFXController.getSpMainController().setContextMenuVisible(false);
//            }
//        }
        hbox.getChildren().remove(tabPane);
        contextMenu.setMaxWidth(300);
        stage.setWidth(200);
//        setPosition(stage.getX() + 270, stage.getY());
//        tabPane.setVisible(false); 
//        tabPane.setMaxWidth(0);
//        tabPane.setMaxHeight(0);

    }

    private MainFXController mainFXController;

    //stage of the application
    private Stage stage;

    private boolean rightSide = true;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        contextMenu.getProperties().put("focusArea", "true");

        initialSlidingWindowOptions();
        visGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (visGroup.getSelectedToggle() != null) {
                    SearchUtility.visualRepresentation = ((RadioButton) visGroup.getSelectedToggle()).getId();
                    mainFXController.getSpmController().getSploms().clearAllHeatMaps();
                    mainFXController.getSpMainController().searchForLocalPatterns();
                }
            }

        });

        colorGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (colorGroup.getSelectedToggle() != null) {
                    SearchUtility.colorCoding = ((RadioButton) colorGroup.getSelectedToggle()).getId();
                    mainFXController.getSpmController().getSploms().clearAllHeatMaps();
                    mainFXController.getSpMainController().searchForLocalPatterns();
                }
            }

        });

        //slider init for regression
        regressionDegreeSlider.valueProperty().addListener((obs, oldval, newVal)
                -> regressionDegreeSlider.setValue(newVal.intValue()));

        regressionDegreeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (rightSide) {
                    mainFXController.getSpRightController().setCurveDegree((int) regressionDegreeSlider.valueProperty().doubleValue());
                } else {
                    mainFXController.getSpMainController().setCurveDegree((int) regressionDegreeSlider.valueProperty().doubleValue());
                }

            }
        });

        searchSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (rightSide) {
                    mainFXController.getSpRightController().setSearchIntersectionSlider(searchSlider.valueProperty().doubleValue());
                } else {
                    mainFXController.getSpMainController().setSearchIntersectionSlider(searchSlider.valueProperty().doubleValue());
                }

            }
        });

        //bound double lens button value to doubleLens value in scatterPlotController
        doubleLens.selectedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                if (rightSide) {
                    mainFXController.getSpRightController().setDoubleLens(newValue);
                } else {
                    mainFXController.getSpMainController().setDoubleLens(newValue);
                }
            }

        });
        
    }

    public void initialSlidingWindowOptions() {

        moveStep.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                SearchUtility.boxTransferStep = (double) newValue;
            }
        });

        scaleStep.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                SearchUtility.boxScaleStep = (double) newValue;
            }
        });

        sizeMax.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                SearchUtility.boxScaleEnd = (double) newValue;
            }
        });

        sizeMin.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                SearchUtility.boxScaleStart = (double) newValue;
            }
        });

        maxModelDistance.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                SearchUtility.minModel = (double) newValue;
            }
        });

        maxShapeDistance.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                SearchUtility.minShape = (double) newValue;
            }
        });
    }

    public void setMainFXController(MainFXController mainFXController) {
        this.mainFXController = mainFXController;
    }

    public void setPosition(double x, double y) {
        stage.setX(x);
        stage.setY(y);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setVisible(boolean show) {
        if (!show) {
            stage.hide();
        } else {
            closeMenu.setSelected(true);
            stage.show();
        }
    }

    public void setRightSide(boolean rightSide) {
        this.rightSide = rightSide;
    }

    public boolean isRightSide() {
        return rightSide;
    }

}
