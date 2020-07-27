/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.gui.charts;

import at.tugraz.cgv.multiviewva.controllers.LabelPaneController;
import at.tugraz.cgv.multiviewva.model.LabelModel;
import at.tugraz.cgv.multiviewva.utility.BrushMngUtility;
import at.tugraz.cgv.multiviewva.utility.SearchUtility;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
//import com.jfoenix.controls.JFXTextField;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author mohammad
 */
public class LabelArea extends AnchorPane {

    private TextField label;
    private Label count;
    private JFXButton delete;
    private JFXButton edit;
    private JFXButton add;
    private JFXButton visible;
    private JFXButton done;
    private JFXButton clear;
    private LabelModel model;
    private ColorPicker colorPicker;

    public LabelArea(LabelModel model, LabelPaneController controller, boolean automatic) {
        super();
        this.model = model;
        this.setPadding(new Insets(5, 10, 0, 10));
//        this.setMinHeight(70);
        this.setStyle("-fx-border-width: 2 2 4 2;"
                + "-fx-text-fill: #5D8CA6;"
                + "-fx-background-color: #F6F6F6;"
                + "-fx-border-color: #EBEBEB #EBEBEB" + model.getColor() + "#EBEBEB;");

        //label init
        label = new TextField(model.getName());
        label.prefWidthProperty().bind(this.widthProperty().divide(3.0));
        if (model.getName().equalsIgnoreCase("unknown")) {
            label.setDisable(true);
        }

        //count init
        count = new Label(Integer.toString(model.getItems().size()));

        //add dimensions to the label just if it is not created automatically
        if (!automatic || model.getName().equalsIgnoreCase("unknown")) {
            model.getDimeInteraction().addAll(
                    SearchUtility.parentController.getSpmModel().getModel().getDimensionSelected());
        }

        for (String name : model.getDimeInteraction()) {
            SearchUtility.dataModel.getDimensionByName(name).setActiveML(true);
            BrushMngUtility.brushDimension(SearchUtility.dataModel.getDimensionByName(name), true);
        }

        HBox labels = new HBox();
        labels.getChildren().addAll(label, count);
        labels.setAlignment(Pos.CENTER_LEFT);
//        AnchorPane.setLeftAnchor(labels, 0.0);
//        AnchorPane.setTopAnchor(labels, 1.0);

        //button add
        try {
            Image addImg = new Image("/icons/1x/add.png");
            add = new JFXButton("", new ImageView(addImg));
        } catch (Exception e) {
        }

        //when adding new records to the label (labelling records)
        add.setOnMouseClicked(event -> {
            //add dimensions that the user interacted with
            if (!model.getName().equalsIgnoreCase("unknown")) {
                model.getDimeInteraction().addAll(
                        SearchUtility.parentController.getSpmModel().getModel().getDimensionSelected());
            }
            SearchUtility.parentController.getSpmModel().getModel().updateCategories(model);
            for (String name : model.getDimeInteraction()) {
                SearchUtility.dataModel.getDimensionByName(name).setActiveML(true);
                BrushMngUtility.brushDimension(SearchUtility.dataModel.getDimensionByName(name), true);
            }

            controller.update();
            //add dimensions to the label that the user interacted with while selecting

        });

        //button visible
        Image onImage = new Image("/icons/1x/on.png");
        Image offImage = new Image("/icons/1x/off.png");
        visible = new JFXButton("", new ImageView(onImage));
        visible.setOnMouseClicked((MouseEvent event) -> {
            if (model.isVisible()) {
                model.setVisible(false);
                visible.setGraphic(new ImageView(offImage));
            } else {
                model.setVisible(true);
                visible.setGraphic(new ImageView(onImage));
            }
            controller.update();
        });

        //button delete
        Image deleteImg = new Image("/icons/1x/delete.png");
        delete = new JFXButton("", new ImageView(deleteImg));
        delete.setOnMouseClicked(event -> {
            if (!model.getName().equalsIgnoreCase("unknown")) {
                SearchUtility.parentController.getSpmModel().getModel().deleteCategory(model);
                controller.deleteCategory(model);
                controller.update();
            }
        });
        if (model.getName().equalsIgnoreCase("unknown")) {
            delete.setDisable(true);
        }

        //button edit
        Image editImg = new Image("/icons/1x/edit.png");
        edit = new JFXButton("", new ImageView(editImg));

        colorPicker = new ColorPicker(Color.web(model.getColor()));
        colorPicker.setMaxWidth(20);
        colorPicker.setMaxHeight(20);
        colorPicker.setOnAction(event -> {
            String t = String.format("#%02X%02X%02X",
                    (int) (colorPicker.getValue().getRed() * 255),
                    (int) (colorPicker.getValue().getGreen() * 255),
                    (int) (colorPicker.getValue().getBlue() * 255));
            model.setColor(t);
            controller.update();
        });

        Image doneImg = new Image("/icons/1x/done.png");
        done = new JFXButton("", new ImageView(doneImg));
        done.setOnMouseClicked(event -> {
            SearchUtility.parentController.getSpmModel().getModel().updateApprovedData(model);
            controller.update();
        });
        if (model.getName().equalsIgnoreCase("unknown")) {
            done.setDisable(true);
        }

        Image clearImg = new Image("/icons/1x/clear.png");
        clear = new JFXButton("", new ImageView(clearImg));
        clear.setOnMouseClicked(event -> {
            SearchUtility.parentController.getSpmModel().getModel().updateClearData(model);
            controller.update();
        });
        if (model.getName().equalsIgnoreCase("unknown")) {
            clear.setDisable(true);
        }

        HBox buttons = new HBox(clear, done, delete, visible, add, colorPicker);
        buttons.setAlignment(Pos.CENTER_LEFT);
        AnchorPane.setRightAnchor(buttons, 0.0);

        this.getChildren().addAll(labels, buttons);

    }

    public TextField getLabel() {
        return label;
    }

    public Label getCount() {
        return count;
    }

    public LabelModel getModel() {
        return model;
    }

    public void deleteAllLabels() {
        this.getChildren().forEach(node -> {
            this.getChildren().remove(node);
        });
    }
}
