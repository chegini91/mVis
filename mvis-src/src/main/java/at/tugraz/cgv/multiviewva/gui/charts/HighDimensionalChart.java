/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.gui.charts;

import at.tugraz.cgv.multiviewva.model.MinMaxPair;
import at.tugraz.cgv.multiviewva.model.Series;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.Chart;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

import java.util.ArrayList;
import java.util.List;
import at.tugraz.cgv.multiviewva.model.Item;

/**
 * @author mchegini
 */
public abstract class HighDimensionalChart extends Chart {
    protected ObservableList<Series> series = FXCollections.observableArrayList();
    protected boolean showLabels = true;

    /**
     * Property holding the height of the chartContent which is updated with each layoutChartChildren call.
     * Represents inner values (without padding, titleLabel, etc.)
     */
    protected DoubleProperty innerHeightProperty = new SimpleDoubleProperty();
    
    /**
     * Property holding the width of the chartContent which is updated with each layoutChartChildren call.
     * Represents inner values (without padding, etc.)
     */
    protected DoubleProperty innerWidthProperty = new SimpleDoubleProperty();

    /**
     * Holds the minimum and maximum values per dimension (to reconstruct normalized data).
     */
    protected List<MinMaxPair> minMaxValues;


    public HighDimensionalChart() {
    }

    /**
     * Draws a border around the chart
     */
    protected void drawBorder() {
        Path path = new Path();

        MoveTo moveTo = new MoveTo();
        moveTo.setX(0);
        moveTo.setY(0);
        path.getElements().add(moveTo);

        LineTo lineTo = new LineTo();
        lineTo.setX(innerWidthProperty.doubleValue());
        lineTo.setY(0);
        path.getElements().add(lineTo);

        lineTo = new LineTo();
        lineTo.setX(innerWidthProperty.doubleValue());
        lineTo.setY(innerHeightProperty.doubleValue());
        path.getElements().add(lineTo);

        lineTo = new LineTo();
        lineTo.setX(0);
        lineTo.setY(innerHeightProperty.doubleValue());
        path.getElements().add(lineTo);

        lineTo = new LineTo();
        lineTo.setX(0);
        lineTo.setY(0);
        path.getElements().add(lineTo);

        getChartChildren().add(path);
    }


    /**
     * Immediately clears the whole chart and chartChildren
     */
    public void clear() {
        getChartChildren().clear();
        series.clear();
    }

    /**
     * Adds a series to the given Chart
     * Inspired by: http://docs.oracle.com/javafx/2/charts/scatter-chart.htm
     *
     * @param s the series to add to this Chart
     */
    public void addSeries(Series s) {
        boolean firstDraw = series.isEmpty();
        series.add(s);

        if (firstDraw) {
            createAxes();
            bindAxes();
        }

        bindSeries(s);
        // TODO: why is this needed?
        redrawAllSeries();
        reorder();
    }

    /**
     * Removes Series from the list of all series in this Chart
     * TODO: test
     *
     * @param index of series to remove
     */
    public void removeSeries(int index) {
        removeSeries(series.get(index));
    }

    
    /**
     * Removes Series from the list of all series in this Chart
     * TODO: test
     *
     * @param s the series to be removed
     */
    public void removeSeries(Series s) {
        for (Item r : s.getItems()) {
            getChartChildren().removeAll(r.getPath());
        }
        series.remove(s);
    }

    /**
     * remove all Series from the graph
     * TODO: test
     */
    public void clearSeries() {
        // List<Path> paths = new ArrayList<>();
        for (Series s : series) {
            for (Item r : s.getItems()) {
                getChartChildren().removeAll(r.getPath());
            }
        }
        series.clear();
    }

    /**
     * Calculates the minimum and maximum value per dimension of the data given by all series and contained records.
     * Can only be used if the data is not normalized.
     *
     * @return A List of MinMaxPairs ordered by dimensions as present in the data.
     */
    @Deprecated
    public List<MinMaxPair> calculateMinMaxPerAxis() {
        List<MinMaxPair> result = new ArrayList<MinMaxPair>();

        int nrDim = series.get(0).getItem(0).getValues().size();

        for (int i = 0; i < nrDim; i++) {
            result.add(new MinMaxPair(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY));
        }

        for (Series s : series) {
            for (Item r : s.getItems()) {
                for (int dim = 0; dim < nrDim; dim++) {
                    //TODO remove this check
                    if (r.getValues().get(dim) instanceof Number) {
                        double value = (double) r.getValues().get(dim);

                        if (value < result.get(dim).getMinimum())
                            result.get(dim).setMinimum(value);

                        if (value > result.get(dim).getMaximum())
                            result.get(dim).setMaximum(value);
                    }
                }
            }
        }

        return result;
    }

    /**
     * Sets the minimum and maximum values per dimension.
     *
     * @param minMaxArray A List of Double[]. Each element of the list represents one dimension.
     *                    The first element of each Double[] is the minimum value, the second
     *                    the maximum value.
     */
    public void setMinMaxValuesFromArray(List<Double[]> minMaxArray) {
        minMaxValues = new ArrayList<MinMaxPair>();
        for (Double[] ar : minMaxArray) {
            minMaxValues.add(new MinMaxPair(ar[0], ar[1]));
        }
    }

    public List<MinMaxPair> getMinMaxValues() {
        return minMaxValues;
    }

    public void setMinMaxValues(List<MinMaxPair> minMaxValues) {
        this.minMaxValues = minMaxValues;
    }

    /**
     * Subclasses should implement this method to create axes for the chart (but NOT bind it)
     */
    protected abstract void createAxes();

    /**
     * Subclasses should implement this method to bind axes to the chart
     */
    protected abstract void bindAxes();


    /**
     * Subclasses should implement this method to bind a given series to the chart
     * also, the paths of the records should be set accordingly
     */
    protected abstract void bindSeries(Series s);

    /**
     * Subclasses should implement this method to reorder certain elements in the
     * z dimension (if necessary)
     */
    protected abstract void reorder();

    protected abstract void redrawAllSeries();
}
