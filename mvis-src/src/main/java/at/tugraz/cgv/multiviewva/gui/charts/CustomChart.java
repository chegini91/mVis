/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.gui.charts;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import at.tugraz.cgv.multiviewva.model.search.Box;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import at.tugraz.cgv.multiviewva.utility.ChartStyleUtility;
import at.tugraz.cgv.multiviewva.utility.Recommender;
import at.tugraz.cgv.multiviewva.utility.SearchUtility;
import at.tugraz.cgv.multiviewva.model.Item;

/**
 * These customCharts are used in a scatter plot matrices (SPLOMs)
 *
 * @author mchegini
 */
public class CustomChart extends Region implements Brushable {

    /**
     * value of distance to sketch
     */
    private double distanceToSketch = -1;

    //set to true if the chart is selected at least once
    private boolean alreadySelected = false;

    private Tooltip tooltip = new Tooltip("N/A");
    /**
     * just logger of the class
     */
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(CustomChart.class.getName());

    /**
     * whether the chart is maximized or not
     */
    private CustomChart maximised = null;

    /**
     * type of the custom chart (maximized, ranking, SPLOM etc.)
     */
    private String type = "SPLOM";

    /**
     * list of chart clones (e.g., maximized or on ranking system)
     */
    private ObservableList<CustomChart> cloneList = FXCollections.observableArrayList();

    /**
     * position of the plot in SPLOMs
     */
    private Point position = new Point();

    /**
     * all founded boxes (rec) in the plot, [0]: 2d, [1]: model [2]: confirm
     */
    private ObservableList<Shape>[] foundPatterns = new ObservableList[3];

    /**
     * sub plots (boxes) in a chart
     */
    ArrayList<Box> subPlots = new ArrayList<>();

    /**
     * scatter plot region (around all points)
     */
    Rectangle rect = new Rectangle();

    /**
     * data points
     */
    private ObservableList<XYChart.Series<Number, Number>> data = FXCollections.observableArrayList();

    /**
     * X axis name of the plot
     */
    private String xAxis = "";

    /**
     * y axis name of the plot
     */
    private String yAxis = "";

    /**
     * circles (real position of the points) mapped with data point of them
     */
    ObservableMap<XYChart.Data<Number, Number>, Circle> circles = FXCollections.observableHashMap();

    private boolean[] patternHighLighted;

    public ObservableList<XYChart.Series<Number, Number>> getData() {
        return data;
    }

    /**
     * copy constructor
     *
     * @param origChart
     * @param type like maximize, ranking etc.
     */
    public CustomChart(CustomChart origChart, String type) {
        this(origChart.getPosition().x, origChart.getPosition().y, origChart.getxAxis(), origChart.getyAxis());
        this.getData().addAll(origChart.getData());
        this.drawPoints();
        this.setMinHeight(0.0);
        this.setPrefHeight(400);
        this.setMinWidth(0.0);
        this.setPrefWidth(400);
        this.combinePoints();
        this.data = origChart.getData();
        this.distanceToSketch = origChart.getDistanceToSketch();
//        this.foundPatterns =  origChart.getFoundPatterns();
        this.patternHighLighted = origChart.getPatternHighLighted();
        this.points = origChart.getPoints();
        this.rect = origChart.getRect();
        this.subPlots = origChart.getSubPlots();

        circles.values().stream().forEach((c) -> {
            c.setRadius(4);
        });
        this.setLayoutX(10);
        this.setLayoutY(10);
        redrawSelected();

        switch (type) {
            case "maximize":
                origChart.maximised = this;
                type = "maximized";
                break;
        }

    }

    public CustomChart(int x, int y, String xAxis, String yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        applyShadow();
        setScatterStyle();
        //set hotness listener
        setHotnessChangeListener();
        //
        rect.setFill(Color.rgb(255, 255, 255));
        rect.setStroke(Color.rgb(170, 170, 170));
        this.getChildren().add(rect);
        this.position.setLocation(x, y);
//        Line[] l1 = new Line[10];
//        for (int i = 0; i < 10; i++) {
//            l1[i] = new Line(i * 10, 0, i * 10, 100);
//            this.getChildren().add(l1[i]);
//            l1[i].setFill(Color.rgb(221, 221, 221));
//            l1[i].setStroke(Color.rgb(221, 221, 221));
//            l1[i].endYProperty().bind(this.heightProperty());
//            l1[i].getStyleClass().setAll("chart-vertical-grid-lines");
//        }
        normalizePointsPosition();

        //init foundPatterns
        foundPatterns[0] = FXCollections.observableArrayList();
        foundPatterns[1] = FXCollections.observableArrayList();
        foundPatterns[2] = FXCollections.observableArrayList();

    }

    public Point getPosition() {
        return position;
    }

    /**
     * Draw all the points on the rectangle, the points is always between 0 and
     * 1
     */
    public void drawPoints() {
        int size = this.getData().size();
        patternHighLighted = new boolean[size];
        for (int i = 0; i < size; i++) {
            patternHighLighted[i] = false;
            for (int j = 0; j < this.getData().get(i).getData().size(); j++) {
                Circle c = new Circle(ChartStyleUtility.circleSize);
                c.centerXProperty().bind(rect.widthProperty()
                        .multiply(this.getData().get(i).getData().get(j).XValueProperty().getValue().doubleValue()));
                c.centerYProperty().bind(rect.heightProperty().subtract(rect.heightProperty()
                        .multiply(this.getData().get(i).getData().get(j).YValueProperty().getValue().doubleValue())));

                c.getProperties().put("index", this.getData().get(i).getData().get(j).getExtraValue());
                c.getProperties().put("colorIndex", i);
                //TODO: add more colors
                if (i > 7) {
                    c.setFill(ChartStyleUtility.colors.get(0));
                } else {
                    c.setFill(ChartStyleUtility.colors.get(i));
                }
                //add the circle and data to hash map
                circles.put(this.getData().get(i).getData().get(j), c);
                //add circle to the scene
                c.toFront();
                this.getChildren().add(c);
            }
        }

    }

    public void normalizePointsPosition() {
        rect.widthProperty().bind(this.widthProperty().subtract(10));
        rect.heightProperty().bind(this.heightProperty().subtract(10));
    }

    public ObservableList<XYChart.Data<Number, Number>> getPoints() {
        return points;
    }

    private BooleanProperty selected = new SimpleBooleanProperty(false);

    //combined points
    ObservableList<XYChart.Data<Number, Number>> points = FXCollections.observableArrayList();

    // Define a getter for the property's value
    public final boolean getSelected() {
        return selected.get();
    }

    public Tooltip getTooltip() {
        return tooltip;
    }

    public String getxAxis() {
        return xAxis;
    }

    public String getyAxis() {
        return yAxis;
    }

    public double getDistanceToSketch() {
        return distanceToSketch;
    }

    public void setDistanceToSketch(double distanceToSketch) {
        this.distanceToSketch = distanceToSketch;
    }

    public boolean isAlreadySelected() {
        return alreadySelected;
    }

    public static Logger getLog() {
        return log;
    }

    public CustomChart getMaximised() {
        return maximised;
    }

    public ObservableList<Shape>[] getFoundPatterns() {
        return foundPatterns;
    }

    public ArrayList<Box> getSubPlots() {
        return subPlots;
    }

    public Rectangle getRect() {
        return rect;
    }

    public ObservableMap<XYChart.Data<Number, Number>, Circle> getCircles() {
        return circles;
    }

    public boolean[] getPatternHighLighted() {
        return patternHighLighted;
    }

    /**
     * set the chart selected. It will change the shadow of the chart as well
     *
     * @param value
     */
    public final void setSelected(boolean value) {
        selected.set(value);

        if (selected.getValue()) {
//            this.setEffect(new DropShadow(15, Color.GREEN));
            alreadySelected = true;
            rect.setStrokeWidth(2.0d);
            rect.setStroke(new Color(1, 0, 0, 1));
        } else {
            applyShadow();
        }
    }

    // Define a getter for the property itself
    public BooleanProperty selectedProperty() {
        return selected;
    }

    /**
     * mainly to apply a grey shadow to the chart. The grey shadow give the
     * chart a visual depth
     */
    private void applyShadow() {
        this.setEffect(null);
        rect.setStroke(Color.rgb(170, 170, 170));
        rect.setStrokeWidth(1.0d);
        //comment due to performance issue
//        DropShadow ds = new DropShadow(5, Color.GREY);
//        ds.setOffsetY(5);
//        this.setEffect(ds);
    }

    /**
     * add a listener to listen to the changes of hotness. if hotness changes it
     * call updateUI
     */
    private void setHotnessChangeListener() {
//        hotness.addListener(new ChangeListener<Double>() {
//
//            @Override
//            public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
//                updateUI();
//            }
//
//        });
    }

    /**
     * set scatter style
     */
    private void setScatterStyle() {
        getStylesheets().add("/styles/scatterEmpty.css");
    }

    /**
     * add a transparent rectangle to the plot
     *
     * @param box
     * @param similarity
     * @param modelSim
     * @param shapeSim
     * @param purity_1
     * @param purity_2
     */
    public void generateRedBox(Box box, ArrayList<Double> similarity, double shapeSim, double modelSim, double purity_1, double purity_2) {
        //don't show result of the current selected
        if (selected.getValue()) {
            return;
        }
        if (!selected.getValue()) {
            rect.setStroke(new Color(0, 0, 1, 1));
        }

        DoubleProperty x1 = null;
        DoubleProperty x2 = null;
        DoubleProperty y1 = null;
        DoubleProperty y2 = null;

        for (Map.Entry<XYChart.Data<Number, Number>, Circle> entry : circles.entrySet()) {
            XYChart.Data<Number, Number> dat = entry.getKey();
            Circle circ = entry.getValue();

            DoubleProperty xt = circ.centerXProperty();
            DoubleProperty yt = circ.centerYProperty();

            double d1 = box.getMinX() - dat.getXValue().doubleValue();
            double d2 = box.getMinY() - dat.getYValue().doubleValue();
            double d3 = box.getMaxX() - dat.getXValue().doubleValue();
            double d4 = box.getMaxY() - dat.getYValue().doubleValue();

            if (Math.abs(d1) < 0.001) {
                x1 = xt;
            }
            if (Math.abs(d2) < 0.001) {
                y2 = yt;
            }
            if (Math.abs(d3) < 0.001) {
                x2 = xt;
            }
            if (Math.abs(d4) < 0.001) {
                y1 = yt;
            }

        }

        if (x1 == null || x2 == null || y1 == null || y2 == null) {
            return;
        }

//        Rectangle rec3 = new Rectangle(x1, y1, Math.abs(x2 - x1), Math.abs(y2 - y1));
        Rectangle rec3 = new Rectangle();
        rec3.xProperty().bind(x1);
        rec3.yProperty().bind(y1);
        rec3.widthProperty().bind(x2.subtract(x1));
        rec3.heightProperty().bind(y2.subtract(y1));

        rec3.setFill(new Color(0, 1, 0, 0.4));

//        int temp = (int) (similarity / 5);
//        if (temp > 10) {
//            temp = 10;
//        }
//        rec3.setFill(new Color(ChartStyleUtility.heatmap.get(temp).getRed(),
//                ChartStyleUtility.heatmap.get(temp).getGreen(),
//                ChartStyleUtility.heatmap.get(temp).getBlue(), 0.3));
        rec3.setFill(new Color(ChartStyleUtility.heatmap.get(0).getRed(),
                ChartStyleUtility.heatmap.get(0).getGreen(),
                ChartStyleUtility.heatmap.get(0).getBlue(), 0.3));
//            rec3.setStroke(ChartStyleUtility.heatmap.get((int) (similarity / 10)));

        rec3.getProperties().put("similarityArray", similarity);

        //TODO
        rec3.getProperties().put("similarity", similarity.get(Recommender.indexOfSimilarityArray));
        rec3.getProperties().put("modelSim", shapeSim);
        rec3.getProperties().put("shapeSim", modelSim);

        rec3.getProperties().put("number", (int) 1);
        rec3.getProperties().put("purity_1", purity_1);
        rec3.getProperties().put("purity_2", purity_2);

        if (SearchUtility.visualRepresentation.equals("all") || !checkBoundsCollision(rec3)) {
            foundPatterns[1].add(rec3);
            Recommender.searchResult.add(rec3);
        }

    }

    private boolean checkBoundsCollision(Rectangle block) {
        boolean collisionDetected = false;
        double result_1 = 0, result_2 = 0;
        for (Shape static_bloc : foundPatterns[1]) {
            if (block.getBoundsInParent().intersects(static_bloc.getBoundsInParent())) {

                switch (SearchUtility.visualRepresentation) {
                    case "union":
                        Shape temp = Shape.union(static_bloc, block);
                        temp.setFill(block.getFill());
                        temp.getProperties().put("number", ((int) static_bloc.getProperties().get("number")) + 1);
                        temp.getProperties().put("similarity", ((double) static_bloc.getProperties().get("similarity")) + ((double) block.getProperties().get("similarity")));
                        temp.getProperties().put("modelSim", ((double) static_bloc.getProperties().get("modelSim")) + ((double) block.getProperties().get("modelSim")));
                        temp.getProperties().put("shapeSim", ((double) static_bloc.getProperties().get("shapeSim")) + ((double) block.getProperties().get("shapeSim")));
                        foundPatterns[1].add(temp);
//                    temp.setStroke(new Color(1, 0, 0, 1));
                        foundPatterns[1].remove(static_bloc);
                        return true;
                    case "subset":
                        if ((double) block.getProperties().get("similarity") > (double) static_bloc.getProperties().get("similarity")) {
                            double left = Math.max(block.getY(), ((Rectangle) static_bloc).getY());
                            double right = Math.min(block.getY() + block.getWidth(), ((Rectangle) static_bloc).getY() + ((Rectangle) static_bloc).getWidth());
                            double bottom = Math.min(block.getY() + block.getHeight(), ((Rectangle) static_bloc).getY() + ((Rectangle) static_bloc).getHeight());
                            double top = Math.max(block.getY(), ((Rectangle) static_bloc).getY());
                            result_1 = (double) (((right - left) * (bottom - top))
                                    / ((block.getWidth()) * (block.getHeight())));
                            result_2 = (double) (((right - left) * (bottom - top))
                                    / ((((Rectangle) static_bloc).getWidth()) * (((Rectangle) static_bloc).getHeight())));
                            if ((result_1 > 0.2 || result_2 > 0.2)) {
                                return true;
                            }
                            break;
                        } else {
                            foundPatterns[1].remove(static_bloc);
                            return false;
                        }

                }
            }
        }
        return false;

    }

    public void resetHotRec() {
        if (!selected.getValue()) {
            rect.setStroke(Color.rgb(170, 170, 170));
            rect.setStrokeWidth(1.0d);
        }

        for (int i = 0; i < 3; i++) {
            foundPatterns[i].stream().forEach((rec) -> {
                this.getChildren().remove(rec);
            });
            foundPatterns[i].clear();
        }
    }

    /**
     * add a rectangle with opacity to the plot when the search is finished to
     * give a loading feeling to user
     */
    public void drawLoadingEffect() {
        Rectangle loadingBox = new Rectangle(0, 1, this.getWidth(), this.getHeight());
        loadingBox.setFill(Color.rgb(0, 0, 1, 0.05));
        this.getChildren().add(loadingBox);
    }

    /**
     * This function draw all rectangles on the plot
     *
     */
    public void drawAllRec() {

        foundPatterns[1].stream()
                .forEach((rec) -> {
                    int temp = 0;
                    switch (SearchUtility.colorCoding) {
                        case "combination":
                            temp = (int) ((double) rec.getProperties().get("similarity")
                                    / (int) rec.getProperties().get("number")) / 5;
                            if (temp > 10) {
                                temp = 10;
                            }
                            rec.setFill(new Color(ChartStyleUtility.heatmap.get(temp).getRed(),
                                    ChartStyleUtility.heatmap.get(temp).getGreen(),
                                    ChartStyleUtility.heatmap.get(temp).getBlue(), 0.3));
                            break;
                        case "model":
                            temp = (int) ((double) rec.getProperties().get("modelSim")
                                    / (int) rec.getProperties().get("number")) / 2;
                            if (temp > 10) {
                                temp = 10;
                            }
                            rec.setFill(new Color(ChartStyleUtility.heatmapModel.get(temp).getRed(),
                                    ChartStyleUtility.heatmapModel.get(temp).getGreen(),
                                    ChartStyleUtility.heatmapModel.get(temp).getBlue(), 0.3));
                            break;
                        case "shape":
                            temp = (int) ((double) rec.getProperties().get("shapeSim")
                                    / (int) rec.getProperties().get("number")) / 10;
                            if (temp > 10) {
                                temp = 10;
                            }
                            rec.setFill(new Color(ChartStyleUtility.heatmapShape.get(temp).getRed(),
                                    ChartStyleUtility.heatmapShape.get(temp).getGreen(),
                                    ChartStyleUtility.heatmapShape.get(temp).getBlue(), 0.3));
                            break;
                    }

                    this.getChildren().add(rec);
                    rec.setOnTouchPressed((TouchEvent event) -> {
                        rec.setFill(new Color(0, 1, 0, 1));
                        Recommender.recommendedRec.add(rec);
                    });
                });

        circles.values().stream().forEach((c) -> {
            c.toFront();
        });
    }

    /**
     * combine all the points from different series in order to use in FVs later
     */
    public void combinePoints() {
        ObservableList<XYChart.Series<Number, Number>> list = this.getData();
        list.stream().forEach((o) -> {
            for (int i = 0; i < o.getData().size(); i++) {
                XYChart.Data temp = new XYChart.Data<>(o.getData().get(i).getXValue().doubleValue(),
                        o.getData().get(i).getYValue().doubleValue());
                temp.setExtraValue(o.getData().get(i).getExtraValue());
                points.add(temp);
            }
        });
    }

    /**
     * Given a boundary, will produce a box containing the points on that
     * boundary
     *
     * @param x : upper left corner X
     * @param y : upper left corner Y
     * @param width : with of rec
     * @param height : height of rec
     * @return
     */
    public Box generateBoxFromBounding(double x, double y, double width, double height) {
        ObservableList<XYChart.Data<Number, Number>> selected = FXCollections.observableArrayList();
        points.stream().forEach((p) -> {
            if (p.getXValue().doubleValue() >= x && p.getXValue().doubleValue() <= x + width
                    && p.getYValue().doubleValue() >= y && p.getYValue().doubleValue() <= y + height) {
                selected.add(p);
            }
        });
        return new Box(SearchUtility.numbOfRow, SearchUtility.numOfCol, selected);
    }

    /**
     * search the whole scatter plot for local pattern similar to the given box
     *
     * @param box
     */
    public void searchScatterPlot(Box box) {
        //scale factor
        double sf = SearchUtility.boxScaleStep;
        //transfer factor
        double tf = SearchUtility.boxTransferStep;
        subPlots.stream().forEach(sp -> {
            if (sp.checkPointRatio(box)) {
                Set<Integer> temp = new HashSet<>(box.getIndexSet());
                temp.retainAll(sp.getIndexSet());
                //check the purity measures
                double purity_1 = ((double) temp.size() / (double) box.getIndexSet().size());
                double purity_2 = ((double) temp.size() / (double) sp.getIndexSet().size());

                if (purity_1 > SearchUtility.purity_1 && purity_2 > SearchUtility.purity_2) {
                    ArrayList<Double> modelArray = box.compareToModelAll(sp);
                    ArrayList<Integer> shapeArray = box.compareToShapeAll(sp, Recommender.gridCoarse);
                    double tempSim = modelArray.get(Recommender.regressionDegree) * (1 - SearchUtility.shapeWeight) * Recommender.alpha[Recommender.alphaIndex]
                            + shapeArray.get(Recommender.gridSizeIndex) * SearchUtility.shapeWeight;
                    if (tempSim < SearchUtility.minSimilarity) {
                        this.generateRedBox(sp, similarityListCreation(shapeArray, modelArray), shapeArray.get(Recommender.gridSizeIndex),
                                modelArray.get(Recommender.regressionDegree), purity_1, purity_2);
                    }
                }
            }
        });

//        this.drawAllRec();
//        this.drawLoadingEffect();
    }

    /**
     *
     * @return an ArrayList of similarity, based on various Coefficients
     */
    public ArrayList<Double> similarityListCreation(ArrayList<Integer> shapeArray, ArrayList<Double> modelArray) {

        ArrayList<Double> result = new ArrayList<>();

        for (Integer s : shapeArray) {
            for (Double m : modelArray) {
                for (int i = 0; i < Recommender.coeff.length; i++) {
                    for (int j = 0; j < Recommender.alpha.length; j++) {
                        result.add((Recommender.coeff[i] * s) + ((1 - Recommender.coeff[i]) * Recommender.alpha[j] * m));
                    }
                }
            }
        }
        return result;
    }

    /**
     * This function calculate all subplots and their correspondence FVs
     */
    public void calculateSubPlots() {

        subPlots.clear();
        //scale factor
        double sf = SearchUtility.boxScaleStep;
        //transfer factor
        double tf = SearchUtility.boxTransferStep;

        //set boarder to red so user know where is search happening now
        //first for scaling over x axis (width)
        for (double w = SearchUtility.boxScaleEnd; w > SearchUtility.boxScaleStart; w = w - sf) {
            for (double h = SearchUtility.boxScaleEnd; h > SearchUtility.boxScaleStart; h = h - sf) {

                //transforming over x axis
                for (double x = 0; x <= 1 - w; x = x + tf) {
                    //tranforming over y axis
                    for (double y = 0; y <= 1 - h; y = y + tf) {

                        Box temp = generateBoxFromBounding(x, y, w, h);
                        //check if there is at least 15 points in the selected box
                        if (temp.getPoints().size() > SearchUtility.minNumPoints) {
                            subPlots.add(temp);
                        }
                    }
                }
            }
        }
//        System.out.println(subPlots.size());
    }

//    /**
//     * reset all highlighted series
//     */
//    public void resetHighLightedSeries() {
//        rect.setStroke(Color.rgb(170, 170, 170));
//        int size = this.getData().size();
//        for (int i = 0; i < size; i++) {
//            patternHighLighted[i] = false;
//        }
//
//        for (int i = 0; i < size; i++) {
//            for (int j = 0; j < this.getData().get(i).getData().size(); j++) {
//                circles.get(this.getData().get(i).getData().get(j)).setRadius(0.5);
//                if (i > 7) {
//                    circles.get(this.getData().get(i).getData().get(j)).setFill(ChartStyleUtility.colors.get(0));
//                } else {
//                    circles.get(this.getData().get(i).getData().get(j)).setFill(ChartStyleUtility.colors.get(i));
//                }
//            }
//        }
//    }
    /**
     * find a specific circle among all circles by the index of the circles
     *
     * @param index
     * @return
     */
    public Circle findCircleByIndex(Integer index) {
        for (Map.Entry<XYChart.Data<Number, Number>, Circle> entry : circles.entrySet()) {
            Circle circ = entry.getValue();
            if ((int) circ.getProperties().get("index") == index) {
                return circ;
            }
        }
        return null;
    }

    public void makeEverythingGrey() {
        this.distanceToSketch = -1;
        tooltip.setText("N/A");
        rect.setStroke(Color.rgb(170, 170, 170));
        rect.setFill(Color.rgb(255, 255, 255));
        int size = this.getData().size();
        for (int i = 0; i < size; i++) {
            patternHighLighted[i] = false;
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < this.getData().get(i).getData().size(); j++) {
                circles.get(this.getData().get(i).getData().get(j)).setRadius(0.5);
                circles.get(this.getData().get(i).getData().get(j)).setFill(Color.rgb(110, 110, 110, 0.5));
            }
        }
    }

    /**
     * highLight series based on index
     *
     * @param index
     * @param distance
     * @param maxDis
     * @param minDis
     */
    public void highLightSeries(int index, double distance, double maxDis, double minDis) {
        this.distanceToSketch = distance;
        double saturation = 1 - ((((distance - minDis) / (maxDis - minDis))) / 4 + 0.70);
        tooltip.setText(Double.toString(distance));
        rect.setFill(Color.hsb(240, 0.5, 1, saturation));
        rect.setStroke(Color.hsb(240, saturation + 0.5, 1));

        patternHighLighted[index] = true;
        for (int j = 0; j < this.getData().get(index).getData().size(); j++) {
            if (index > 7) {
                circles.get(this.getData().get(index).getData().get(j)).setFill(ChartStyleUtility.colors.get(0));
            } else {
                circles.get(this.getData().get(index).getData().get(j)).setFill(ChartStyleUtility.colors.get(index));
            }
            circles.get(this.getData().get(index).getData().get(j)).setRadius(1.2);
        }

        int size = this.getData().size();
        for (int i = 0; i < size; i++) {
            if (!patternHighLighted[i]) {
                for (int j = 0; j < this.getData().get(i).getData().size(); j++) {
                    circles.get(this.getData().get(i).getData().get(j)).setFill(Color.rgb(110, 110, 110, 0.4));
                }
            }
        }
    }

    public boolean retriveChart(String xDim, String yDim) {
        return xDim.equals(xAxis) && yDim.equals(yAxis);
    }

    @Override
    public void redrawSelected() {
        drawAllBackground();
        SearchUtility.parentController.getSpmModel().getModel().getSelectedItems().forEach(item -> {
            outerloop:
            for (Map.Entry<XYChart.Data<Number, Number>, Circle> entry : circles.entrySet()) {
                if (entry.getValue().getProperties().get("index").equals(item)) {
                    String clsofCirc = (String) SearchUtility.parentController.getSpmModel().getModel().
                            getComplexObjList().get((int) entry.getValue().getProperties().get("index")).getAttribute("class");

                    entry.getValue().setFill(Color.web(SearchUtility.parentController.getSpmModel().getModel().colorofClass(clsofCirc)));
                    entry.getValue().getProperties().put("colorIndex",
                            SearchUtility.parentController.getSpmModel().getModel().indexofClass(clsofCirc));
//                    entry.getValue().setFill(ChartStyleUtility.colors.get((int) entry.getValue().getProperties().get("colorIndex")));
                    break outerloop;
                }
            }
        });

        //for maximised version
        if (maximised != null) {
            SearchUtility.parentController.getSpmModel().getModel().getSelectedItems().forEach(item -> {
                outerloop:
                for (Map.Entry<XYChart.Data<Number, Number>, Circle> entry : maximised.circles.entrySet()) {
                    if (entry.getValue().getProperties().get("index").equals(item)) {
                        entry.getValue().setFill(ChartStyleUtility.colors.get((int) entry.getValue().getProperties().get("colorIndex")));
                        break outerloop;
                    }
                }
            });
        }

    }

    @Override
    public void drawAllBackground() {
        circles.entrySet().forEach(entry -> {
            entry.getValue().setFill(Color.rgb(230, 230, 230));
        });

        if (maximised != null) {
            maximised.circles.entrySet().forEach(entry -> {
                entry.getValue().setFill(Color.rgb(230, 230, 230));
            });
        }
    }

    @Override
    public void redrawCategories() {
        for (Map.Entry<XYChart.Data<Number, Number>, Circle> entry : circles.entrySet()) {
            Circle circ = entry.getValue();
//            circ.getProperties().remove("colorIndex");

            String clsofCirc = (String) SearchUtility.parentController.getSpmModel().getModel().
                    getComplexObjList().get((int) circ.getProperties().get("index")).getAttribute("class");

            //////
            if (!SearchUtility.parentController.getSpmModel().getModel().isClassVisible(clsofCirc)) {
                circ.setVisible(false);
                continue;
            } else {
                circ.setVisible(true);
            }
            //////

            circ.setFill(Color.web(SearchUtility.parentController.getSpmModel().getModel().colorofClass(clsofCirc)));
            circ.getProperties().put("colorIndex",
                    SearchUtility.parentController.getSpmModel().getModel().indexofClass(clsofCirc));
        }
    }

    @Override
    public void highLightRecordTemp(boolean highlight, Item item) {
        Circle c = findCircleByIndex(item.getIndex());
        if (highlight) {
            c.setRadius(4);
        } else {
            c.setRadius(0.5);
        }
    }
}
