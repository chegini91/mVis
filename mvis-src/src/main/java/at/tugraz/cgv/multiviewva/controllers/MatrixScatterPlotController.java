/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.controllers;

import com.google.common.base.Converter;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import at.tugraz.cgv.multiviewva.gui.charts.Brushable;
import at.tugraz.cgv.multiviewva.gui.charts.CustomChart;
import at.tugraz.cgv.multiviewva.gui.charts.SPLOMsChart;
import at.tugraz.cgv.multiviewva.model.EventLog;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.input.SwipeEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import at.tugraz.cgv.multiviewva.model.SPMModel;
import at.tugraz.cgv.multiviewva.model.indexing.FeatureExtractor;
import at.tugraz.cgv.multiviewva.model.indexing.IndexProperties;
import at.tugraz.cgv.multiviewva.model.indexing.ScatterPlot2DHistogramFE;
import at.tugraz.cgv.multiviewva.model.indexing.SimpleIndex;
import at.tugraz.cgv.multiviewva.model.indexing.metrics.Hausdorff;
import at.tugraz.cgv.multiviewva.model.indexing.metrics.L1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import at.tugraz.cgv.multiviewva.model.Item;
import at.tugraz.cgv.multiviewva.utility.BrushMngUtility;
import at.tugraz.cgv.multiviewva.utility.SearchUtility;

/**
 * FXML Controller class
 *
 * @author mchegini
 */
public class MatrixScatterPlotController implements Initializable {

    private static final Logger log = LoggerFactory.getLogger(MatrixScatterPlotController.class);

    /**
     *
     */
    public SimpleIndex<List<XYChart.Series<Number, Number>>, FeatureExtractor<List<XYChart.Series<Number, Number>>>> index;

    /**
     * keep the state of current selection of scatter plots
     */
    private String selectionState = "none";

    @FXML
    private SPLOMsChart sploms;

    @FXML
    private void onTouchPressed(TouchEvent event) {
        Node source = (Node) event.getSource();
        Integer colIndex = GridPane.getColumnIndex(source);
        Integer rowIndex = GridPane.getRowIndex(source);
        SPLOMsChart sp = (SPLOMsChart) source;
        if (sp != null) {
        }
    }

    @FXML
    private void onSwipeRight(TouchEvent event) {
//        Node source = (Node) event.getSource();
//
//        SPLOMsChart sp = (SPLOMsChart) source;
//        if (sp != null) {
//        }
    }

    @FXML
    private void onSwipeLeft(SwipeEvent event) {
        Node source = (Node) event.getSource();

        SPLOMsChart sp = (SPLOMsChart) source;

//        
//        System.out.println("okokok");
//        
//        if (!selectionState.equals("none") && !sploms.getSelectedSPMain().getSelected()) {
//            parentController.changeDataOnPlotWrapper(sploms.getSelectedSPMain().getData(), sploms.getDimensions().get(sploms.getSelectedSPMain().getPosition().x), sploms.getDimensions().get(sploms.getSelectedSPMain().getPosition().y), "left");
//        }
    }
    /**
     * parent controller
     */
    private MainFXController parentController;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sploms.getProperties().put("focusArea", "true");
        BrushMngUtility.brushables.add(sploms);
        BrushMngUtility.dimBrushables.add(sploms);
    }

    public SPLOMsChart getSploms() {
        return sploms;
    }

    public void setSploms(SPLOMsChart sploms) {
        this.sploms = sploms;
    }

    public void setParentController(MainFXController ctrl) {
        this.parentController = ctrl;
    }

    /**
     * set listeners. should called every time for new SPLOMsChart
     */
    public void setListeners() {

        sploms.getScList().forEach(scL -> {
            scL.forEach(sc -> {
                addListenersToScatterPlots(sc);
            });
        });
    }

    /**
     * two important listeners. First one listen to mouse and touch clicks and
     * set the clicked scatter plot to selectedSP second one listen to changes
     * and see if there is a new scatter plot that is now selected
     *
     * @param sc
     */
    private void addListenersToScatterPlots(CustomChart sc) {

        //tootip for onhover
//        Tooltip.install(
//                sc,
//                sc.getTooltip()
//        );
        sc.setOnZoom(event -> {
            if (sc.getMaximised() == null) {
                selectionState = "none";

                sploms.setSelectedSPMain(null);
                sploms.setSelectedSPRight(null);
                sc.setSelected(false);

                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/maximizeChart.fxml"));
                Parent parent = null;

                try {
                    parent = (Parent) fxmlLoader.load();
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(MatrixScatterPlotController.class.getName()).log(Level.SEVERE, null, ex);
                }

                MaximizeChartController maxCtrl = fxmlLoader.getController();
                //            maxCtrl.setStage(stage);
                //            leftController.setRightSide(false);
                //            leftController.setMainFXController(mainController);

                Scene leftMenuScene = new Scene(parent);
                stage.setScene(leftMenuScene);
                //            stage.initStyle(StageStyle.UNDECORATED);
                stage.setAlwaysOnTop(true);
                stage.show();
                maxCtrl.addChart(sc);
            }
            event.consume();
        });

        sc.setOnSwipeLeft(event -> {
            if (!sploms.backGroundJob) {
//                sploms.backGroundJob = true;
                sploms.setSelectedSPMain(sc);
                if (!sc.getSelected()) {
                    parentController.changeDataOnPlotWrapper(sc.getData(), sc.getxAxis(), sc.getyAxis(), "left");
                }
                sc.setSelected(true);
            }
            sploms.backGroundJob = false;
            event.consume();
        });

        sc.setOnMouseClicked(event -> {
//            System.out.println(sc.getPosition().x + " " + sc.getPosition().y);
            if (!sploms.backGroundJob) {
//                sploms.backGroundJob = true;
                sploms.setSelectedSPMain(sc);
                if (!sc.getSelected()) {
                    parentController.changeDataOnPlotWrapper(sc.getData(), sc.getxAxis(), sc.getyAxis(), "left");
                }
                sc.setSelected(true);
            }
            sploms.backGroundJob = false;

            event.consume();
        });


        /*
         sc.setOnSwipeLeft(event -> {

         if (!sploms.backGroundJob) {
         //                sploms.backGroundJob = true;
         sploms.setSelectedSPRight(sc);
         if (!sc.getSelected()) {
         parentController.changeDataOnPlotWrapper(sc.getData(), sploms.getDimensions().get(sc.getPosition().x), sploms.getDimensions().get(sc.getPosition().y), "right");
         }
         sc.setSelected(true);
         }
         sploms.backGroundJob = false;
         });
         */
        //add listener to each plot, now when a new plot is selected
        //we will be informed
        sc.selectedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                sploms.getScList().forEach(scL -> {
                    scL.forEach(sc -> {
                        if (!sc.equals(sploms.getSelectedSPRight())
                                && !sc.equals(sploms.getSelectedSPMain())) {
                            sc.setSelected(false);
                        }
                    });
                });
            }

        }
        );
    }

    public void initSPLOMsFromModel(SPMModel spmModel) {
        //clean UI
        this.getSploms().cleanUI();

        //set preprocessed false
        this.getSploms().setPreProcessed(false);

        //set data model of sploms
        this.getSploms().setDataModel(spmModel);

        this.setListeners();

        initIndex();
    }

    /**
     * initialize the index
     */
    public void initIndex() {
        ScatterPlot2DHistogramFE extractor = new ScatterPlot2DHistogramFE();
        HashBiMap<String, List<XYChart.Series<Number, Number>>> plotsByName = HashBiMap.create();
        sploms.getScList().stream().forEach((scL) -> {
            scL.stream().forEach(sc -> {
                plotsByName.put(sploms.getDataModel().getFilename() + "/" + sc.getxAxis() + "/" + sc.getyAxis(), sc.getData());
            });
        });

        Converter<List<XYChart.Series<Number, Number>>, String> objectNameConverter = Converter.from(
                chart -> {
                    assert chart != null;// this should not happen, even in testing
                    String name = plotsByName.inverse().get(chart);
                    if (name == null) {
                        return "sketch";
                    }
                    return name;
                },
                name -> {
                    assert name != null; // this should not happen, even in testing
                    List<XYChart.Series<Number, Number>> plot = plotsByName.get(name);
                    if (plot == null) {
                        throw new RuntimeException("NIL");
                    }
                    return plot;
                });
        //new QuadraticForm(SearchUtility.getSimilarityWeightsMatrix(4))
        index = new SimpleIndex<>(extractor, objectNameConverter, new Hausdorff<>(new L1()), IndexProperties.indexingPropertiesDefaultWithOverrides(
                ImmutableMap.of(
                        IndexProperties.IDX_KEY_CACHE_DIR,
                        IndexProperties.IDX_VAL_CACHE_DIR_USER_HOME + sploms.getDataModel().getFilename())));
        index.waitForOpFinish(10, 100000);

        index.add(Lists.newArrayList(plotsByName.values()),//List of objects to add
                null,//properties to pass to the feature extractor
                //onSuccess : what to do if things went well... this might be handy in an event driven UI
                indexedContainers -> {
                    if (log.isInfoEnabled()) {
                        indexedContainers.forEach(c -> {
                            log.info("successfully added object:" + c.getIndexedName() + " with descriptor: " + c.getDescriptor());
                        });
                    }

                },
                //onError
                e -> {
                    if (e instanceof Exception) {
                        log.error("Something went wrong:", (Exception) e);
                    }
                }
        );
    }

    public void resetVisualizedPatterns() {
        sploms.getScList().forEach(scL -> {
            scL.forEach(sc -> {
//                sc.resetHighLightedSeries();
                sc.makeEverythingGrey();
            });
        });
    }

    public void visualizePattern(String xDim, String yDim, int seriesIndex, double distance, double maxDis, double minDis) {
        sploms.getScList().forEach(scL -> {
            scL.forEach(sc -> {
                if (sc.retriveChart(xDim, yDim)) {
                    sc.highLightSeries(seriesIndex, distance, maxDis, minDis);
                }
            });
        });
    }

}
