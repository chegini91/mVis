/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.controllers;

import at.tugraz.cgv.multiviewva.gui.charts.CustomChart;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javax.imageio.ImageIO;
import at.tugraz.cgv.multiviewva.utility.SearchUtility;
import javafx.scene.chart.XYChart;

/**
 * FXML Controller class
 *
 * @author mchegini
 */
public class MaximizeChartController implements Initializable {

    @FXML
    AnchorPane maxAnchor;

    @FXML
    AnchorPane mainAnchor;

    @FXML
    Label yAxis;

    @FXML
    Label xAxis;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        maxAnchor.setStyle("-fx-background-color: #FFFFFF");
        mainAnchor.setStyle("-fx-background-color: #FFFFFF");
    }

    /**
     * get a chart and add it to the anchorPane
     *
     * @param chart
     */
    public void addChart(CustomChart chart) {
        CustomChart cs = new CustomChart(chart, "maximize");
        maxAnchor.getChildren().add(cs);
        yAxis.setText(cs.getyAxis());
        xAxis.setText(cs.getxAxis());
        cs.combinePoints();
        cs.setLayoutX(10);
        cs.setLayoutY(10);
        xAxis.setFont(new Font(25));
        yAxis.setFont(new Font(25));

        maxAnchor.setOnMouseClicked(event -> {
            WritableImage image = mainAnchor.snapshot(new SnapshotParameters(), null);

            // TODO: probably use a file chooser here
            File file = new File(cs.getxAxis() + "_" + cs.getyAxis() + "_scatterplot.png");

//            try {
//                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
//            } catch (IOException e) {
//                // TODO: handle exception here
//            }
        });

//
//        if (SearchUtility.lastSearchQuery != null) {
////            cs.drawAllRec("model");
//            cs.searchScatterPlot(SearchUtility.lastSearchQuery, "model");
//        }
//        cs.getData().addAll(chart.getData());
//        cs.drawPoints();
//        cs.setMinHeight(0.0);
//        cs.setPrefHeight(400);
//        cs.setMinWidth(0.0);
//        cs.setPrefWidth(400);
//        cs.combinePoints();
    }

    @FXML
    public void saveAsPng() {

    }
}
