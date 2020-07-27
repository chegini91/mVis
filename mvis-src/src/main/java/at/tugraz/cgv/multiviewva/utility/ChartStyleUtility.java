/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.utility;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import at.tugraz.cgv.multiviewva.model.LabelModel;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;

/**
 * @author mchegini
 */
public class ChartStyleUtility {

    /**
     * size of the circle in scatteplot
     */
    public static double circlSizeMainSP = 2.0;

    /**
     * for scatterplot matrix
     */
    public static double circleSize = 0.8d;
    /**
     * SHOW bounding box in scatter plot after data selection
     */
    public static boolean showBoundingBox = true;

    /**
     * colors of clusters
     */
    public static List<Color> colors = Collections.unmodifiableList(Arrays.asList(
            Colours.Red.getColorWeb(), Colours.Brown.getColorWeb(), Colours.Blue.getColorWeb(),
            Colours.Green.getColorWeb(), Colours.Orange.getColorWeb(), Colours.Purple.getColorWeb(),
            Colours.Lime.getColorWeb(), Colours.Olive.getColorWeb(), Colours.Yellow.getColorWeb(),
            Colours.Cyan.getColorWeb(), Colours.Lavender.getColorWeb(), Colours.Amber.getColorWeb(),
            Colours.Navy.getColorWeb(), Colours.Crismon.getColorWeb(), Colours.Mint.getColorWeb(),
            Colours.Pink.getColorWeb(),
            Colours.DarkPurple.getColorWeb(), Colours.LighIndigo.getColorWeb(), Colours.BlueGrey.getColorWeb()
    ));

    /**
     * colors of clusters
     */
    public static List<String> colorsString = Collections.unmodifiableList(Arrays.asList(
            Colours.Red.getHexHashtag(), Colours.Blue.getHexHashtag(),
            Colours.Green.getHexHashtag(), Colours.Orange.getHexHashtag(), Colours.Purple.getHexHashtag(),
            Colours.Lime.getHexHashtag(), Colours.Olive.getHexHashtag(), Colours.Yellow.getHexHashtag(),
            Colours.Cyan.getHexHashtag(), Colours.Lavender.getHexHashtag(), Colours.Amber.getHexHashtag(),
            Colours.Navy.getHexHashtag(), Colours.Crismon.getHexHashtag(), Colours.Mint.getHexHashtag(),
            Colours.Pink.getHexHashtag(),
            Colours.DarkPurple.getHexHashtag(), Colours.LighIndigo.getHexHashtag(), Colours.BlueGrey.getHexHashtag()
    ));

    /**
     * colors of general heatmap
     */
    public static List<Color> heatmap = Collections.unmodifiableList(Arrays.asList(Color.web("#800026"), Color.web("#bd0026"), Color.web("#e31a1c"),
            Color.web("#fc4e2a"), Color.web("#fd8d3c"), Color.web("#feb24c"), Color.web("#fed976"), Color.web("#ffeda0"), Color.web("#fed9a6"), Color.web("#ffffcc"), Color.web("#ffffcc")));
    /**
     * colors of shape heatmap
     */
    public static List<Color> heatmapShape = Collections.unmodifiableList(Arrays.asList(Color.web("#004529"), Color.web("#006837"), Color.web("#238443"),
            Color.web("#41ab5d"), Color.web("#78c679"), Color.web("#addd8e"), Color.web("#d9f0a3"), Color.web("#f7fcb9"), Color.web("#ffffe5"), Color.web("#ffffe5"), Color.web("#ffffe5")));
    /**
     * colors of model heatmap
     */
    public static List<Color> heatmapModel = Collections.unmodifiableList(Arrays.asList(Color.web("#49006a"), Color.web("#7a0177"), Color.web("#ae017e"),
            Color.web("#dd3497"), Color.web("#f768a1"), Color.web("#fa9fb5"), Color.web("#fcc5c0"), Color.web("#fde0dd"), Color.web("#fff7f3"), Color.web("#fff7f3"), Color.web("#fff7f3")));

    public static void setLabelStyle(Label label, boolean isActive) {
        if (isActive) {
            label.setStyle("-fx-border-width: 2 2 4 2;"
                    + "-fx-background-color: #F6F6F6;"
                    + "-fx-border-color: #EBEBEB #EBEBEB #0096C9 #EBEBEB;"
            );
        } else {
            label.setStyle("-fx-border-width: 2 2 4 2;"
                    + "-fx-background-color: #F6F6F6;"
                    + "-fx-border-color: #EBEBEB #EBEBEB #EBEBEB #EBEBEB;"
            );
        }
    }

    /**
     * @param pt        the record to color
     * @param highlight if highlighting is happening true, otherwise false
     */
    public static void coloringNodeUtility(XYChart.Data pt, boolean highlight) {
        pt.getNode().setStyle(null);
        pt.getNode().setEffect(null);
        String clsofCirc = (String) SearchUtility.parentController.getSpmModel().getModel().
                getComplexObjList().get((Integer) pt.getExtraValue()).getAttribute("class");

        //remove if the class is not visible
        if (!SearchUtility.parentController.getSpmModel().getModel().isClassVisible(clsofCirc)) {
            pt.getNode().setVisible(false);
            return;
        } else {
            pt.getNode().setVisible(true);
        }

        ((Shape) pt.getNode()).setFill(
                Color.web(SearchUtility.parentController.getSpmModel().getModel().colorofClass(clsofCirc)));

        //if data is labelled, then make a border, otherwise, don't
        if (SearchUtility.parentController.getSpmModel().getModel().getComplexObjList()
                .get((Integer) pt.getExtraValue()).getDescription().equals("true")) {

            if (DataLoadUtility.labellingDemo) {
                ((SVGPath) pt.getNode()).
                        setContent("M2,0 L5,4 L8,0 L10,0 L10,2 L6,5 L10,8 L10,10 L8,10 L5,6 L2,10 L0,10 L0,8 L4,5 L0,2 L0,0 Z");
            } else {
                ((SVGPath) pt.getNode()).setContent("M 4, 4 ,m -4, 0 ,a 4,4 0 1,0 8,0, a 4,4 0 1,0 -8,0");
            }
            ((SVGPath) pt.getNode()).setStroke(null);
            if (!highlight) {
                ((SVGPath) pt.getNode()).setScaleX(1.5);
                ((SVGPath) pt.getNode()).setScaleY(1.5);
            } else {
                ((SVGPath) pt.getNode()).setScaleX(3);
                ((SVGPath) pt.getNode()).setScaleY(3);
            }
        } else if (SearchUtility.parentController.getSpmModel().getModel().getComplexObjList()
                .get((Integer) pt.getExtraValue()).getAttribute("class").equals("unknown")) {
            //uknown   triangle
            ((SVGPath) pt.getNode()).setContent("M 5,0.5 9.5, 9.75 0.5, 9.75 z");
            ((SVGPath) pt.getNode()).setStroke(null);
            if (!highlight) {
                ((SVGPath) pt.getNode()).setScaleX(1.5);
                ((SVGPath) pt.getNode()).setScaleY(1.5);
            } else {
                ((SVGPath) pt.getNode()).setScaleX(3);
                ((SVGPath) pt.getNode()).setScaleY(3);
            }
        } else {
            if (DataLoadUtility.labellingDemo) {
                //circle solid
                ((SVGPath) pt.getNode()).setContent("M 4, 4 ,m -4, 0 ,a 4,4 0 1,0 8,0, a 4,4 0 1,0 -8,0");
            } else {
                //empty circle
                ((SVGPath) pt.getNode()).setContent("M 3, 3 ,m -3, 0 ,a 3,3 0 1,0 6,0, a 3,3 0 1,0 -6,0");
                ((SVGPath) pt.getNode()).setStroke(((SVGPath) pt.getNode()).getFill());
                ((SVGPath) pt.getNode()).setFill(null);
            }

            if (!highlight) {
                ((SVGPath) pt.getNode()).setScaleX(1.5);
                ((SVGPath) pt.getNode()).setScaleY(1.5);
                ((SVGPath) pt.getNode()).setStrokeWidth(3);
            } else {
                ((SVGPath) pt.getNode()).setStrokeWidth(5);
                ((SVGPath) pt.getNode()).setScaleX(2);
                ((SVGPath) pt.getNode()).setScaleY(2);
            }
            //cross
            //((SVGPath) pt.getNode()).setContent("M2,0 L5,4 L8,0 L10,0 L10,2 L6,5 L10,8 L10,10 L8,10 L5,6 L2,10 L0,10 L0,8 L4,5 L0,2 L0,0 Z");

        }
//        ((SVGPath) pt.getNode()).setScaleX(3);
//        ((SVGPath) pt.getNode()).setScaleY(3);
    }

    /**
     * add all necessary listeners to records (aka points)
     */
    public static void addListenersRecords(ObservableList<XYChart.Series<Number, Number>> list) {

//        ObservableList<XYChart.Series<Number, Number>> list = scatter.getData();
        //for temp highlighting, on hover and on hover exit
        list.stream().forEach(series -> {

            //highlight when enter
            series.getData().forEach(pt -> {

                pt.getNode().setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        //label one single data point (record)
                        if (DataLoadUtility.labellingDemo) {

                            if (DataLoadUtility.labellingDemo) {

                                //show a dialog to the user to confirm the label
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Labelling a Record");
                                alert.setHeaderText(SearchUtility.parentController.getSpmModel().getModel().getNodeByIndex((Integer) pt.getExtraValue()).getName()
                                        + " -> " +
                                        (String) SearchUtility.dataModel.getOriginalLables().get((Integer) pt.getExtraValue()));
                                alert.setContentText("Please confirm the label for the selected record.");

                                Optional<ButtonType> result = alert.showAndWait();
                                if (result.get() == ButtonType.OK) {
                                    //manage data model
                                    SearchUtility.dataModel.singleItemClickHandeling((Integer) pt.getExtraValue());
                                    //graphic handling
                                    SearchUtility.parentController.getLabelPaneController().update();
                                    pt.getNode().toFront();
                                    alert.close();
                                } else {
                                    alert.close();
                                }
                            }

                        }
                    }
                });

//                pt.getNode().setOnMouseReleased(mouseEvent -> {
//
//                });

                pt.getNode().setOnMouseEntered(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
//                        System.out.println(SearchUtility.dataModel.getSelectedItems().size());
                        //check if the record is in selected
                        if (SearchUtility.dataModel.getSelectedItems().isEmpty()
                                || SearchUtility.dataModel.getSelectedItems().contains((Integer) pt.getExtraValue())) {
                            BrushMngUtility.highLightRecordTemp(true,
                                    SearchUtility.parentController.getSpmModel().getModel().getItembyIndex((Integer) pt.getExtraValue()));
                        }
                        //add tooltps for node
                        if (GraphicUtility.onHoverScatterplot) {
                            Tooltip t = new Tooltip(
                                    SearchUtility.parentController.getSpmModel().getModel().getNodeByIndex((Integer) pt.getExtraValue()).getName()
                            );
                            pt.getNode().getProperties().put("Tooltip", t);
                            GraphicUtility.hackTooltipStartTiming(t);
                            Tooltip.install(pt.getNode(), t);
                        }

                    }
                });

                //unhighlight when exit
                pt.getNode().setOnMouseExited(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (SearchUtility.dataModel.getSelectedItems().isEmpty()
                                || SearchUtility.dataModel.getSelectedItems().contains((Integer) pt.getExtraValue())) {
                            BrushMngUtility.highLightRecordTemp(false,
                                    SearchUtility.parentController.getSpmModel().getModel().getItembyIndex((Integer) pt.getExtraValue()));
                        }
                    }
                });
            });
        });
    }
}
