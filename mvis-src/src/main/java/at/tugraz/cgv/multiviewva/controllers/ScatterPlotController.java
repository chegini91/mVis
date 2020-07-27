/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.controllers;

import at.tugraz.cgv.multiviewva.gui.charts.Brushable;
import at.tugraz.cgv.multiviewva.gui.charts.DimBrushable;
import at.tugraz.cgv.multiviewva.gui.charts.ScatterPlotExtended;
import at.tugraz.cgv.multiviewva.gui.javaFXextension.ExtendedPath;
import at.tugraz.cgv.multiviewva.model.Dimension;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import at.tugraz.cgv.multiviewva.model.search.Box;
import at.tugraz.cgv.multiviewva.utility.ChartStyleUtility;
import at.tugraz.cgv.multiviewva.utility.GraphicUtility;
import at.tugraz.cgv.multiviewva.utility.Recommender;
import at.tugraz.cgv.multiviewva.utility.SearchUtility;
import at.tugraz.cgv.multiviewva.utility.math.Regression;
import at.tugraz.cgv.multiviewva.model.Item;
import at.tugraz.cgv.multiviewva.utility.BrushMngUtility;
import at.tugraz.cgv.multiviewva.utility.gazeLensUtility;
import at.tugraz.cgv.multiviewva.controllers.ZoomManager;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

/**
 * FXML Controller class
 *
 * @author mchegini
 */
public class ScatterPlotController implements Initializable, Brushable, DimBrushable {

    /**
     * regression model on the lens
     */
    Path regressionModel, regressionModelOther;

    private boolean boxMoving = false;

    private double searchIntersectionSlider = 50.0;

    /**
     * if the other lens should be shown
     */
    private boolean doubleLens = false;

    private boolean nodeText = true;
    double sceneX, sceneY;
    ExtendedPath onePath = new ExtendedPath();
    Point2D anchorPt;

    /**
     * if context menu (extended lens menu) is visible
     */
    private boolean contextMenuVisible = false;
    Rectangle selectBox = new Rectangle(0, 0, 0, 0);

    private boolean updateIsFinished = true;
    /**
     * this variable is useful to know after click release which button was
     * clicked
     */
    boolean isPrimButtDown = false;

    /**
     * if the controller is for right or left side
     */
    boolean righSide = false;
    //for rec moving
    int touchId = -1;
    double touchx, touchy;
    //for rectangle movement
    boolean isSecondButton = false;

    /**
     * for scaling from edge
     */
    private boolean mouseRightSide, mouseUpSide = false;

    /**
     * is user zoomed in or not
     */
    private boolean zoomedIn = true;

    //parent controller
    private MainFXController parentController;

    /**
     * list of selected nodes in scatter plot
     */
    ObservableList<XYChart.Data<Number, Number>> selectedPoints;

    /**
     * max and min of selectedPoints (bound) 0 -> minX 1 -> maxX 2-> minY 3->
     * maxY
     */
    double[] selectedPointsBound = new double[4];

    /**
     * previous selectedPoints set
     */
    private ObservableSet<Integer> prevSelectedItems = FXCollections.observableSet(new HashSet<Integer>());

    Label xLabel = new Label("X");
    Label yLabel = new Label("Y");

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        BrushMngUtility.dimBrushables.add(this);
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        scatter = new ScatterPlotExtended(xAxis, yAxis);
        createLabels();

        selectedPoints = FXCollections.observableArrayList();

        scatterPane.getChildren().add(onePath);

        if (ChartStyleUtility.showBoundingBox) {
            scatterPane.getChildren().add(selectBox);
        }

        this.initScatterPlot();
        scatterPane.getStylesheets().add("/styles/regression.css");
        scatterPane.getProperties().put("focusArea", "true");

        BrushMngUtility.brushables.add(this);

        scatterPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isShiftDown()) {
                    zoomingEvent(true, false);
                }
            }
        });

//        tsneInit();
    }

    public void zoomingEvent(boolean zoom, boolean reset) {
        //zoom out
        if (zoomedIn) {
            zoomedIn = false;
            resetPlotAxisBound();
        } else if (zoom && calculateSelectedPointBound()) {
            //zoom in
            System.out.println("zoom zoom zoooooooooom");
            setPlotAxisBound(selectedPointsBound[0] - 0.05, selectedPointsBound[1] + 0.05,
                    selectedPointsBound[2] - 0.05, selectedPointsBound[3] + 0.05);
            zoomedIn = true;
//            removeRect();
            updateUI(true, false);
        }

    }

    public void createLabels() {
        //init the normal scatterplot
        xLabel.setPadding(new Insets(0, 15, 0, 15));
        yLabel.setPadding(new Insets(0, 15, 0, 15));
        yLabel.setRotate(-90);
        VBox xbox = new VBox(5, new Group(xLabel));
        VBox ybox = new VBox(5, new Group(yLabel));
        xbox.setAlignment(Pos.CENTER);
        ybox.setAlignment(Pos.CENTER);
        scatterPane.setLeft(ybox);
        ChartStyleUtility.setLabelStyle(yLabel, true);
        ChartStyleUtility.setLabelStyle(xLabel, true);
        BorderPane.setAlignment(yLabel, Pos.CENTER);
        scatterPane.setBottom(xbox);
        BorderPane.setAlignment(xbox, Pos.CENTER);
        BorderPane.setMargin(xbox, new Insets(0, 0, 10, 0));
        BorderPane.setMargin(scatter, new Insets(0, 0, 0, 0));
        BorderPane.setMargin(ybox, new Insets(0, 0, 0, 10));
        scatterPane.setCenter(scatter);

        //brushing dimensions
        xbox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String labelTemp = xLabel.getText();
                if (SearchUtility.dataModel.getDimensionByName(labelTemp).isActiveML()) {
                    BrushMngUtility.brushDimension(SearchUtility.dataModel.getDimensionByName(labelTemp),
                            false);
                    SearchUtility.dataModel.getDimensionByName(labelTemp).setActiveML(false);
                } else {
                    SearchUtility.dataModel.getDimensionByName(labelTemp).setActiveML(true);
                    BrushMngUtility.brushDimension(SearchUtility.dataModel.getDimensionByName(labelTemp),
                            true);
                }
            }

        });

        //brushing dimensions
        ybox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String labelTemp = yLabel.getText();
                if (SearchUtility.dataModel.getDimensionByName(labelTemp).isActiveML()) {
                    BrushMngUtility.brushDimension(SearchUtility.dataModel.getDimensionByName(labelTemp),
                            false);
                    SearchUtility.dataModel.getDimensionByName(labelTemp).setActiveML(false);
                } else {
                    SearchUtility.dataModel.getDimensionByName(labelTemp).setActiveML(true);
                    BrushMngUtility.brushDimension(SearchUtility.dataModel.getDimensionByName(labelTemp),
                            true);
                }
            }

        });
    }

    /**
     * initialize regression models
     */
    public void initRegressionModels() {
        //add current rm
        Color primeColor, secondColor;
        primeColor = (righSide) ? GraphicUtility.regressionLineOtherColor : GraphicUtility.regressionLineColor;
        secondColor = (righSide) ? GraphicUtility.regressionLineColor : GraphicUtility.regressionLineOtherColor;

        regressionModel = new Path();

        regressionModel.setStroke(primeColor);
        regressionModel.setStrokeWidth(GraphicUtility.regressionLineStroke);
        scatterPane.getChildren().add(regressionModel);

        //add other regression model
        regressionModelOther = new Path();
        regressionModelOther.getStrokeDashArray().addAll(3.0, 7.0, 3.0, 7.0);
        regressionModelOther.setStroke(secondColor);
        regressionModelOther.setStrokeWidth(GraphicUtility.regressionOtherLineStroke);
        scatterPane.getChildren().add(regressionModelOther);

        /**
         * add listener so when the curve change the other scatter plot knows
         */
        if (GraphicUtility.doubleUser) {
            curve.addListener((ListChangeListener.Change<? extends Double> c) -> {
                if (regressionModel.getElements().size() > 0) {
                    if (righSide) {
                        parentController.getSpMainController().signalRegression();
                    } else {
                        parentController.getSpRightController().signalRegression();
                    }
                }
            });
        }
    }

    @FXML
    private BorderPane scatterPane;

    private ScatterPlotExtended scatter;

    @FXML
    private void handleMouseClicked(MouseEvent event) {

        if (event.isShiftDown()) {
            return;
        }
        if (GraphicUtility.searching) {
            if (pointOutOfBox(selectBox, event.getX(), event.getY(), false)) {
                removeRect();
                startPath(event.getX(), event.getY(), false);
            }

        } else if (GraphicUtility.labeling) {
            startPath(event.getX(), event.getY(), false);
        }
    }

    @FXML
    private void handleMouseReleased(MouseEvent event) {
        if (event.isShiftDown()) {
            return;
        }

        if (GraphicUtility.searching) {
            if (!boxMoving) {
                createSelectBox();
                contextMenuVisible = true;
                updateUI(true, false);
            }
            isPrimButtDown = false;
        } else if (GraphicUtility.labeling) {
            updateLabelledList();
            clearPath();
        }
    }

    @FXML
    private void handleMouseDragged(MouseEvent event) {

        if (event.isShiftDown()) {
            return;
        }

        if (GraphicUtility.searching) {
            if (!boxMoving) {
                drawPath(event.getX(), event.getY(), event
                        .getSceneX(), event.getSceneY(), false);
            }
        } else if (GraphicUtility.labeling) {
            drawPath(event.getX(), event.getY(), event
                    .getSceneX(), event.getSceneY(), false);
        }
    }

    /**
     * It is good to call this method after any updates in the UI
     *
     * @param box
     * @param changeRegDegree if the degree of regression changed
     */
    public void updateUI(boolean box, boolean changeRegDegree) {

        updateSelectedPoints(box, changeRegDegree);

        if (box) {
            onePath.clearPathPoints();
            onePath.getElements().clear();
        }

        //update context menu
        if (!contextMenuVisible) {
            if (righSide) {
                parentController.getRightMenuController().setVisible(false);
            } else if (!righSide) {
                parentController.getLeftMenuController().setVisible(false);
            }
        } else {
            if (righSide) {
                parentController.getRightMenuController().setVisible(true);
//                parentController.getRightMenuController().setPosition(selectBox.localToScreen(selectBox.getBoundsInLocal()).getMinX() - 400,
//                        selectBox.localToScreen(selectBox.getBoundsInLocal()).getMinY());
            } else {
                parentController.getLeftMenuController().setVisible(true);
//                parentController.getLeftMenuController().setPosition(selectBox.localToScreen(selectBox.getBoundsInLocal()).getMinX() - 400,
//                        selectBox.localToScreen(selectBox.getBoundsInLocal()).getMinY());
            }
        }

    }

    /**
     * init selectedpointsBound
     */
    public void initSelectedPointsBound() {
        selectedPointsBound = new double[4];
        selectedPointsBound[0] = Double.MAX_VALUE;
        selectedPointsBound[1] = Double.MIN_VALUE;
        selectedPointsBound[2] = Double.MAX_VALUE;
        selectedPointsBound[3] = Double.MIN_VALUE;
    }

    public boolean calculateSelectedPointBound() {
        //if any point is selected
        initSelectedPointsBound();
        if (selectedPoints.size() < 1) {
            return false;
        }
        selectedPoints.stream().forEach(sp -> {
            double x = sp.getXValue().doubleValue();
            double y = sp.getYValue().doubleValue();
            if (x < selectedPointsBound[0]) {
                selectedPointsBound[0] = x;
            }
            if (x > selectedPointsBound[1]) {
                selectedPointsBound[1] = x;
            }
            if (y < selectedPointsBound[2]) {
                selectedPointsBound[2] = y;
            }
            if (y > selectedPointsBound[3]) {
                selectedPointsBound[3] = y;
            }
        });
        return true;
    }

    /**
     * init the text (lable)
     */
    public void initText() {
        scatter.initText(parentController.getSpmModel());
    }

    /**
     * This method is called in updateUI and highlight selected points also add
     * or remove selected points from selectedPoints List
     */
    private void updateSelectedPoints(boolean box, boolean changeRegDegree) {

        //check if selectBox is not too small
//        if (selectBox.getWidth() > 10) {
        if (updateIsFinished) {
            updateIsFinished = false;
            int prevSize = parentController.getSpmModel().getModel().getSelectedItems().size();
            ObservableList<XYChart.Series<Number, Number>> list = scatter.getData();
            selectedPoints.clear();
            list.stream().forEach((o) -> {
                for (int i = 0; i < o.getData().size(); i++) {
                    o.getData().get(i).getNode().setStyle(null);
                    o.getData().get(i).getNode().setEffect(null);
                    if (box) {
                        if (selectBox.getWidth() > 10) {
                            Bounds boundsInScene = o.getData().get(i).getNode().localToScene(o.getData().get(i).getNode().getBoundsInLocal());
                            if (!scatterDataOutOfSelectedBox(selectBox, boundsInScene.getMaxX(), boundsInScene.getMaxY())
                                    || !scatterDataOutOfSelectedBox(selectBox, boundsInScene.getMaxX(), boundsInScene.getMinY())
                                    || !scatterDataOutOfSelectedBox(selectBox, boundsInScene.getMinX(), boundsInScene.getMaxY())
                                    || !scatterDataOutOfSelectedBox(selectBox, boundsInScene.getMinX(), boundsInScene.getMinY())) {
                                if (GraphicUtility.eyeFish) {
                                    double factor = lenseEffectFactor(selectBox, boundsInScene.getMaxX(), boundsInScene.getMaxY());
                                    o.getData().get(i).getNode().setStyle(" -fx-scale-x:" + factor + "; -fx-scale-y:" + factor + ";");
                                }
                                //calculate bounding of selected points
                                selectedPoints.add(o.getData().get(i));

                            } else {
                                BoxBlur bb = new BoxBlur();
                                bb.setWidth(2);
                                bb.setHeight(2);
                                bb.setIterations(2);
//                        if (MainFXController.effects) {
                                o.getData().get(i).getNode().setEffect(bb);
                                o.getData().get(i).getNode().setStyle("-fx-fill:rgb(193,193,193);");
//                                o.getData().get(i).getNode().setStyle(null);
//                        }
                            }
                        }
//                Bounds bounds = rec.localToScene(rec.getBoundsInLocal());
                    } else {
                        if (onePath.getPoints().size() > 0 && selectBox.getWidth() > 10) {
                            Bounds boundsInScene = o.getData().get(i).getNode().localToScene(o.getData().get(i).getNode().getBoundsInLocal());
                            if (!onePath.pointInsidePath(new Point2D(boundsInScene.getMaxX(), boundsInScene.getMinY()))) {
                                BoxBlur bb = new BoxBlur();
                                bb.setWidth(2);
                                bb.setHeight(2);
                                bb.setIterations(2);
                                o.getData().get(i).getNode().setEffect(bb);
                            } else {
                                if (GraphicUtility.eyeFish) {
                                    double factor = lenseEffectFactor(selectBox, boundsInScene.getMaxX(), boundsInScene.getMaxY());
                                    o.getData().get(i).getNode().setStyle(" -fx-scale-x:" + factor + "; -fx-scale-y:" + factor + ";");
                                }
                                selectedPoints.add(o.getData().get(i));
                            }
                        }
                    }

                }
            });

            parentController.getSpmModel().getModel().getSelectedItems().clear();

            selectedPoints.stream().forEach(pt -> {
                parentController.getSpmModel().getModel().getSelectedItems().add((Integer) pt.getExtraValue());
            });

            //check if degree of regression changed or more than 3 points are different in new lens
            if (changeRegDegree || (prevSize != parentController.getSpmModel().getModel().getSelectedItems().size()
                    && parentController.getSpmModel().getModel().getSelectedItems().size() > 0)) {
                if (BrushMngUtility.brushing) {
                    BrushMngUtility.redrawSelectedAll();
                }
                drawRegressionModel(curveDegree);
                if (GraphicUtility.doubleUser) {
                    if (doubleLens) {
                        drawOtherRegressionModel();
                    } else {
                        signalClearRegression();
                    }
                }
            }

            //add text to pionts
//            scatter.addTextGroupNode(selectedPoints);
            updateIsFinished = true;
        }

    }

    public void setRighSide(boolean righSide) {
        this.righSide = righSide;
    }

    public void setDoubleLens(boolean doubleLens) {
        this.doubleLens = doubleLens;
        updateUI(true, false);
    }

    public boolean isDoubleLens() {
        return doubleLens;
    }

    public ObservableList<Double> getCurve() {
        return curve;
    }

    public int getCurveDegree() {
        return curveDegree;
    }

    private ObservableList<Double> curve = FXCollections.observableArrayList();
    private int curveDegree = 1;

    /**
     * draw the regression model in the other scatter plot
     */
    public void drawOtherRegressionModel() {
        double width = selectBox.getWidth();
        double height = -1 * selectBox.getHeight();
        if (righSide && parentController.getSpMainController().getCurve().size() > 0) {
            getRegressionPathPoints(width, height, parentController.getSpMainController().getCurve(),
                    parentController.getSpMainController().getCurveDegree(), 100.5d, regressionModelOther);
        } else if (!righSide && parentController.getSpRightController().getCurve().size() > 0) {
            getRegressionPathPoints(width, height, parentController.getSpRightController().getCurve(),
                    parentController.getSpRightController().getCurveDegree(), 100.5d, regressionModelOther);
        }
    }

    /**
     * draw regression model on the plot
     *
     * @param degree of regression model (from 1 to 4)
     */
    public void drawRegressionModel(int degree) {
        ObservableList<XYChart.Data<Number, Number>> normalizePoints = Box.normalizePoints(selectedPoints);
        double[] tempArray;
        curve.clear();
        switch (degree) {
            case 1:
                tempArray = Regression.calculateLinearRegression(Box.getPoints2D(normalizePoints), false);
                for (int i = 0; i < tempArray.length; i++) {
                    curve.add(tempArray[i]);
                }
                curveDegree = 1;
                break;
            case 2:
                tempArray = Regression.calculateQuadRegression(Box.getPoints2D(normalizePoints), false);
                for (int i = 0; i < tempArray.length; i++) {
                    curve.add(tempArray[i]);
                }
                curveDegree = 2;
                break;
            case 3:
                tempArray = Regression.calculateCubicRegression(Box.getPoints2D(normalizePoints), false);
                for (int i = 0; i < tempArray.length; i++) {
                    curve.add(tempArray[i]);
                }
                curveDegree = 3;
                break;
            case 4:
                tempArray = Regression.calculate4DegreeRegression(Box.getPoints2D(normalizePoints), false);
                for (int i = 0; i < tempArray.length; i++) {
                    curve.add(tempArray[i]);
                }
                curveDegree = 4;
                break;
        }

        double width = selectBox.getWidth();
        double height = -1 * selectBox.getHeight();

        getRegressionPathPoints(width, height, curve, curveDegree, 100.5d, regressionModel);

        //reg end
        //draw regression model of other lens
    }

    /**
     * when the regression model change, inform the other plot to change the
     * second (other) regression model
     */
    public void signalRegression() {
        double width = selectBox.getWidth();
        double height = -1 * selectBox.getHeight();
        if (righSide && parentController.getSpMainController().isDoubleLens()) {
            getRegressionPathPoints(width, height, parentController.getSpMainController().getCurve(),
                    parentController.getSpMainController().getCurveDegree(), 100.5d, regressionModelOther);
        } else if (!righSide && parentController.getSpRightController().isDoubleLens()) {
            getRegressionPathPoints(width, height, parentController.getSpRightController().getCurve(),
                    parentController.getSpRightController().getCurveDegree(), 100.5d, regressionModelOther);
        }
    }

    public void signalClearRegression() {
        regressionModelOther.getElements().clear();
    }

    /**
     * get a mathematics curve and return physical points of the regression in
     * the box on screen
     *
     * @param width width of the box
     * @param height height of the box
     * @param curve the mathematics curve
     * @param degree degree of regression model
     * @param range number of points in regression model. higher number result
     * better curve
     * @param regressionModel the path that the regression model will be added
     * to
     */
    public void getRegressionPathPoints(double width, double height, ObservableList<Double> curve, int degree, double range, Path regressionModel) {
        double nextP, tempX, tempY = 0;
        regressionModel.getElements().clear();
        //draw regression model of current SP
        for (double i = 0.0d; i < range; i++) {
            nextP = i / 100;
            switch (degree) {
                case 1:
                    if (curve.size() == 2) {
                        tempY = Math.pow(nextP, 1) * curve.get(0) + curve.get(1);
                    }
                    break;
                case 2:
                    if (curve.size() == 3) {
                        tempY = Math.pow(nextP, 2) * curve.get(0) + Math.pow(nextP, 1) * curve.get(1) + curve.get(2);
                        break;
                    }
                case 3:
                    if (curve.size() == 4) {
                        tempY = Math.pow(nextP, 3) * curve.get(0) + Math.pow(nextP, 2) * curve.get(1) + Math.pow(nextP, 1) * curve.get(2) + curve.get(3);
                    }
                    break;
                case 4:
                    if (curve.size() == 5) {
                        tempY = Math.pow(nextP, 4) * curve.get(0) + Math.pow(nextP, 3) * curve.get(1) + Math.pow(nextP, 2) * curve.get(2) + Math.pow(nextP, 1) * curve.get(3) + curve.get(4);
                    }
                    break;
            }
            tempX = nextP * width + selectBox.getX();
            //onePath.getMinWidth();
            tempY = tempY * height + selectBox.getY() + selectBox.getHeight();
            //onePath.getMaxHeight();
            if (i < 0.5) {
                regressionModel.getElements().add(new MoveTo(tempX, tempY));
            } else {
                regressionModel.getElements().add(new LineTo(tempX, tempY));
            }
        }
    }

    public XYChart.Series<Number, Number> getSketch() {
        ObservableList<XYChart.Data<Number, Number>> temp = FXCollections.observableArrayList();
        onePath.getPoints().forEach(pnt -> {
            temp.add(new XYChart.Data<>(pnt.getX(), onePath.getMinHeight() + onePath.getMaxHeight() - onePath.getMinHeight() - pnt.getY()));
        });

        at.tugraz.cgv.multiviewva.model.search.Box b = new Box(SearchUtility.numbOfRow, SearchUtility.numOfCol, temp);
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.getData().addAll(b.getPoints());
        return series;
    }

    /**
     * search for local patterns
     */
    public void searchForLocalPatterns() {
        if (selectedPoints.size() > 0) {
            at.tugraz.cgv.multiviewva.model.search.Box b = new Box(SearchUtility.numbOfRow, SearchUtility.numOfCol, selectedPoints);
            Recommender.adjustVars();
            parentController.searchWrapper(b);
        }
//        Recommender.resetRecommender();
    }

    ArrayList<Box> temp = new ArrayList<>();

    /**
     * This function return a lense factor between 1 and 2. First the function
     * calculates the distance between a node and middle of rectangle. Then
     * normalize the distance and at the end return the factor for further use.
     *
     * @param rec
     * @param x
     * @param y
     * @return
     */
    private double lenseEffectFactor(Rectangle rec, double x, double y) {
        //calculate distance
        Bounds bounds = rec.localToScene(rec.getBoundsInLocal());
        double midRecX = bounds.getMinX() + (rec.getWidth()) / 2;
        double midRecY = bounds.getMinY() + (rec.getHeight()) / 2;
        double maxDistance = Math.sqrt(Math.pow((bounds.getMaxX() - midRecX), 2) + Math.pow((bounds.getMaxY() - midRecY), 2));
        double distance = Math.sqrt(Math.pow((x - midRecX), 2) + Math.pow((y - midRecY), 2));

        double temp = maxDistance - distance;
        double con = 4;
        if (temp < 2) {
            temp = 2;
        }

        if (maxDistance - distance > 0) {
            //normalize to 0-1
            double tempMin = (con * 2) / maxDistance;
            double tempMax = (con * (maxDistance)) / maxDistance;
            double zi = (con * (temp)) / maxDistance;
            double temp2 = (zi - tempMin) / (tempMax - tempMin);
            //normalize to 1-4
            return ((temp2 + 1) * 1.1) - 0.1;
        } else {
            return 1;
        }

    }

    /**
     * This method creates a selectBox after the mouse is released
     */
    private void createSelectBox() {
        //create regression line
//                Line line = new Line(onePath.getMinWidth(), onePath.getMinHeight(), onePath.getMaxWidth(),
//                onePath.getMaxHeight());
        selectBox = new Rectangle(onePath.getMinWidth(), onePath.getMinHeight(), onePath.getMaxWidth() - onePath.getMinWidth(),
                onePath.getMaxHeight() - onePath.getMinHeight());
        selectBox.setFill(Color.TRANSPARENT);
        selectBox.setStroke(GraphicUtility.selectedBoxColor);
        selectBox.setStrokeWidth(1);
        selectBox.setVisible(true);
        selectBox.getStrokeDashArray().addAll(25d, 20d, 5d, 20d);

        if (ChartStyleUtility.showBoundingBox) {
            scatterPane.getChildren().add(selectBox);
        }

        /*uncomment for having scroll feature
         //This is for scaling the box with two hands
         selectBox.setOnScrollStarted(new EventHandler<ScrollEvent>() {

         @Override
         public void handle(ScrollEvent event) {
         //check if the user use more than 2 fingers

         if (!isSecondButton && event.getTouchCount() > 1) {
         mouseRightSide = false;
         mouseUpSide = false;
         //set the user touch which half
         if (event.getX() > selectBox.getX() + selectBox.getWidth() / 2.0) {
         mouseRightSide = true;
         }
         if (event.getY() < selectBox.getY() + selectBox.getWidth() / 2.0) {
         mouseUpSide = true;
         }
         }
         }

         });
         //do the scaling for the box when user use two fingers
         selectBox.setOnScroll(new EventHandler<ScrollEvent>() {

         @Override
         public void handle(ScrollEvent event) {
         if (!isSecondButton && event.getTouchCount() > 1) {
         if (!mouseUpSide) {
         selectBox.setHeight(selectBox.getHeight() + event.getDeltaY());
         } else {
         double height = selectBox.getHeight();
         double y = selectBox.getY();
         selectBox.setY(y + event.getDeltaY());
         selectBox.setHeight(height - event.getDeltaY());
         }
         if (mouseRightSide) {
         selectBox.setWidth(selectBox.getWidth() + event.getDeltaX());
         } else {
         double width = selectBox.getWidth();
         double x = selectBox.getX();
         selectBox.setX(x + event.getDeltaX());
         selectBox.setWidth(width - event.getDeltaX());
         }
         if (GraphicUtility.spEffects) {
         updateUI(true);
         }
         }

         }

         });
        


         selectBox.setOnScrollFinished(new EventHandler<ScrollEvent>() {

         @Override
         public void handle(ScrollEvent event) {
         updateUI(true);
         }

         });
         */
        //mouse event handler
        if (!GraphicUtility.touch) {
            if (GraphicUtility.searching) {
                EventHandler<MouseEvent> circleOnMousePressedEventHandler = new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        if (event.isSecondaryButtonDown()) {
                            touchx = event.getX()
                                    - selectBox.getX();
                            touchy = event.getY() - selectBox.getY();
                            selectBox.setFill(Color.TRANSPARENT);
                            selectBox.setStroke(GraphicUtility.selectedBoxColor);
                            selectBox.setStrokeWidth(3);
                            isSecondButton = true;
                        }
                    }
                };
                selectBox.setOnMousePressed(circleOnMousePressedEventHandler);

                // //draging on select box (rect) event
                EventHandler<MouseEvent> circleOnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        if (isSecondButton) {
                            selectBox.setX(event.getX() - touchx);
                            selectBox.setY(event.getY() - touchy);
                        }

                        if (GraphicUtility.spDynamicEffects) {

                            updateUI(true, false);
                        }
                    }
                };
                selectBox.setOnMouseDragged(circleOnMouseDraggedEventHandler);
                EventHandler<MouseEvent> circleOnMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        if (isSecondButton) {
                            selectBox.setFill(Color.TRANSPARENT);
                            selectBox.setStroke(GraphicUtility.selectedBoxColor);
                            selectBox.setStrokeWidth(2);
                            isSecondButton = false;
                            updateUI(true,
                                    false);
                        }
                    }
                };
                selectBox.setOnMouseReleased(circleOnMouseReleasedEventHandler);
                //end of mouse events
            } else if (GraphicUtility.labeling) {

            }
        } else if (GraphicUtility.touch) {
            selectBox.setOnTouchStationary((TouchEvent event) -> {
                touchx = event.getTouchPoint().getX() - selectBox.getX();
                touchy = event.getTouchPoint().getY() - selectBox.getY();
                selectBox.setFill(Color.TRANSPARENT);
                selectBox.setStroke(GraphicUtility.selectedBoxColor);
                selectBox.setStrokeWidth(3);
                isSecondButton = true;
                event.consume();
            });

            selectBox.setOnTouchMoved((TouchEvent event) -> {
                boxMoving = true;
                rateRight++;
                rateLeft++;
                if ((righSide && rateRight % 3 == 0) || (!righSide && rateLeft % 3 == 0)) {
                    if (boxMoving) {
                        selectBox.setX(event.getTouchPoint().getX() - touchx);
                        selectBox.setY(event.getTouchPoint().getY() - touchy);
                    }
                    if (GraphicUtility.spDynamicEffects) {
                        updateUI(true, false);
                    }
                }
                event.consume();
            });

            selectBox.setOnTouchReleased((TouchEvent event) -> {

                rateLeft = 0;
                rateRight = 0;
                ScaleTransition st = new ScaleTransition(Duration.millis(100), selectBox);
                st.setByX(0.05f);
                st.setByY(0.05f);
                st.setAutoReverse(true);
                st.setCycleCount(2);
                st.play();
                boxMoving = false;
                event.consume();
            });
        }

//
//        //creat animation for selectBox
//        ScaleTransition st = new ScaleTransition(Duration.millis(100), selectBox);
//        st.setByX(0.05f);
//        st.setByY(0.05f);
//        st.setAutoReverse(true);
//        st.setCycleCount(2);
//        st.play();
//
    }

    public static int rateRight = -1, rateLeft = -1;

    private void clearPath() {
        onePath.clearPathPoints();
        onePath.getElements().clear();
    }

    private void startPath(double x, double y, boolean touch) {
        if (touch) {
            x = x - onePath.getParent().localToScene(onePath.getParent().getBoundsInLocal()).getMinX();
            y = y - onePath.getParent().localToScene(onePath.getParent().getBoundsInLocal()).getMinY();
        }
        onePath.clearPathPoints();
        onePath.getElements().clear();
        anchorPt = new Point2D(x, y);      // start point in path
        onePath.setStrokeWidth(1);
        onePath.setStroke(GraphicUtility.selectionPathColor);
        onePath.getElements()
                .add(new MoveTo(anchorPt.getX(), anchorPt.getY()));
    }

    private void drawPath(double x, double y, double screenX, double screenY, boolean touch) {
        if (touch) {
            x = x - onePath.getParent().localToScene(onePath.getParent().getBoundsInLocal()).getMinX();
            y = y - onePath.getParent().localToScene(onePath.getParent().getBoundsInLocal()).getMinY();
        }
        onePath.getElements().add(new LineTo(x, y));
        onePath.addPoint(new Point2D(x, y), new Point2D(screenX, screenY));

    }

    private void initScatterPlot() {

        if (GraphicUtility.spEffects) {
            final DropShadow shadow = new DropShadow();
            shadow.setOffsetX(2);
            shadow.setColor(Color.GREY);
        }

        //comment for performance concerns
        if (!GraphicUtility.spEffects) {
            scatter.setAnimated(false);
        } else {
            scatter.setAnimated(true);
        }

        scatter.setLegendVisible(false);

    }

    public static boolean scatterDataOutOfSelectedBox(Rectangle rec, double x, double y) {
        Bounds bounds = rec.localToScene(rec.getBoundsInLocal());
        double MaxWidth = bounds.getMinX() + rec.getWidth();
        double MinWidth = bounds.getMinX();
        double MaxHeight = bounds.getMinY() + rec.getHeight();
        double MinHeight = bounds.getMinY();
        if (x > MinWidth && x < MaxWidth && y > MinHeight && y < MaxHeight) {
            return false;
        }
        return true;
    }

    /**
     * Determine if a specific point is out of a specific rectangle or not
     *
     * @param rec
     * @param x
     * @param y
     * @return
     */
    public boolean pointOutOfBox(Rectangle rec, double x, double y, boolean touch) {
        if (touch) {
            x = x - onePath.getParent().localToScene(onePath.getParent().getBoundsInLocal()).getMinX();
            y = y - onePath.getParent().localToScene(onePath.getParent().getBoundsInLocal()).getMinY();

        }
        double MaxWidth = rec.getX() + rec.getWidth();
        double MinWidth = rec.getX();
        double MaxHeight = rec.getY() + rec.getHeight();
        double MinHeight = rec.getY();
        if (x > MinWidth && x < MaxWidth && y > MinHeight && y < MaxHeight) {
            return false;
        }
        return true;
    }

    /**
     * This function get a list of data series and replace current scatterplot
     * data with them
     *
     * @param seriesList : Is a ObservableList of data series from small scatter
     * plots of "scatter plot matrix" section
     * @param xAxis
     * @param yAxis
     */
    public void changeDataOnPlot(ObservableList<XYChart.Series<Number, Number>> seriesList, String xAxis, String yAxis) {
        //first should remove all the current data
        removeCurrentData();
        //then add the new data to the plot
        //this loop is responsible for creating a new series and add it to\
        //scatter plot

        //adding svgpath as a node to every point
        seriesList.stream().forEach((series) -> {
            series.getData().forEach(pt -> {
                pt.setNode(new SVGPath());
            });
        });

        seriesList.stream().forEach((series) -> {
            scatter.getData().add(series);
        });

        //add tooltps for node
        ChartStyleUtility.addListenersRecords(scatter.getData());

        if (!selectedPoints.isEmpty()) {
            redrawSelected();
        }

        xLabel.setText(xAxis);
        yLabel.setText(yAxis);

        setAxisFormatter((NumberAxis) scatter.getXAxis(), xAxis);
        setAxisFormatter((NumberAxis) scatter.getYAxis(), yAxis);

        redrawCategories();
//        removeTextNodes();

        //init zoom? not sure how it works yet
        final Rectangle zoomRect = new Rectangle();
        zoomRect.setManaged(false);
        zoomRect.setFill(Color.LIGHTSEAGREEN.deriveColor(0, 1, 1, 0.5));
        scatterPane.getChildren().add(zoomRect);

//        setUpZooming(zoomRect, scatter);
//        new ZoomManager(scatterPane, scatter, seriesList);
    }

    private void setUpZooming(final Rectangle rect, final Node zoomingNode) {
        final ObjectProperty<Point2D> mouseAnchor = new SimpleObjectProperty<>();
        zoomingNode.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isControlDown()) {
                    mouseAnchor.set(new Point2D(event.getX(), event.getY()));
                    rect.setWidth(0);
                    rect.setHeight(0);
                }
            }
        });
        zoomingNode.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isControlDown()) {
                    double x = event.getX();
                    double y = event.getY();
                    rect.setX(Math.min(x, mouseAnchor.get().getX()));
                    rect.setY(Math.min(y, mouseAnchor.get().getY()));
                    rect.setWidth(Math.abs(x - mouseAnchor.get().getX()));
                    rect.setHeight(Math.abs(y - mouseAnchor.get().getY()));
                }
            }
        });

        zoomingNode.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isControlDown()) {
//                    doZoom(rect, (ScatterPlotExtended<Number, Number>) zoomingNode);
                    if (calculateSelectedPointBound()) {
                        setPlotAxisBound(selectedPointsBound[0] - 0.05, selectedPointsBound[1] + 0.05,
                                selectedPointsBound[2] - 0.05, selectedPointsBound[3] + 0.05);
                        zoomedIn = true;
                        updateUI(true, false);
                    }
                    System.out.println("zoom happend?");
                }
            }
        });
    }

    private void doZoom(Rectangle zoomRect, ScatterPlotExtended<Number, Number> chart) {
        Point2D zoomTopLeft = new Point2D(zoomRect.getX(), zoomRect.getY());
        Point2D zoomBottomRight = new Point2D(zoomRect.getX() + zoomRect.getWidth(), zoomRect.getY() + zoomRect.getHeight());
        final NumberAxis yAxis = (NumberAxis) chart.getYAxis();
        Point2D yAxisInScene = yAxis.localToScene(0, 0);
        final NumberAxis xAxis = (NumberAxis) chart.getXAxis();
        Point2D xAxisInScene = xAxis.localToScene(0, 0);
        double xOffset = zoomTopLeft.getX() - yAxisInScene.getX();
        double yOffset = zoomBottomRight.getY() - xAxisInScene.getY();
        double xAxisScale = xAxis.getScale();
        double yAxisScale = yAxis.getScale();
        xAxis.setLowerBound(xAxis.getLowerBound() + xOffset / xAxisScale);
        xAxis.setUpperBound(xAxis.getLowerBound() + zoomRect.getWidth() / xAxisScale);
        yAxis.setLowerBound(yAxis.getLowerBound() + yOffset / yAxisScale);
        yAxis.setUpperBound(yAxis.getLowerBound() - zoomRect.getHeight() / yAxisScale);
        System.out.println(yAxis.getLowerBound() + " " + yAxis.getUpperBound());
        zoomRect.setWidth(0);
        zoomRect.setHeight(0);
    }

    /**
     * add all necessary listeners to records (aka points)
     */
    /*
    public void addListenersRecords(ObservableList<XYChart.Series<Number, Number>> seriesList) {

        ObservableList<XYChart.Series<Number, Number>> list = scatter.getData();
        //for temp highlighting, on hover and on hover exit
        list.stream().forEach(series -> {

            //highlight when enter
            series.getData().forEach(pt -> {

                pt.getNode().setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        System.out.println(SearchUtility.dataModel.getOriginalLables()
                                .get((Integer) pt.getExtraValue()));
                    }
                });

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

     */
    /**
     * set formatter for axis
     *
     * @param axis
     * @param name of the axis
     */
    public void setAxisFormatter(NumberAxis axis, String name) {
        axis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(axis) {
            @Override
            public String toString(Number value) {
                // if the lower and upper bound were negated, the displayed value has to be fixed
                double val = value.doubleValue();
                double high = SearchUtility.dataModel.getDimensionByName(name).getMinMax()[1];
                double low = SearchUtility.dataModel.getDimensionByName(name).getMinMax()[0];
                val = (double) (val * (high - low) + low);
                //if low and high are close
                if (high - low < 1) {
                    return super.toString(val);
                } else if (high - low > 1 && high - low < 10) {
                    return String.format("%.2f", val);
                } else {
                    return String.format("%.0f", val);
                }
            }
        });
    }

    public void setContextMenuVisible(boolean contextMenuVisible) {
        this.contextMenuVisible = contextMenuVisible;
    }

    public void setParentController(MainFXController ctrl) {
        this.parentController = ctrl;
    }

    public void removeCurrentData() {
        int size = scatter.getData().size();
        scatter.getData().clear();
    }

    /**
     * change the regression degree (curve degree) of the controller for the
     * lens
     *
     * @param degree
     */
    public void setCurveDegree(int degree) {
        curveDegree = degree;
        updateUI(true, true);
    }

    /**
     * This function removes the current rectangle
     */
    public void removeRect() {
        scatterPane.getChildren().remove(selectBox);
        selectBox = new Rectangle(0, 0, 0, 0);
        selectBox.setVisible(false);
        onePath.clearPathPoints();
        onePath.getElements().clear();

        //remove regression lens
        regressionModel.getElements().clear();
        regressionModelOther.getElements().clear();

        if (righSide) {
            parentController.getSpMainController().signalClearRegression();
        } else {
            parentController.getSpRightController().signalClearRegression();
        }

        curve.clear();

        //clear context menu
        contextMenuVisible = false;
        if (righSide) {
            parentController.getRightMenuController().setVisible(false);
        } else {
            parentController.getLeftMenuController().setVisible(false);
        }
    }

    /**
     * set upper and lower bound of axis in the plot
     *
     * @param xLowerBound
     * @param xUpperBound
     * @param yLowerBound
     * @param yUpperBound
     */
    public void setPlotAxisBound(double xLowerBound, double xUpperBound, double yLowerBound, double yUpperBound) {
        scatter.getXAxis().setAutoRanging(false);
        scatter.getYAxis().setAutoRanging(false);
        ((NumberAxis) scatter.getXAxis()).setLowerBound(xLowerBound);
        ((NumberAxis) scatter.getXAxis()).setUpperBound(xUpperBound);
        ((NumberAxis) scatter.getYAxis()).setLowerBound(yLowerBound);
        ((NumberAxis) scatter.getYAxis()).setUpperBound(yUpperBound);
    }

    public void setSearchIntersectionSlider(double searchIntersectionSlider) {
        this.searchIntersectionSlider = searchIntersectionSlider;
    }

    public double getSearchIntersectionSlider() {
        return searchIntersectionSlider;
    }

    /**
     * with this function zoom out to actual axis bound can happen
     */
    public void resetPlotAxisBound() {
        scatter.getXAxis().setAutoRanging(true);
        scatter.getYAxis().setAutoRanging(true);
    }

    @Override
    public void redrawSelected() {
        drawAllBackground();
        ObservableList<XYChart.Series<Number, Number>> list = scatter.getData();
        selectedPoints.clear();
        SearchUtility.parentController.getSpmModel().getModel().getSelectedItems().forEach(item -> {
            outerloop:
            for (XYChart.Series<Number, Number> o : list) {
                for (int i = 0; i < o.getData().size(); i++) {
                    if (o.getData().get(i).getExtraValue().equals(item)) {
                        o.getData().get(i).getNode().setStyle(null);
                        o.getData().get(i).getNode().setEffect(null);
                        selectedPoints.add(o.getData().get(i));
                        break outerloop;
                    }
                }
            }
        });
    }

    @Override
    public void drawAllBackground() {
        ObservableList<XYChart.Series<Number, Number>> list = scatter.getData();
        list.stream().forEach((o) -> {
            for (int i = 0; i < o.getData().size(); i++) {
                BoxBlur bb = new BoxBlur();
                bb.setWidth(2);
                bb.setHeight(2);
                bb.setIterations(2);
                o.getData().get(i).getNode().setEffect(bb);
                o.getData().get(i).getNode().setStyle("-fx-fill:rgb(193,193,193);");
            }
        });
    }

    /**
     * set style of the record
     */
    private void setRecordStyle(XYChart.Data<Number, Number> data, boolean deactivate) {
        Bounds boundsInScene = data.getNode().localToScene(data.getNode().getBoundsInLocal());
        //outside
        if (deactivate) {
            BoxBlur bb = new BoxBlur();
            bb.setWidth(2);
            bb.setHeight(2);
            bb.setIterations(2);
            data.getNode().setEffect(bb);
            data.getNode().setStyle(" -fx-fill:rgb(193,193,193);");

        } else {
            selectedPoints.add(data);
            data.getNode().setStyle(null);
            data.getNode().setEffect(null);
        }
    }

    private void updateUtilityAfterSelection(ObservableList<XYChart.Series<Number, Number>> list) {
        parentController.getSpmModel().getModel().getSelectedItems().clear();

        if (selectedPoints.size() == 0) {
            list.stream().forEach((o) -> {
                for (int i = 0; i < o.getData().size(); i++) {
                    selectedPoints.add(o.getData().get(i));
                    o.getData().get(i).getNode().setStyle(null);
                    o.getData().get(i).getNode().setEffect(null);
                }
            });
        }
        selectedPoints.stream().forEach(pt -> {
            parentController.getSpmModel().getModel().getSelectedItems().add((Integer) pt.getExtraValue());
        });
    }

    public void updateLabelledListGaze() {

        selectedPoints.clear();
        ObservableList<XYChart.Series<Number, Number>> list = scatter.getData();

        list.stream().forEach(series -> {

            series.getData().forEach(pt -> {
                pt.getNode().setStyle(null);
                pt.getNode().setEffect(null);
                ((SVGPath) pt.getNode()).setScaleX(1);
                ((SVGPath) pt.getNode()).setScaleY(1);
                Point2D p = pt.getNode().localToScene(0.0, 0.0);
                double x = p.getX()
                        + pt.getNode().getScene().getX() + pt.getNode().getScene().getWindow().getX();
                double y = p.getY()
                        + pt.getNode().getScene().getY() + pt.getNode().getScene().getWindow().getY();
                if (Math.abs(SearchUtility.parentController.getGazePos().getCenterX() - x) < gazeLensUtility.lensSize
                        && Math.abs(SearchUtility.parentController.getGazePos().getCenterY() - y) < gazeLensUtility.lensSize) {
                    //lens factor
                    double factor = 2;
                    ((SVGPath) pt.getNode()).setScaleX(factor);
                    ((SVGPath) pt.getNode()).setScaleY(factor);
                    setRecordStyle(pt, false);
                } else {
                    setRecordStyle(pt, true);
                }
            });
        });

        updateUtilityAfterSelection(list);

//        parentController.getSpmModel().getModel().getDimensionSelected().clear();
//        parentController.getSpmModel().getModel().getDimensionSelected().add(xLabel.getText());
//        parentController.getSpmModel().getModel().getDimensionSelected().add(yLabel.getText());
        if (BrushMngUtility.brushing) {
            BrushMngUtility.redrawSelectedAll();
        }
    }

    /**
     * update the selected data after every selection
     */
    private void updateLabelledList() {
        selectedPoints.clear();
        ObservableList<XYChart.Series<Number, Number>> list = scatter.getData();
        list.stream().forEach((o) -> {
            for (int i = 0; i < o.getData().size(); i++) {
                o.getData().get(i).getNode().setStyle(null);
                o.getData().get(i).getNode().setEffect(null);
                if (onePath.getPoints().size() > 0) {
                    Bounds boundsInScene = o.getData().get(i).getNode().localToScene(o.getData().get(i).getNode().getBoundsInLocal());
                    //outside
                    if (!onePath.pointInsidePath(new Point2D(boundsInScene.getMaxX(), boundsInScene.getMinY()))) {
                        setRecordStyle(o.getData().get(i), true);
                    } else {
                        setRecordStyle(o.getData().get(i), false);
                    }
                }
            }
        });

        updateUtilityAfterSelection(list);

        parentController.getSpmModel().getModel().getDimensionSelected().clear();
        parentController.getSpmModel().getModel().getDimensionSelected().add(xLabel.getText());
        parentController.getSpmModel().getModel().getDimensionSelected().add(yLabel.getText());

        if (BrushMngUtility.brushing) {
            BrushMngUtility.redrawSelectedAll();
        }

    }

    @Override
    public void redrawCategories() {
        //for scatterplot
        ObservableList<XYChart.Series<Number, Number>> list = scatter.getData();
        list.stream().forEach(series -> {

            series.getData().forEach(pt -> {
                ChartStyleUtility.coloringNodeUtility(pt, false);
            });
        });
    }

    @Override
    public void brushAllDim() {
        //TODO
    }

    @Override
    public void brushDimension(Dimension dimensions, boolean highligh) {
        if (xLabel.getText().equals(dimensions.getName())) {
            if (highligh) {
                ChartStyleUtility.setLabelStyle(xLabel, true);
            } else {
                ChartStyleUtility.setLabelStyle(xLabel, false);
            }
        }

        if (yLabel.getText().equals(dimensions.getName())) {
            if (highligh) {
                ChartStyleUtility.setLabelStyle(yLabel, true);
            } else {
                ChartStyleUtility.setLabelStyle(yLabel, false);
            }
        }
    }

    @Override
    public void unbrushAll() {
        //TODO
    }

    /**
     * for when the focus of gaze is on portion of the scatterplot
     */
    public void focusGaze() {
        ObservableList<XYChart.Series<Number, Number>> list = scatter.getData();

        list.stream().forEach(series -> {

            series.getData().forEach(pt -> {

                Tooltip t = (Tooltip) pt.getNode().getProperties().get("Tooltip");
                t.setAutoHide(true);
                Point2D p = pt.getNode().localToScene(0.0, 0.0);
                double x = p.getX()
                        + pt.getNode().getScene().getX() + pt.getNode().getScene().getWindow().getX();
                double y = p.getY()
                        + pt.getNode().getScene().getY() + pt.getNode().getScene().getWindow().getY();
                if (Math.abs(SearchUtility.parentController.getGazePos().getCenterX() - x) < 50
                        && Math.abs(SearchUtility.parentController.getGazePos().getCenterY() - y) < 50) {

                    pt.getNode().getProperties().put("Tooltip", t);
                    t.show(pt.getNode(), p.getX()
                            + pt.getNode().getScene().getX() + pt.getNode().getScene().getWindow().getX(), p.getY()
                            + pt.getNode().getScene().getY() + pt.getNode().getScene().getWindow().getY() + 12);
                }
            });
        });

    }

    /**
     * a record will become bigger when highlighted
     *
     * @param highlight
     * @param item
     */
    @Override
    public void highLightRecordTemp(boolean highlight, Item item) {

        ObservableList<XYChart.Series<Number, Number>> list = scatter.getData();
        list.stream().forEach(series -> {
            series.getData().forEach(pt -> {
                if ((Integer) pt.getExtraValue() == item.getIndex()) {
                    if (highlight) {
                        ChartStyleUtility.coloringNodeUtility(pt, true);
                        return;
                    } else {
                        ChartStyleUtility.coloringNodeUtility(pt, false);
                        return;
                    }
                }
            });
        });

    }

}
