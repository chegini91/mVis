package at.tugraz.cgv.multiviewva.gui.charts;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import at.tugraz.cgv.multiviewva.model.Pattern;
import at.tugraz.cgv.multiviewva.model.Point2D;
import at.tugraz.cgv.multiviewva.model.SPMModel;
import at.tugraz.cgv.multiviewva.model.search.Box;
import at.tugraz.cgv.multiviewva.utility.SearchUtility;
import at.tugraz.cgv.multiviewva.model.DataModel;
import at.tugraz.cgv.multiviewva.model.Dimension;
import at.tugraz.cgv.multiviewva.model.Item;
import at.tugraz.cgv.multiviewva.model.LabelModel;
import at.tugraz.cgv.multiviewva.utility.GraphicUtility;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;

/**
 * @author mchegini
 */
public class SPLOMsChart extends AnchorPane implements Brushable, DimBrushable {

    ObservableList<Label> labels = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    public SPLOMsChart() {
        super();

        initGrid();
        initAnchorPane();
        this.getChildren().add(grid);
        dimensions = new ArrayList<>();
        grid.setStyle("-fx-background-color: #ffffff");
        this.setStyle("-fx-background-color: #ffffff");
    }

    private void initGrid() {
        grid = new GridPane();
        grid.setLayoutX(0.0);
        grid.setLayoutY(0.0);
        AnchorPane.setBottomAnchor(grid, padd);
        AnchorPane.setLeftAnchor(grid, padd);
        AnchorPane.setRightAnchor(grid, padd);
        AnchorPane.setTopAnchor(grid, padd);
    }

    private void initAnchorPane() {
        this.minHeight(0);
        this.minWidth(0);
        this.prefHeight(Double.MAX_VALUE);
        this.prefWidth(Double.MAX_VALUE);
    }

    /**
     * check if there is any background job in order to disable clicking
     */
    public static boolean backGroundJob = false;

    /**
     * true if user already clicked on pre-processed
     */
    private boolean preProcessed = false;

    /**
     * dataModel of SPLOMsChart
     */
    private SPMModel dataModel;

    /**
     * private padding
     */
    double padd = 25.0d;

    /**
     * this attribute maps physical dimension (in GridPane) to dataModel. e.g.
     * it says where is grid(7,7) in dataSet (index) first element is dimension
     * in real dataSet and second element is physical position in the grid
     * (column or x)
     */
    private HashMap<Integer, Integer> dimensionMap = new HashMap<Integer, Integer>();

    /**
     * dimension of scatter plot matrix
     */
    private ArrayList<String> dimensions;

    GridPane grid;

    /**
     * selected scatter plot
     */
    private CustomChart selectedSPRight = null;

    /**
     * selected scatter plot
     */
    private CustomChart selectedSPMain = null;

    /**
     * List of scatter plots in SPLOMsChart
     */
    private ObservableList<ObservableList<CustomChart>> scList = FXCollections.<ObservableList<CustomChart>>observableArrayList();

    /**
     * vertical axises of SPLOMsChart
     */
    private ObservableList<NumberAxis> verticalAxises = FXCollections.observableArrayList();

    /**
     * horizontal axises of SPLOMsChart
     */
    private ObservableList<NumberAxis> horizontalAxises = FXCollections.observableArrayList();

    public SPMModel getDataModel() {
        return dataModel;
    }

    public void setDataModel(SPMModel dataModel) {
        this.dataModel = dataModel;
        initSPLOMs();
    }

    public void setDimensionMap(HashMap<Integer, Integer> dimensionMap) {
        this.dimensionMap = dimensionMap;
    }

    public HashMap<Integer, Integer> getDimensionMap() {
        return dimensionMap;
    }

    public void setPreProcessed(boolean preProcessed) {
        this.preProcessed = preProcessed;
    }

    public boolean isPreProcessed() {
        return preProcessed;
    }

    public CustomChart getSelectedSPRight() {
        return selectedSPRight;
    }

    public CustomChart getSelectedSPMain() {
        return selectedSPMain;
    }

    public void setSelectedSPRight(CustomChart selectedSPRight) {
        this.selectedSPRight = selectedSPRight;
    }

    public void setSelectedSPMain(CustomChart selectedSPMain) {
        this.selectedSPMain = selectedSPMain;
    }

    public ArrayList<String> getDimensions() {
        return dimensions;
    }

    public boolean isBackGroundJob() {
        return backGroundJob;
    }

    public GridPane getGrid() {
        return grid;
    }

    public ObservableList<ObservableList<CustomChart>> getScList() {
        return scList;
    }

    public ObservableList<NumberAxis> getVerticalAxises() {
        return verticalAxises;
    }

    public ObservableList<NumberAxis> getHorizontalAxises() {
        return horizontalAxises;
    }

    public void setBackGroundJob(boolean backGroundJob) {
        this.backGroundJob = backGroundJob;
    }

    public void setDimensions(ArrayList<String> dimensions) {
        this.dimensions = dimensions;
    }

    public void setGrid(GridPane grid) {
        this.grid = grid;
    }

    public void setScList(ObservableList<ObservableList<CustomChart>> scList) {
        this.scList = scList;
    }

    public void setVerticalAxises(ObservableList<NumberAxis> verticalAxises) {
        this.verticalAxises = verticalAxises;
    }

    public void setHorizontalAxises(ObservableList<NumberAxis> horizontalAxises) {
        this.horizontalAxises = horizontalAxises;
    }

    public Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();

        for (Node node : childrens) {
            if (gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return result;
    }

    /**
     * This function is used for generating a label in the grid cells that x==y
     *
     * @param dimention
     * @param i         position of the label x
     * @param j         position of the label y
     */
    public void generateDimentionLabel(String dimention, int i, int j) {
        Label lb = new Label(dimention);
        labels.add(lb);
        lb.setTextFill(Color.web("#0098C6"));
        this.dimensions.add(dimention);
        GridPane.setHalignment(lb, HPos.CENTER);
        grid.add(lb, i + 1, j + 1);

        Tooltip t = new Tooltip(
                lb.getText()
        );
        lb.getProperties().put("Tooltip", t);
        GraphicUtility.hackTooltipStartTiming(t);
        Tooltip.install(lb, t);

    }

    /**
     * This function get data series and add a plot to a specific location in
     * the scatter plot matrix
     *
     * @param x                 : x location in the matrix
     * @param y                 : y location in the matrix
     * @param localPatternArray : ArrayList of points2d[], means data series
     * @param localPatternIDs   : name of data series
     * @param reverse
     */
    public void addPlotToMatrix(int x, int y, ArrayList<Point2D[]> localPatternArray, ArrayList<String> localPatternIDs, boolean reverse) {

        //finding max min
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;

        if (reverse) {
            int tempSize = localPatternArray.size();
            for (int i = 0; i < tempSize; i++) {
                for (int j = 0; j < localPatternArray.get(i).length; j++) {
                    double xTemp = localPatternArray.get(i)[j].getX();
                    double yTemp = localPatternArray.get(i)[j].getY();
                    localPatternArray.get(i)[j].setX(yTemp);
                    localPatternArray.get(i)[j].setY(xTemp);
                }
            }
        }

        for (Point2D[] patternArray : localPatternArray) {
            for (Point2D point : patternArray) {
                if (point.getX() >= maxX) {
                    maxX = point.getX();
                }
                if (point.getX() <= minX) {
                    minX = point.getX();
                }
                if (point.getY() >= maxY) {
                    maxY = point.getY();
                }
                if (point.getY() <= minY) {
                    minY = point.getY();
                }
            }
        }

        //end of finding max min
        //instantiate scatter plot
        final CustomChart sc = new CustomChart(x, y, dataModel.getHeader().get(x), dataModel.getHeader().get(y));

        sc.setMinHeight(0.0);
        sc.setPrefHeight(1000);
        sc.setMinWidth(0.0);
        sc.setPrefWidth(1000);

        //add new scatter plot to SPLOMs
        scList.get(x).add(sc);

        //add points to scatter plot
        int size = localPatternIDs.size();
        for (int i = 0; i < size; i++) {
            XYChart.Series seriesTemp = new XYChart.Series();
            //seriesTemp.setName(localPatternIDs.get(i));
            int seriesSize = localPatternArray.get(i).length;
            seriesTemp.setName(localPatternIDs.get(i));

            for (int j = 0; j < seriesSize; j++) {
                XYChart.Data data = new XYChart.Data(
                        localPatternArray.get(i)[j].getX(), localPatternArray.get(i)[j].getY());

//                DummyNode node = new DummyNode();
//                data.setNode(node);
//                node.getProperties().put("index", localPatternArray.get(i)[j].getIndex());
                data.setExtraValue(localPatternArray.get(i)[j].getIndex());
                seriesTemp.getData().add(data);
            }
            sc.getData().addAll(seriesTemp);

        }
        sc.drawPoints();

        //combine all series
        sc.combinePoints();

        grid.add(sc, x + 1, y + 1);
        dimensionMap.put(x, x + 1);

        if (x == 0) {
            NumberAxis te = new NumberAxis(0, 1, 0.2);
            te.autosize();
            te.setSide(Side.LEFT);
            verticalAxises.add(te);
            te.setTickLabelsVisible(false);
            te.prefHeightProperty().bind(sc.rect.heightProperty());
            te.maxHeightProperty().bind(sc.rect.heightProperty());
            te.setTranslateY(-padd / 4);
            grid.add(te, x, y + 1);
        }

        if (y == 0) {
            NumberAxis te = new NumberAxis(0, 1, 0.2);
            te.autosize();
            te.setSide(Side.TOP);
            horizontalAxises.add(te);
            te.setTickLabelsVisible(false);
            te.prefWidthProperty().bind(sc.rect.widthProperty());
            te.maxWidthProperty().bind(sc.rect.widthProperty());
            grid.add(te, x + 1, y);
        }

    }

    /**
     * clean the whole UI
     */
    public void cleanUI() {
        selectedSPRight = null;
        grid.getChildren().clear();
        dimensions = new ArrayList<>();
        scList.clear();
    }

    public void compareAllChartsModel(Box box) {

        SearchUtility.lastSearchQuery = box;
        scList.parallelStream().forEach((scL) -> {
            scL.parallelStream().forEach(sc -> {
                sc.searchScatterPlot(box);
            });
        });

        scList.stream().forEach((scL) -> {
            scL.stream().forEach(sc -> {
                sc.drawAllRec();
            });
        });
    }

    /**
     * clear all the heat-maps in the SPLOM
     */
    public void clearAllHeatMaps() {
        scList.stream().forEach((scL) -> {
            scL.stream().forEach(sc -> {
                sc.resetHotRec();
            });
        });
    }

    /**
     * ask all the scatter plots to calculate their feature vectors and
     * regression model
     */
    public void preProcessForSearch() {

        setPreProcessed(true);
        final double wndwWidth = 300.0d;
        Label updateLabel = new Label("pre processing...");
        updateLabel.setPrefWidth(wndwWidth);
        ProgressBar progress = new ProgressBar();
        progress.setPrefWidth(wndwWidth);

        VBox updatePane = new VBox();
        updatePane.setPadding(new Insets(10));
        updatePane.setSpacing(5.0d);
        updatePane.getChildren().addAll(updateLabel, progress);

        Stage taskUpdateStage = new Stage(StageStyle.UTILITY);
        taskUpdateStage.setScene(new Scene(updatePane));
        taskUpdateStage.show();
        taskUpdateStage.setAlwaysOnTop(true);

        Task preprocess = new Task<Void>() {
            /*
             scList.stream().forEach(sc -> {
             sc.calculateSubPlots();
             });
             */

            @Override
            protected Void call() throws Exception {
                int max = scList.size();

//                scList.parallelStream().forEach(scTemp -> {
//                    scTemp.parallelStream().forEach(sc -> {
//                        sc.calculateSubPlots();
//                    });
//                });
                for (int i = 0; i < max; i++) {
                    for (int j = 0; j < max - 1; j++) {
                        if (scList.get(i) != null && scList.get(i).get(j) != null) {
                            if (scList.get(i).get(j) != null) {
                                scList.get(i).get(j).calculateSubPlots();
                            }
                            if (isCancelled()) {
                                return null;
                            }
                            updateProgress(i * (max - 1) + j, max * (max - 1));
                            updateMessage("Plot " + String.valueOf(i * (max) + j) + "/" + max * (max - 1) + " processed");
                        }

                    }

                }
                return null;
            }
        };

        preprocess.setOnSucceeded(
                new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t
                    ) {
                        taskUpdateStage.hide();
                    }
                }
        );

        taskUpdateStage.setOnCloseRequest(
                new EventHandler<WindowEvent>() {

                    @Override
                    public void handle(WindowEvent event
                    ) {
                        preprocess.cancel();
                    }
                }
        );
        progress.progressProperty()
                .bind(preprocess.progressProperty());
        updateLabel.textProperty()
                .bind(preprocess.messageProperty());

        taskUpdateStage.show();

        new Thread(preprocess)
                .start();

    }

    /**
     * initialize SPLOMsChart
     */
    public void initSPLOMs() {

        int nrAttributes = dataModel.getModel().getNumberAttributes();

        //init sclist
        scList = FXCollections.<ObservableList<CustomChart>>observableArrayList();
        for (int i = 0; i < nrAttributes; i++) {
            final ObservableList<CustomChart> row = FXCollections.<CustomChart>observableArrayList();
            scList.add(i, row);
        }

        //create a grid
        for (int y = nrAttributes - 1; y > -1; y = y - 1) {
            for (int x = nrAttributes - 1; x > -1; x = x - 1) {
                if (x == y) {
                    this.generateDimentionLabel("" + dataModel.getHeader().get(x), x, y);
                } else if (x < y) {
                    ArrayList<Pattern> localPatternList = getAllClusterPatternsByIndex(dataModel.getHeader().get(x), dataModel.getHeader().get(y), dataModel.getPatternList());
                    ArrayList<Point2D[]> localPatternArray = getAllClusterPatternsPoints(localPatternList, dataModel.getModel());
                    ArrayList<String> localPatternIDs = getAllClusterPatternsIDs(localPatternList, dataModel.getModel());

                    this.addPlotToMatrix(x, y, localPatternArray, localPatternIDs, false);
                } else {
                    ArrayList<Pattern> localPatternList = getAllClusterPatternsByIndex(dataModel.getHeader().get(y), dataModel.getHeader().get(x), dataModel.getPatternList());
                    ArrayList<Point2D[]> localPatternArray = getAllClusterPatternsPoints(localPatternList, dataModel.getModel());
                    ArrayList<String> localPatternIDs = getAllClusterPatternsIDs(localPatternList, dataModel.getModel());

                    this.addPlotToMatrix(x, y, localPatternArray, localPatternIDs, true);
                }
            }
        }

        if (dataModel.getFilename().equals("eurostat27")) {
            ArrayList<String> labels = new ArrayList<>();
            for (int i = 1; i < 15; i++) {
                labels.add("P" + i);
            }
            //TODO double check
//            spmModel.setClassNames(labels);
        }
    }

    private ArrayList<Pattern> getAllClusterPatternsByIndex(String x, String y, ArrayList<Pattern> patternList) {
        ArrayList<Pattern> result = new ArrayList<>();
        patternList.stream().forEach((p) -> {
            String dimX = p.getxDimension();
            String dimY = p.getyDimension();
            if (dimX.equals(x) && dimY.equals(y)) {
                result.add(p);
            }
        });
        return result;
    }

    //pattern is scatterplot here, why?
    public static ArrayList<Point2D[]> getAllClusterPatternsPoints(ArrayList<Pattern> list, DataModel model) {
        ArrayList<Point2D[]> localPatternsArray = new ArrayList<>();
        ArrayList<LabelModel> labels = model.getClassNames();
        ArrayList<String> labelNames = new ArrayList<>();

        for (LabelModel label : labels) {
            labelNames.add(label.getName());
        }

        if (model.getNrOfCatDims() > 0) {
            ArrayList<Object>[] data = model.getDataSet();
            int classIndex = model.getClassIndex().get(0);      // 0 -> only 1 class label

            ArrayList<Point2D> points = list.get(0).getPoints();
            for (LabelModel className : labels) {

                ArrayList<Point2D> pointsOfclass = new ArrayList<Point2D>();
                for (int i = 0; i < points.size(); i++) {
                    if (data[classIndex].get(i).equals(className.getName())) {
                        pointsOfclass.add(points.get(i));
                    }
                }
                Point2D[] arrayPoints = pointsOfclass.toArray(new Point2D[pointsOfclass.size()]);
                localPatternsArray.add(arrayPoints);
            }
        } else {
            for (Pattern p : list) {
                ArrayList<Point2D> listPoints = p.getPoints();
                Point2D[] arrayPoints = listPoints.toArray(new Point2D[listPoints.size()]);
                localPatternsArray.add(arrayPoints);
            }
        }

        return localPatternsArray;
    }

    /**
     * get a list and return name of the all clusters
     *
     * @param list
     * @param model
     * @return
     */
    private ArrayList<String> getAllClusterPatternsIDs(ArrayList<Pattern> list, DataModel model) {
        ArrayList<String> result = new ArrayList<String>();
        if (model.getNrOfCatDims() > 0) {
            ArrayList<String> classNames = model.getCopyClassNames();

            for (String name : classNames) {
                result.add(name);
            }

        } else {
            for (Pattern p : list) {
                result.add(p.getId());
            }
        }

        return result;
    }

    @Override
    public void redrawSelected() {
        getScList().forEach(scL -> {
            scL.forEach(sc -> {
                sc.redrawSelected();
            });
        });
    }

    @Override
    public void drawAllBackground() {
        getScList().forEach(scL -> {
            scL.forEach(sc -> {
                sc.drawAllBackground();
            });
        });
    }

    @Override
    public void redrawCategories() {
        getScList().forEach(scL -> {
            scL.forEach(sc -> {
                sc.redrawCategories();
            });
        });
    }

    @Override
    public void brushAllDim() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void brushDimension(Dimension dimensions, boolean highligh) {
        for (Label label : labels) {
            if (label.getText().equals(dimensions.getName())) {
                if (highligh) {
                    label.setTextFill(Color.web("#0098C6"));
                } else {
                    label.setTextFill(Color.web("#000000"));
                }
            }
        }
    }

    @Override
    public void unbrushAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void highLightRecordTemp(boolean highlight, Item item) {
        getScList().forEach(scL -> {
            scL.forEach(sc -> {
                sc.highLightRecordTemp(highlight, item);
            });
        });
    }

}
