/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.controllers;

import at.tugraz.cgv.multiviewva.gui.charts.Brushable;
import at.tugraz.cgv.multiviewva.gui.charts.ScatterPlotExtended;
import at.tugraz.cgv.multiviewva.model.Item;
import at.tugraz.cgv.multiviewva.model.LabelModel;
import at.tugraz.cgv.multiviewva.utility.BrushMngUtility;
import at.tugraz.cgv.multiviewva.utility.ChartStyleUtility;
import at.tugraz.cgv.multiviewva.utility.GraphicUtility;
import at.tugraz.cgv.multiviewva.utility.MLUtility;
import at.tugraz.cgv.multiviewva.utility.SearchUtility;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;

/**
 * FXML Controller class
 *
 * @author chegini
 */
public class SimilarityMapController implements Initializable, Brushable {

    @FXML
    AnchorPane anchorPane;

    /**
     * for t-sne dimension reduction view
     */
    @FXML
    private AnchorPane records;

    @FXML
    private AnchorPane partitions;

    private ScatterPlotExtended recordsPlot;

    private LineChart partitionsPlot;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //init the records t-sne
        recordsPlot = new ScatterPlotExtended(new NumberAxis(), new NumberAxis());
        records.getChildren().add(recordsPlot);

        //init the partitions similarity map
        partitionsPlot = new LineChart(new NumberAxis(), new NumberAxis());
        partitions.getChildren().add(partitionsPlot);

        //anchorpane parameters
        AnchorPane.setBottomAnchor(recordsPlot, 0.0);
        AnchorPane.setTopAnchor(recordsPlot, 0.0);
        AnchorPane.setLeftAnchor(recordsPlot, 0.0);
        AnchorPane.setRightAnchor(recordsPlot, 0.0);

        AnchorPane.setBottomAnchor(partitionsPlot, 0.0);
        AnchorPane.setTopAnchor(partitionsPlot, 0.0);
        AnchorPane.setLeftAnchor(partitionsPlot, 0.0);
        AnchorPane.setRightAnchor(partitionsPlot, 0.0);

        BrushMngUtility.brushables.add(this);

        partitionsPlot.setAnimated(false);
        anchorPane.getStylesheets().add("styles/similarity.css");

        ((NumberAxis) partitionsPlot.getXAxis()).setAutoRanging(false);
        ((NumberAxis) partitionsPlot.getYAxis()).setAutoRanging(false);
        ((NumberAxis) partitionsPlot.getXAxis()).setUpperBound(5.0);
        ((NumberAxis) partitionsPlot.getXAxis()).setLowerBound(-5.0);
        ((NumberAxis) partitionsPlot.getYAxis()).setUpperBound(5.0);
        ((NumberAxis) partitionsPlot.getYAxis()).setLowerBound(-5.0);

//        ((NumberAxis) recordsPlot.getXAxis()).setAutoRanging(false);
//        ((NumberAxis) recordsPlot.getYAxis()).setAutoRanging(false);
        recordsPlot.setVerticalGridLinesVisible(false);
        recordsPlot.setHorizontalGridLinesVisible(false);
        ((NumberAxis) recordsPlot.getXAxis()).setUpperBound(4.0);
        ((NumberAxis) recordsPlot.getXAxis()).setLowerBound(-4.0);
        ((NumberAxis) recordsPlot.getYAxis()).setUpperBound(4.0);
        ((NumberAxis) recordsPlot.getYAxis()).setLowerBound(-4.0);
    }

    /**
     * initiating t-SNE chart on the other tab
     */
    public void initSimilarityRecords() {

        XYChart.Series<Number, Number> tempSeries = new XYChart.Series();

        SearchUtility.dataModel.getItems().forEach(item -> {
            XYChart.Data<Number, Number> tempData = null;
            switch (MLUtility.dimeRedType) {
                case PCA:
                    tempData = new XYChart.Data<>(item.getPcaValues()[0], item.getPcaValues()[1]);
                    break;
                case TSNE:
                    tempData = new XYChart.Data<>(item.getTsneValues()[0], item.getTsneValues()[1]);
                    break;
                case MDS:
                    tempData = new XYChart.Data<>(item.getMdsValues()[0], item.getMdsValues()[1]);
                    break;
            }
            tempData.setExtraValue(item.getIndex());
            tempSeries.getData().add(tempData);
        });

        tempSeries.getData().forEach(pt -> {
            pt.setNode(new SVGPath());
        });

        recordsPlot.getData().add(tempSeries);
        recordsPlot.setLegendVisible(false);

        ChartStyleUtility.addListenersRecords(recordsPlot.getData());

        redrawCategories();
        
    }


    /**
     * initiating t-SNE chart on the other tab
     */
    public void initSimilarityPartitions() {

        partitionsPlot.getData().clear();

        XYChart.Series<Number, Number> tempSeries = new XYChart.Series();

        SearchUtility.dataModel.getClassNames().forEach(label -> {
            if (SearchUtility.dataModel.clusterSize(label.getName()) > 0) {
                XYChart.Data<Number, Number> tempData
                        = SearchUtility.dataModel.centroidValue(label.getName());
                if (tempData != null) {
                    tempSeries.getData().add(tempData);
                }
            }

        });

//         SearchUtility.dataModel.getClassNames().get(0).
        tempSeries.getData().forEach(pt -> {
            pt.setNode(new SVGPath());
        });

        partitionsPlot.getData().add(tempSeries);
        partitionsPlot.setLegendVisible(false);

        //add links
        int sizeClasse = SearchUtility.dataModel.getClassNames().size();
        partitionsPlot.setCreateSymbols(true);
        for (int i = 0; i < sizeClasse; i++) {
            for (int j = 0; j < i; j++) {
//                if (SearchUtility.dataModel.
//                        intersectionLabelsDimnsions(SearchUtility.dataModel.getClassNames().get(i),
//                                SearchUtility.dataModel.getClassNames().get(j)) != null) {
                String firstLabel = SearchUtility.dataModel.getClassNames().get(i).getName();
                String secondLabel = SearchUtility.dataModel.getClassNames().get(j).getName();
                if (SearchUtility.dataModel.clusterSize(firstLabel) > 0
                        && SearchUtility.dataModel.clusterSize(secondLabel) > 0) {

                    Set<String> tempSet = SearchUtility.dataModel.
                            intersectionLabelsDimnsions(SearchUtility.dataModel.getClassNames().get(i),
                                    SearchUtility.dataModel.getClassNames().get(j));
                    if (tempSet.size() > 0) {
                        XYChart.Series<Number, Number> linkSeries = new XYChart.Series();

                        XYChart.Data<Number, Number> firstLabelPos
                                = SearchUtility.dataModel.centroidValue(firstLabel);
                        XYChart.Data<Number, Number> secondLabelPos
                                = SearchUtility.dataModel.centroidValue(secondLabel);
                        linkSeries.getData().addAll(firstLabelPos, secondLabelPos);
                        partitionsPlot.getData().add(linkSeries);
                        //make label on the link
                        String hoverText = "";
                        for (String dim : tempSet) {
                            hoverText = hoverText + "(" + dim + ")\n";
                        }
                        linkSeries.getNode().setStyle("-fx-stroke-width:" + tempSet.size() * 2 + ";-fx-stroke: #808080;");
                        Tooltip t = new Tooltip(hoverText);
                        linkSeries.getNode().getProperties().put("Tooltip", t);
                        GraphicUtility.hackTooltipStartTiming(t);
                        Tooltip.install(linkSeries.getNode(), t);
                    }
                }
            }
        }

        ObservableList<XYChart.Series<Number, Number>> list = partitionsPlot.getData();
        list.stream().forEach(series -> {

            series.getData().forEach(pt -> {
                String tooltip = "Name: " + (String) pt.getExtraValue() + "\n\n Related Dimensions:";
                //add all relative dimensions
                LabelModel tempLabel = SearchUtility.dataModel.getLabelModel((String) pt.getExtraValue());

                for (String dim : tempLabel.getDimeInteraction()) {
                    tooltip = tooltip + "\n(" + dim + ")";
                }
                Tooltip t = new Tooltip(tooltip);
                pt.getNode().getProperties().put("Tooltip", t);
                GraphicUtility.hackTooltipStartTiming(t);

                Tooltip.install(pt.getNode(), t);
            });
        });

        XYChart.Series<Number, Number> series = (XYChart.Series<Number, Number>) partitionsPlot.getData().get(0);

        series.getData().forEach(pt -> {
            pt.getNode().setStyle(null);
            pt.getNode().setEffect(null);
            String colorCircl = (String) SearchUtility.dataModel.colorofClass((String) pt.getExtraValue());

            ((Shape) pt.getNode()).setFill(
                    Color.web(colorCircl));
            //if data is labelled, then make a border, otherwise, don't
            ((SVGPath) pt.getNode()).setContent("M 4, 4 ,m -4, 0 ,a 4,4 0 1,0 8,0, a 4,4 0 1,0 -8,0");
            pt.getNode().setScaleX(SearchUtility.dataModel.ratioofCluster((String) pt.getExtraValue()));
            pt.getNode().setScaleY(SearchUtility.dataModel.ratioofCluster((String) pt.getExtraValue()));
        });

//        if (listPartition.get(0).getData().size() > 2) {
//            partitionsPlot.addLineToChart(listPartition.get(0).getData().get(0),
//                    listPartition.get(0).getData().get(1));
//
//        }
    }

    @Override
    public void redrawSelected() {
        //for dimensionReduction view
        drawAllBackground();
        ObservableList<XYChart.Series<Number, Number>> tsneData = recordsPlot.getData();
        SearchUtility.dataModel.getSelectedItems().forEach(item -> {
            outerloop:
            for (XYChart.Series<Number, Number> o : tsneData) {
                for (int i = 0; i < o.getData().size(); i++) {
                    if (o.getData().get(i).getExtraValue().equals(item)) {
                        o.getData().get(i).getNode().setStyle(null);
                        o.getData().get(i).getNode().setEffect(null);
                        break outerloop;
                    }
                }
            }
        });

    }

    @Override
    public void drawAllBackground() {
        ObservableList<XYChart.Series<Number, Number>> list = recordsPlot.getData();
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

    @Override
    public void redrawCategories() {
        //for scatterplot
        ObservableList<XYChart.Series<Number, Number>> list = recordsPlot.getData();
        list.stream().forEach(series -> {

            series.getData().forEach(pt -> {
                ChartStyleUtility.coloringNodeUtility(pt, false);
            });
        });

        //for clusters
        initSimilarityPartitions();
    }

    /**
     * reset similarity maps, useful when the algorithm changes
     */
    public void reset() {
        initSimilarityPartitions();
        recordsPlot.getData().clear();
        initSimilarityRecords();
    }

    @Override
    public void highLightRecordTemp(boolean highlight, Item item) {
        ObservableList<XYChart.Series<Number, Number>> list = recordsPlot.getData();
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
