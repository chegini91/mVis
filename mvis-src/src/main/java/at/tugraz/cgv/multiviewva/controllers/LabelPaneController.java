/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.controllers;

import at.tugraz.cgv.multiviewva.gui.charts.LabelArea;
import at.tugraz.cgv.multiviewva.model.LabelModel;
import at.tugraz.cgv.multiviewva.utility.BrushMngUtility;
import at.tugraz.cgv.multiviewva.utility.ChartStyleUtility;
import at.tugraz.cgv.multiviewva.utility.MLUtility;
import at.tugraz.cgv.multiviewva.utility.SearchUtility;
import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXSlider;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author mohammad
 */
public class LabelPaneController implements Initializable {

    @FXML
    private VBox vbox;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    ScrollPane scrollPane = new ScrollPane();

    JFXButton addLabelBtn = new JFXButton("Add Partition");
    //    JFXButton alBtn = new JFXButton("Active Learning");
    JFXCheckBox alCheckBox = new JFXCheckBox("Active Learning");

    //slider for tuning similarity of classses to pass classification
    JFXSlider classSimilaritySlider = new JFXSlider(0, 100, 0);
    JFXButton classificationBtn = new JFXButton("Classification");
    JFXButton clusteringBtn = new JFXButton("Clustering");

    final Spinner<Integer> spinnerAL = new Spinner(2, 50, 20);

    final Spinner<Integer> spinnerCl = new Spinner(2, 20, 4);

    //number of clusters to create
//    ObservableList<Integer> numbers
//            = FXCollections.observableArrayList(
//                    2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20
//            );
//    JFXComboBox<Integer> clusterNumCombo = new JFXComboBox(numbers);
    HBox hbox;
    HBox classHbox;
    HBox clusterHbox;
    HBox activeLeaBox;

    /**
     * all the labels
     */
    ObservableList<LabelArea> labelsArrayList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vbox.setStyle("-fx-background-color:WHITE");
        anchorPane.setStyle("-fx-background-color:WHITE");
        scrollPane.setStyle("-fx-background-color:transparent;");
//        anchorPane.getChildren().add(scrollPane);
        scrollPane.setContent(vbox);
//        clusterNumCombo.getSelectionModel().select(numbers.get(0));
        addLabelBtn.setStyle("-fx-background-color: rgb(15, 157, 88); -fx-text-fill:WHITE;");
        addLabelBtn.setPrefWidth(125);

        classificationBtn.setStyle("-fx-background-color: #E0E0E0; -fx-text-fill:BLACK;");
        classificationBtn.setPrefWidth(125);

        clusteringBtn.setStyle("-fx-background-color: #E0E0E0; -fx-text-fill:BLACK;");
        clusteringBtn.setPrefWidth(125);

        alCheckBox.setPrefWidth(125);

    }


    /**
     * get bunch of label models and add them to the pane
     *
     * @param models
     */
    public void initLabelsBulk(ArrayList<LabelModel> models) {
        removeAllLabels();
        //add add label and refresh buttons
        hbox = new HBox(addLabelBtn);

        //classification box
        classSimilaritySlider.setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double value) {
                return String.format("%02d", value.longValue()) + "%";
            }

            @Override
            public Double fromString(String arg0) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

        SearchUtility.classSimilarity.bind(classSimilaritySlider.valueProperty());
        Label classThresholdLabel = new Label("Similarity Threshold");
        classSimilaritySlider.setPrefWidth(110);
        classHbox = new HBox(classificationBtn, classSimilaritySlider, classThresholdLabel);
        classHbox.setSpacing(10);
        classHbox.setAlignment(Pos.CENTER_LEFT);

        //clustering box
        Label spinnerClLabel = new Label("# of Clusters");
        clusterHbox = new HBox(clusteringBtn, spinnerCl, alCheckBox, spinnerClLabel);
        clusterHbox.setSpacing(10);
        clusterHbox.setAlignment(Pos.CENTER_LEFT);

        //clustering box
        spinnerCl.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {
                SearchUtility.numClusters.set((Integer) arg2);
            }
        });
        spinnerCl.setPrefWidth(60);

        Label spinnerALLabel = new Label("# of Suggestions");
        spinnerAL.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {
                MLUtility.activeLearningNumber = (Integer) arg2;
            }

        });
        alCheckBox.setSelected(true);
        activeLeaBox = new HBox(alCheckBox, spinnerAL, spinnerALLabel);
        activeLeaBox.setAlignment(Pos.CENTER_LEFT);
        spinnerAL.setPrefWidth(60);
        activeLeaBox.setSpacing(10);

//      labels alraedy existing
        vbox.getChildren().add(hbox);
        vbox.getChildren().add(classHbox);
        vbox.getChildren().add(clusterHbox);
        vbox.getChildren().add(activeLeaBox);
        for (LabelModel model : models) {
            LabelArea area = new LabelArea(model, this, true);
            labelsArrayList.add(area);
            vbox.getChildren().add(area);
            if (model.getName().equalsIgnoreCase("unknown")) {
                VBox.setMargin(area, new Insets(15, 0, 15, 0));
            }
            //set all loaded labels to true
            SearchUtility.parentController.getSpmModel().getModel().updateApprovedData(model);
        }
        addListeners();
        update();
    }

    /**
     * add listeners to add and refresh buttons
     */
    public void addListeners() {

        alCheckBox.setOnMouseClicked(event -> {
            SearchUtility.parentController.getSpmModel().getModel().updateAL();
            update();
        });

        addLabelBtn.setOnMouseClicked(event -> {

            LabelModel m = new LabelModel("Label " + Integer.toString(labelsArrayList.size() + 1),
                    ChartStyleUtility.colorsString.get(labelsArrayList.size() + 1));
            addLabel(m);
            SearchUtility.parentController.getSpmModel().getModel().getSelectedItems().clear();
//            BrushMngUtility.redrawSelectedAll();
        });

        classificationBtn.setOnMouseClicked(event -> {
            SearchUtility.parentController.getSpmModel().getModel().getSelectedItems().clear();
            SearchUtility.parentController.getSpmModel().getModel().updateClassificationData();
            update();
        });

        clusteringBtn.setOnMouseClicked(event -> {
            SearchUtility.parentController.getSpmModel().getModel().getSelectedItems().clear();
            int tempClusterCount = SearchUtility.parentController.getSpmModel().getModel().getClassNames().size();
            ArrayList<LabelModel> labels
                    = SearchUtility.parentController.getSpmModel().getModel().updateClustersData();
            addLabelsBulks(labels, true);
            update();

        });
    }

    /**
     * adding a new label
     *
     * @param model
     */
    public void addLabel(LabelModel model) {
        LabelArea area = new LabelArea(model, this, false);
        labelsArrayList.add(area);
        vbox.getChildren().add(area);
        SearchUtility.parentController.getSpmModel().getModel().addClassName(model);
        SearchUtility.parentController.getSpmModel().getModel().updateCategories(model);
        update();
    }

    /**
     * adding a new label (but not updating the model)
     *
     * @param model
     */
    public void addLabelInterface(LabelModel model, boolean automatic) {

        LabelArea area = new LabelArea(model, this, automatic);
        labelsArrayList.add(area);
        vbox.getChildren().add(area);
        SearchUtility.parentController.getSpmModel().getModel().updateCategories(model);
        update();
    }

    /**
     * update the UI (e.g. updating number of records in the clusters)
     */
    public void update() {

        BrushMngUtility.redrawAllCategories();
        //update the item list and then count
        labelsArrayList.forEach(label -> {
            List<ComplexDataObject> temp = SearchUtility.parentController.getSpmModel().getModel().getComplexObjList();
            int tempSize = temp.size();
            label.getModel().getItems().clear();
            for (int i = 0; i < tempSize; i++) {
                if (temp.get(i).getAttribute("class").equals(label.getModel().getName())) {
                    label.getModel().getItems().add(i);
                }
            }
            label.getCount().setText(Integer.toString(label.getModel().getItems().size()));

            //update color of labels
            label.setStyle(null);
            label.setStyle("-fx-border-width: 2 2 4 2;"
                    + "-fx-text-fill: #5D8CA6;"
                    + "-fx-background-color: #F6F6F6;"
                    + "-fx-border-color: #EBEBEB #EBEBEB" + label.getModel().getColor() + "#EBEBEB;");
            if (label.getModel().getName().equalsIgnoreCase("unknown")) {
                label.setStyle("-fx-border-width: 2 2 2 2;"
                        + "-fx-text-fill: #5D8CA6;"
                        + "-fx-background-color: #EDEDED;"
                        + "-fx-border-color: #EBEBEB #EBEBEB" + label.getModel().getColor() + "#EBEBEB;");
            }
        });

    }

    public void removeAllLabels() {
        vbox.getChildren().forEach(node -> {
            vbox.getChildren().remove(node);
        });
        labelsArrayList = FXCollections.observableArrayList();
    }

    /**
     * this one updates all the labels that are added to due clustering
     *
     * @param labels    list of labels to add to the label list
     * @param automatic if labels are automatically created by ML algorithsm
     */
    public void addLabelsBulks(ArrayList<LabelModel> labels, boolean automatic) {
        for (LabelModel label : labels) {
            addLabelInterface(label, automatic);
        }

    }

    /**
     * delete a category (graphical)
     */
    public void deleteCategory(LabelModel model) {
        for (int i = 0; i < labelsArrayList.size(); i++) {
            if (labelsArrayList.get(i).getModel().getName().equals(model.getName())) {
                vbox.getChildren().remove(labelsArrayList.get(i));
                labelsArrayList.remove(labelsArrayList.get(i));
                return;
            }
        }
    }

}
