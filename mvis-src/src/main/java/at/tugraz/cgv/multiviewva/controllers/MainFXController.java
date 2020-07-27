/*
 * This is the main controller class for JFX. this controller handle a lot of
 * stuff for Main.fxml class. Some of the modules in Main.fxml are included
 * for example the scatterplot have another controller for itself despite the
 * fact that it is included in Main.fxml file. This controller somehow
 * works as a manager for all the controllers in the scnee
 */
package at.tugraz.cgv.multiviewva.controllers;

import at.tugraz.cgv.multiviewva.model.*;
import at.tugraz.cgv.multiviewva.utility.evaluation.AxisOrderType;
import at.tugraz.cgv.multiviewva.utility.evaluation.AxisSortType;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import at.tugraz.cgv.multiviewva.model.indexing.IndexSearch;
import at.tugraz.cgv.multiviewva.model.search.Box;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import at.tugraz.cgv.multiviewva.utility.DataLoadUtility;
import at.tugraz.cgv.multiviewva.utility.GraphicUtility;
import at.tugraz.cgv.multiviewva.utility.SearchUtility;
import com.anchorage.docks.node.DockNode;
import com.anchorage.docks.stations.DockStation;
import com.anchorage.system.AnchorageSystem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import at.tugraz.cgv.multiviewva.gui.charts.ParallelCoordinatesChart;
import at.tugraz.cgv.multiviewva.model.enums.clusteringType;
import at.tugraz.cgv.multiviewva.model.enums.dimReductionType;
import at.tugraz.cgv.multiviewva.utility.BrushMngUtility;
import at.tugraz.cgv.multiviewva.utility.ChartStyleUtility;
import at.tugraz.cgv.multiviewva.utility.MLUtility;
import at.tugraz.cgv.multiviewva.utility.gazeLensUtility;
import com.theeyetribe.clientsdk.GazeManager;

import java.util.prefs.Preferences;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.shape.Circle;

/**
 * @author mchegini
 */
public class MainFXController implements Initializable {

    static private Logger log = LoggerFactory.getLogger(MainFXController.class);

    @FXML
    Spinner<Integer> stepSpinner;

    @FXML
    private Label label;

    @FXML
    private BorderPane spMain;

    @FXML
    private ParallelCoordinatesChart parcoordChart;

    @FXML
    private BorderPane spRight;

    //get access to the scatter plot controller
    @FXML
    private ScatterPlotController spMainController;

    @FXML
    private SimilarityMapController similarityMapController;

    //get access to the scatter plot controller
    @FXML
    private ScatterPlotController spRightController;

    //scatter plot matrix anchorpane
    @FXML
    private AnchorPane spm;

    @FXML
    private Pane informationPane;

    @FXML
    private HBox gazeCtrlHBox;

    @FXML
    private Pane scatterPaneRight;

    @FXML
    private AnchorPane contextMenu;


    @FXML
    private AnchorPane labelMenu;

    @FXML
    private LabelPaneController labelMenuController;

    @FXML
    private AnchorPane consoleArea;

    @FXML
    private ConsolePaneController consoleAreaController;

    @FXML
    private Pane scatterPaneLeft;

    @FXML
    private AnchorPane similarityMap;

    //scatter plot matrix controller. The pattern to access the controller
    //is to write spm(name of the node) + Controller
    @FXML
    private MatrixScatterPlotController spmController;

    //    @FXML
//    private Slider pattNumSlider;
    @FXML
    private void loadDataAction(ActionEvent event) {
        loadDataStageCreation();
    }

    @FXML
    private Button button;

    @FXML
    private Menu drType;

    @FXML
    private Menu clusterType;

    @FXML
    private Button submit;

    @FXML
    private void getSketchAction(ActionEvent e) {
        spmController.getSploms().clearAllHeatMaps();
        spMainController.searchForLocalPatterns();
//        searchForSketch("left");
    }

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private ToggleGroup searchMethod;

    @FXML
    private AnchorPane mainPane;

    private DockStation station;

    private Circle gazePos;

    public Stage loadStage;

    @FXML
    private Button reorder;

    @FXML
    private ToggleGroup typeGroup;

    @FXML
    private ToggleGroup orderingGroup;

    //if false, the effects of everything will be removed
    //progress bar
    ProgressBar pb = new ProgressBar();

    /**
     * spmModel of the whole project, contains DataModel
     */
    private SPMModel spmModel;

    private final GazeController gazeListener = new GazeController();

    private HashBiMap<String, Point2D[]> objectsByName;

    //left and right menu controller
    private ContextMenuController leftMenuController;
    private ContextMenuController rightMenuController;

    public void setSpmModel(SPMModel spmModel) {
        this.spmModel = spmModel;
    }

    public SPMModel getSpmModel() {
        return spmModel;
    }

    public ScatterPlotController getSpMainController() {
        return spMainController;
    }

    public GazeController getGazeListener() {
        return gazeListener;
    }

    public ScatterPlotController getSpRightController() {
        return spRightController;
    }

    public Pane getScatterPaneRight() {
        return scatterPaneRight;
    }

    //    private SPLOMsChart selectedSPRight;
//    private SPLOMsChart selectedSPLeft;
//
//    public SPLOMsChart getSelectedSPRight() {
//        return selectedSPRight;
//    }
//
//    public SPLOMsChart getSelectedSPLeft() {
//        return selectedSPLeft;
//    }
//
//    public void setSelectedSPRight(SPLOMsChart selectedSPRight) {
//        this.selectedSPRight = selectedSPRight;
//    }
//
//    public void setSelecte dSPLeft(SPLOMsChart selectedSPLeft) {
//        this.selectedSPLeft = selectedSPLeft;
//    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //init Eye Tribe
        final GazeManager gm = GazeManager.getInstance();
        boolean success = gm.activate(GazeManager.ApiVersion.VERSION_1_0);
        gazePos = new Circle(gazeLensUtility.lensSize);
        gazePos.setFill(Color.TRANSPARENT);
        gazePos.setStrokeWidth(3);
        gazePos.setStyle(" -fx-border-color: silver;");
        gazePos.setStroke(Color.RED);
        gazePos.setMouseTransparent(true);
        gazePos.setVisible(gazeLensUtility.lensIsVisible);
        gm.addGazeListener(gazeListener);
        mainPane.getChildren().add(gazePos);

        //end eyetribe
        station = AnchorageSystem.createStation();
        SearchUtility.station = station;
        AnchorageSystem.installDefaultStyle();
        mainBorderPane.setCenter(station);

        //send the controller to the matrix scatter plot controller (dirty I know)
        spmController.setParentController(this);

        //add spmcontroller to list of brushables
        //it's very dumb to store a pointer like this, but I am not in the mood to do it otherwise
        SearchUtility.parentController = this;

        if (!GraphicUtility.doubleUser) {
            spRightController.setParentController(this);
            spRightController.setRighSide(true);
            spRightController.initRegressionModels();
        }

        spMainController.setParentController(this);
        spMainController.initRegressionModels();

        mainPane.setStyle("-fx-background-color: white");
        spMain.setStyle("-fx-background-color: white");
        if (!GraphicUtility.doubleUser) {
            spRight.setStyle("-fx-background-color: white");
        }

        similarityMap.setStyle("-fx-background-color: white");

        DockNode node1 = AnchorageSystem.createDock("Scatterplot", spMain);
        node1.dock(SearchUtility.station, DockNode.DockPosition.CENTER);
        node1.floatableProperty().set(false);

        DockNode node2 = AnchorageSystem.createDock("Scatterplot Matrix", spm);
        node2.dock(SearchUtility.station, DockNode.DockPosition.LEFT);
        node2.resizableProperty().set(false);

        DockNode node5 = AnchorageSystem.createDock("Similarity Map", similarityMap);
        node5.dock(SearchUtility.station, DockNode.DockPosition.RIGHT, 0.85);
        node5.resizableProperty().set(false);

        DockNode node3 = AnchorageSystem.createDock("Parallel Coordinates", parcoordChart);
        parcoordChart.setPadding(new Insets(0, 70, 0, 70));
        node3.dock(SearchUtility.station, DockNode.DockPosition.BOTTOM, 0.6);
        node3.resizableProperty().set(false);

        DockNode node4 = AnchorageSystem.createDock("Partitions", labelMenu);
        node4.dock(node3, DockNode.DockPosition.LEFT);
        node4.setPrefWidth(400);
        node4.setMaxWidth(400);
        node4.resizableProperty().set(false);

        DockNode console = AnchorageSystem.createDock("console", consoleArea);
        console.dock(node4, DockNode.DockPosition.LEFT);
        console.setPrefWidth(300);
        console.setMaxWidth(400);
        console.resizableProperty().set(false);

        initMenuItems();

        //init speech

        //initiate pacoord demo
        reorder.setOnMouseClicked(mouseEvent -> {

            AxisSortType sortType = AxisSortType.LEFT;
            switch (((RadioButton) orderingGroup.getSelectedToggle()).getId()) {
                case "left":
                    sortType = AxisSortType.LEFT;
                    break;
                case "right":
                    sortType = AxisSortType.RIGHT;
                    break;
                case "center":
                    sortType = AxisSortType.CENTER;
                    break;
            }


            switch (((RadioButton) typeGroup.getSelectedToggle()).getId()) {
                case "organisation":
                    parcoordChart.reOrderAxes(AxisOrderType.ORG, sortType, stepSpinner.getValue());
                    break;
                case "exploration":
                    parcoordChart.reOrderAxes(AxisOrderType.EXPL, sortType, stepSpinner.getValue());
                    break;
            }
        });

    }

    public void initMenuItems() {

        //init clustering
        RadioMenuItem kmeans = new RadioMenuItem("K-means");
        kmeans.setSelected(true);
        RadioMenuItem hierarchical = new RadioMenuItem("Hierarchical");

        ToggleGroup toggleGroupClustering = new ToggleGroup();
        toggleGroupClustering.getToggles().add(kmeans);
        toggleGroupClustering.getToggles().add(hierarchical);

        toggleGroupClustering.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (toggleGroupClustering.getSelectedToggle() != null) {
                    switch (((RadioMenuItem) newValue).getText()) {
                        case "K-means":
                            MLUtility.clusterType = clusteringType.KMEANS;
                            break;
                        case "Hierarchical":
                            MLUtility.clusterType = clusteringType.HIERARCHIAL;
                            break;

                    }

                }
            }
        });

        clusterType.getItems().add(kmeans);
        clusterType.getItems().add(hierarchical);

        //init dimensionality reduction
        RadioMenuItem tsne = new RadioMenuItem("t-SNE");

        //TODO change names of PCA and t-sne later
        RadioMenuItem pca = new RadioMenuItem("PCA");
        tsne.setSelected(
                true);
        RadioMenuItem mds = new RadioMenuItem("MDS");

        ToggleGroup toggleGroupDR = new ToggleGroup();

        toggleGroupDR.getToggles().add(tsne);
        toggleGroupDR.getToggles().add(pca);
        toggleGroupDR.getToggles().add(mds);

        drType.getItems().add(tsne);
        drType.getItems().add(pca);
        drType.getItems().add(mds);

        toggleGroupDR.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (toggleGroupDR.getSelectedToggle() != null) {
                    switch (((RadioMenuItem) newValue).getText()) {
                        case "t-SNE":
                            MLUtility.dimeRedType = dimReductionType.TSNE;
                            break;
                        case "PCA":
                            MLUtility.dimeRedType = dimReductionType.PCA;
                            break;
                        case "MDS":
                            MLUtility.dimeRedType = dimReductionType.MDS;
                            break;

                    }
                    similarityMapController.reset();

                }
            }
        });

    }

    /**
     * just a wrapper to call changeDataOnPlot from the main controller
     *
     * @param seriesList
     * @param xAxis
     * @param yAxis
     * @param scatterName
     */
    public void changeDataOnPlotWrapper(ObservableList<XYChart.Series<Number, Number>> seriesList, String xAxis, String yAxis, String scatterName) {

        if (!GraphicUtility.doubleUser) {
            spMainController.changeDataOnPlot(seriesList, xAxis, yAxis);
//            scatterPaneLeft.requestFocus();

        } else {
            if (scatterName.equals("right")) {
                spRightController.changeDataOnPlot(seriesList, xAxis, yAxis);
                scatterPaneRight
                        .requestFocus();

            } else if (scatterName.equals("left")) {
                spMainController.changeDataOnPlot(seriesList, xAxis, yAxis);
                scatterPaneLeft
                        .requestFocus();

            }
        }

        //very important: with this function call the focus after choosing scatter plot will
        //go directly to scatter plot pane and the user won't experience any delay for change
        //remove the current lens from the scatter plot
        removeRecWrapper(scatterName);

    }

    /**
     * Just a wrapper for removing rec (lens) from the scatter plot
     */
    public void removeRecWrapper(String scatterName) {
        if (!GraphicUtility.doubleUser || scatterName.equals("left")) {
            spMainController.removeRect();

        } else if (scatterName.equals("right")) {
            spRightController.removeRect();

        }

    }

    /**
     * create stage for loading the data
     */
    public void loadDataStageCreation() {

        //creating a new stage
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/DataModelLoad.fxml"));
        Parent root = null;
        try {
            root = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        DataModelLoadController dataModelCtrl = fxmlLoader.getController();
        dataModelCtrl.setMainFXController(this);
        Scene loadScene = new Scene(root);

        // New window (Stage)
        loadStage = new Stage();
        dataModelCtrl.setLoadStage(loadStage);
        loadStage.setTitle("Load .CSV File");
        loadStage.setScene(loadScene);
        loadStage.centerOnScreen();
        // Specifies the modality for new window.
        loadStage.initModality(Modality.WINDOW_MODAL);

        // Specifies the owner Window (parent) for new window
        loadStage.initOwner(GraphicUtility.stage);

        loadStage.show();

        //finishing new stage
    }

    /**
     * Load data from a file to scatter plot matrix
     */
    public void loadData() {

        FileChooser fileChooser = new FileChooser();
        fileChooser
                .setTitle("Open Resource File");

        //change file chooser to choose csv files and location in ./data
        FileChooser.ExtensionFilter extentionFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser
                .getExtensionFilters().add(extentionFilter);

        File userDirectory = new File(System.getProperty("user.home"));

        Preferences prefs = Preferences.userNodeForPackage(DataLoadUtility.class);
        String preferedLoc = prefs.get("fileLoaderLocation", userDirectory.getPath());
        userDirectory = new File(preferedLoc);

        fileChooser.setInitialDirectory(userDirectory);

        File file = fileChooser.showOpenDialog(null);

        //load from file
        if (file != null) {
            //load SPLOM
            prefs.put("fileLoaderLocation", file.getParentFile().getPath());
            spmModel = DataLoadUtility.loadData(file);
            //init categories (aka class, cluster)
            spmModel.getModel().initClassNames();
            spmController
                    .initSPLOMsFromModel(spmModel);

            initLabels();
            loadParcoord(false);
            loadStage.close();

        } else {
            System.out.println("Open command cancelled by user.");

        }

        //perform DR
        initDimensionReduction();

        //remove the gaze control pane
        if (!gazeListener.isOnGazeParcoord())
            informationPane.getChildren().remove(gazeCtrlHBox);
//            gazeCtrlHBox.setVisible(false);

    }

    /**
     * perform DR on the data
     */
    public void initDimensionReduction() {
        similarityMapController.initSimilarityRecords();
        similarityMapController
                .initSimilarityPartitions();

    }

    /**
     * init labels in label panel
     */
    private void initLabels() {
        //init rest of labels
        labelMenuController.initLabelsBulk(spmModel.getModel().getClassNames());
    }

    /**
     * loading parallel coordinate chart
     */
    public void loadParcoord(boolean drawLegend) {
        //            //pacoord loading
        if (spmModel.getModel() != null) {
            ObservableList<Item> items = spmModel.getModel().getItems();

            Series s1 = new Series("Series1", items, Color.RED, 0.2);

            //storing all series here
            ArrayList<List<Item>> allSeries = new ArrayList();

            parcoordChart
                    .clear();
            parcoordChart
                    .setMinMaxValuesFromArray(spmModel.getModel().getMinMaxValues());

            ArrayList<String> tempAttr = new ArrayList();

            if (spmModel.getModel().getNrOfCatDims() > 0) {
                for (int i = 0; i
                        < spmModel.getModel().getDataHeader().size(); i++) {
                    if (spmModel.getModel().getClassIndex().get(0) != i
                            && spmModel.getModel().getNameDimIndex() != i) {
                        tempAttr.add(spmModel.getModel().getDataHeader().get(i));

                    }
                }
                parcoordChart.setAxisLabels(tempAttr);

            } else {
                parcoordChart.setAxisLabels(spmModel.getModel().getDataHeader());

            }

            //get what is index is for classification of attributes (categories)
            int tempClassIndex = spmModel.getModel().getClassIndex().get(0);

            ObservableList<Item> allData = FXCollections.observableArrayList();

            for (int i = 0; i
                    < spmModel.getModel().getCopyClassNames().size(); i++) {
//                List<Item> tempList = new LinkedList();
                for (Item item : items) {
                    if (item.getAttByIndex(tempClassIndex).equals(spmModel.getModel().getCopyClassNames().get(i))) {
                        allData.add(item);
//                        tempList.add(item);

                    }
                }

            }
            parcoordChart.addSeries(new Series("unkown",
                    allData, ChartStyleUtility.colors.get(0), 0.2));
//                parcoordChart.addSeries(s1);

            parcoordChart
                    .setHighlightColor(Color.BLACK);
            parcoordChart
                    .setHighlightStrokeWidth(3);

            if (drawLegend) {
                parcoordChart.drawLegend();

            }
            parcoordChart.enableBrushing();
            BrushMngUtility.brushables.add(parcoordChart);
            BrushMngUtility.dimBrushables.add(parcoordChart);

        }
    }

    public void searchWrapper(Box box) {
//        RadioButton b = (RadioButton) searchMethod.getSelectedToggle();
        spmController.getSploms().compareAllChartsModel(box);

        if (!spmController.getSploms().isPreProcessed()) {
            spmController.getSploms().preProcessForSearch();
            spmController
                    .getSploms().setPreProcessed(true);

        }
//        switch (b.getId()) {
//            case "model":
//                spmController.getSploms().compareAllChartsModel(box);
//                break;
//            case "2d":
//                spmController.getSploms().compareAllCharts2D(box);
//                break;
//            case "confirm":
//                spmController.getSploms().compareAllChartsConfirm(box);
//                break;
//        }
    }

    /**
     * search for sketch in SPLOMs
     *
     * @param scatterName
     */
    public void searchForSketch(String scatterName) {
        resetVisualizedPatternsWrapper();
        IndexSearch<List<XYChart.Series<Number, Number>>> search;

        if (GraphicUtility.doubleUser && scatterName.equals("right")) {
//            search = spmController.index.findNeighbors(Lists.newArrayList(spRightController.getSketch()), null, new Double(pattNumSlider.getValue()).intValue(), Double.MAX_VALUE);
            search = spmController.index.findNeighbors(Lists.newArrayList(spRightController.getSketch()), null, 15, Double.MAX_VALUE);

        } else {
            search = spmController.index.findNeighbors(Lists.newArrayList(spMainController.getSketch()), null, 15, Double.MAX_VALUE);

        }
        search.addCallbacks(results -> {

                    final DMatrixRMaj sketchDescriptor = search.getQuery().getDescriptor();
                    final List<Integer> closestSegmentIndices = new ArrayList<>();

                    double minDis = results.get(0).getDistance();

                    double maxDis = results.get(results.size() - 1).getDistance();
                    results
                            .forEach(result -> {

                                final double minDist = result.getDistance();
                                final DMatrixRMaj plotDesc = result.get().getDescriptor();

                                for (int i = 0; i
                                        < plotDesc.numRows;
                                     ++i) {
                                    DMatrixRMaj segmentDescriptor = CommonOps_DDRM.extractRow(plotDesc, i, null);
                                    closestSegmentIndices
                                            .add(i);

                                }

                                //xAxis separated[1];
                                //yAxis separated[2];
                                //seg number closestSegmentIndices.get(closestSegmentIndices.size() - 1)
                                String[] separated = result.get().getIndexedName().split("/");

                                visualizePatternWrapper(separated[1], separated[2], closestSegmentIndices.get(closestSegmentIndices.size() - 1), result.getDistance(), maxDis, minDis);

                                if (log.isDebugEnabled()) {

                                    log.debug("Found result, Scatter Plot Name name = " + result.get().getIndexedName()
                                            + "\nclosest segment=" + closestSegmentIndices.get(closestSegmentIndices.size() - 1)
                                            + "\ndistance" + result.getDistance()
                                            + "\nsketchDescriptor= " + sketchDescriptor
                                            + "\nplotDescriptor= " + plotDesc);

                                }

                            }); //end forEach result
                    //something is really wrongt if this is not true
                    assert (closestSegmentIndices.size() == results.size());

                },
                ex -> {
                    log.error("Sketch Search failed, lets cry now!", ex);

                },
                true);

    }

    public void resetVisualizedPatternsWrapper() {
        spmController.resetVisualizedPatterns();

    }

    public void visualizePatternWrapper(String xDim, String yDim, int seriesIndex, double distance, double maxDis, double minDis) {
        spmController.visualizePattern(xDim, yDim, seriesIndex, distance, maxDis, minDis);

    }

    public AnchorPane
    getMainPane() {
        return mainPane;

    }

    public ConsolePaneController getConsolePaneController() {
        return consoleAreaController;
    }


    public LabelPaneController
    getLabelPaneController() {
        return labelMenuController;

    }

    public Circle getGazePos() {
        return gazePos;

    }

    public void updateGazePos(double x, double y) {
        gazePos.setCenterX(x);
        gazePos.setCenterY(y);

    }

    /**
     * update color of gaze circle if it is fixated
     */
    public void updateColorGazeCircle(boolean fix) {
        if (fix) {
            gazePos.setStyle(" -fx-border-color: silver;");
            gazePos.setStroke(Color.RED);
            gazePos.setStrokeWidth(2);

        } else {
            gazePos.setStyle(" -fx-border-color: silver;");
            gazePos.setStroke(Color.RED);
            gazePos.setStrokeWidth(0.5);
        }
    }

    public void setLeftMenuController(ContextMenuController leftMenuController) {
        this.leftMenuController = leftMenuController;

    }

    public void setLabelPaneController(LabelPaneController labelPaneController) {
        this.labelMenuController = labelPaneController;

    }

    public void setRightMenuController(ContextMenuController rightMenuController) {
        this.rightMenuController = rightMenuController;

    }

    public void setConsolePaneController(ConsolePaneController consolePaneController) {
        this.consoleAreaController = consolePaneController;
    }


    public ContextMenuController
    getLeftMenuController() {
        return leftMenuController;

    }

    public ContextMenuController
    getRightMenuController() {
        return rightMenuController;

    }

    public MatrixScatterPlotController
    getSpmController() {
        return spmController;

    }

    public ParallelCoordinatesChart getParcoordChart() {
        return parcoordChart;
    }
}

//class GazeListener implements IGazeListener {
//
//    private GazeData gazeData;
//    private GazeData previousData;
//    private int notAGoodIdea = 0;
//
//    @Override
//    public void onGazeUpdate(GazeData gazeData) {
//        notAGoodIdea++;
//        if (previousData == null) {
//            previousData = gazeData;
//        }
//        this.gazeData = gazeData;
//        SearchUtility.parentController.updateColorGazeCircle(gazeData.isFixated);
//        SearchUtility.parentController.updateGazePos(this.gazeData.smoothedCoordinates.x, this.gazeData.smoothedCoordinates.y);
//
////        (Math.abs(gazeData.smoothedCoordinates.x - previousData.smoothedCoordinates.x) > 40 ||
////                Math.abs(gazeData.smoothedCoordinates.y - previousData.smoothedCoordinates.y) > 40)
//        if (gazeData.isFixated && notAGoodIdea % 30 == 0) {
//            SearchUtility.parentController.getSpMainController().updateLabelledListGaze();
//            this.previousData = gazeData;
//        }
//        //        SearchUtility.parentController.getSpMainController().selectBox.setX(this.gazeData.smoothedCoordinates.x - 100);
//        //        SearchUtility.parentController.getSpMainController().selectBox.setY(this.gazeData.smoothedCoordinates.y + 100);
//    }
//
//    public com.theeyetribe.clientsdk.data.Point2D getCoordinates() {
//        return gazeData.smoothedCoordinates;
//    }
//
//    public GazeData getData() {
//        return gazeData;
//    }
//
//}
