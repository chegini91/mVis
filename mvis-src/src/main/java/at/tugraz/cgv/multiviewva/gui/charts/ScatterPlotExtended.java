/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.gui.charts;

import at.tugraz.cgv.multiviewva.model.SPMModel;
import at.tugraz.cgv.multiviewva.utility.SearchUtility;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author mohammad
 * @param <X>
 * @param <Y>
 */
public class ScatterPlotExtended<X, Y> extends ScatterChart<X, Y> {

    public ScatterPlotExtended(Axis<X> xAxis, Axis<Y> yAxis) {
        super(xAxis, yAxis);
    }

    /**
     * init the text (lable)
     *
     * @param spmModel
     */
    public void initText(SPMModel spmModel) {
        ObservableList<XYChart.Series<X, Y>> list = this.getData();
        list.stream().forEach((o) -> {
            for (int i = 0; i < o.getData().size(); i++) {
                addTextNode(o.getData().get(i));
            }
        });
    }

    /**
     * add text to a specific node
     *
     * @param data node
     */
    public void addTextNode(Data<X, Y> data) {
        Bounds boundsInScene = data.getNode().localToParent(data.getNode().getBoundsInLocal());
        Text text = new Text(boundsInScene.getMinX(),
                boundsInScene.getMinY() - 5,
                SearchUtility.parentController.getSpmModel().getModel().getNodeByIndex((Integer) data.getExtraValue()).getName());
        text.setFont(new Font(10));
        this.getPlotChildren().add(text);

    }

    public void addTextGroupNode(ObservableList<Data<X, Y>> points) {

        points.stream().forEach(point -> {
            addTextNode(point);
        });

    }

    public void addLineToChart(Data<X, Y> a, Data<X, Y> b) {
        Bounds boundsA = a.getNode().localToScene(a.getNode().getBoundsInLocal());
//        Bounds boundsB = b.getNode().localToScene(b.getNode().getBoundsInLocal());
//        Line line = new Line(boundsA.getMinX(), boundsA.getMinY(), boundsB.getMinX()+100, boundsB.getMinY());
//        System.out.println(boundsA + " " +  boundsB);
//        this.getPlotChildren().add(line);

        double yShift = boundsA.getMinY();
        // set x parameters of the valueMarker to chart area bounds
        Line line = new Line(20, 20, boundsA.getMinX() - 10, boundsA.getMinY() - 10);
        this.getPlotChildren().add(line);
    }
    

}
