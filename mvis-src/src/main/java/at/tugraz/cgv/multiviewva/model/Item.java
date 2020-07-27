/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.model;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.shape.Path;
import at.tugraz.cgv.multiviewva.gui.charts.ParallelCoordinatesChart;
import at.tugraz.cgv.multiviewva.model.Series;

/**
 * An item is a row in data set
 *
 * @author mchegini
 */
public class Item {

    /**
     * unique index of item
     */
    private int index;

    /**
     * name of the record
     */
    private String name;

    public enum Status {

        VISIBLE, // indicates that this record is visible
        OPAQUE, // indicates that this record is hidden
        NONE, // indicates that no meaningful status can be applied (should be treated like VISIBLE for drawing)
        INVISIBLE, //indicates that the record is not visible
    }

    /**
     * ArrayList of all attributes
     */
//    private ArrayList<Object> attributes = new ArrayList<>();
    /**
     * TODO: calculate when adding the path for the record Path for
     * pacoordinates
     */
    private Path path;

    /**
     * if the item is generally visible or completely invisible
     */
    private Item.Status generalVisibleCategory = Item.Status.VISIBLE;

    /**
     * indicates the current status of the record concerning axisFilters for
     * pacoord
     */
    private Item.Status axisFilterStatus = Item.Status.VISIBLE;

    /**
     * indicates the current status of the record (concerning brushing). Is NONE
     * by default to show that no brushing has affected the record yet.
     */
    private Item.Status brushingStatus = Item.Status.NONE;

    /**
     * indicates the current status of the record (concerning highlighting). Is
     * NONE by default to show that no brushing has affected the record yet.
     */
    private Item.Status highlightingStatus = Item.Status.NONE;

    /**
     * values of the record
     */
    private ObservableList<Object> values = FXCollections.observableArrayList();

    /**
     * item in reduced dimension (2 dimensions)
     */
    private double[] pcaValues = new double[2];

    /**
     * item in reduced dimension (2 dimensions)
     */
    private double[] tsneValues = new double[2];

    /**
     * item in reduced dimension (2 dimensions)
     */
    private double[] mdsValues = new double[2];

    /**
     * the series this record belongs to
     */
    private Series series;

    /**
     * number of attributes
     */
    private int nrOfAttributes;

    /**
     * number of category dimensions
     */
    private int nrOfCatDim;

    public Item(int nrOfAttributes, int nrOfCatDim, ObservableList<Object> values, String name, int index) {
        this.nrOfAttributes = nrOfAttributes;
        this.nrOfCatDim = nrOfCatDim;
        //TODO should remove either attribues or values
        this.values = values;
        brushingStatus = Item.Status.NONE;
        axisFilterStatus = Item.Status.NONE;
        this.name = name;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNrOfAttributes() {
        return nrOfAttributes;
    }

    public int getNrOfCatDim() {
        return nrOfCatDim;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setNrOfAttributes(int nrOfAttributes) {
        this.nrOfAttributes = nrOfAttributes;
    }

    public double[] getPcaValues() {
        return pcaValues;
    }

    public void setPcaValues(double[] pcaValues) {
        this.pcaValues = pcaValues;
    }

    public Status getGeneralVisibleCategory() {
        return generalVisibleCategory;
    }

    public void setGeneralVisibleCategory(Status generalVisibleCategory) {
        this.generalVisibleCategory = generalVisibleCategory;
    }

    public void setNrOfCatDim(int nrOfCatDim) {
        this.nrOfCatDim = nrOfCatDim;
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < nrOfAttributes; i++) {
            result = result + " " + this.getValues().get(i);
        }
        return result;
    }

    /**
     * Checks whether a record should be visible according to the statuses it
     * has been assigned.
     *
     * @return true if it should be visible, false otherwise
     */
    public boolean isVisible() {
        return !(axisFilterStatus == Item.Status.OPAQUE || brushingStatus == Item.Status.OPAQUE);
    }

    /**
     * Sets opacity, stroke and strokeWidth for the Path of this record
     * according to the various statuses this record has.
     *
     * @param chart The chart the record is contained in
     */
    public void updateStatus(ParallelCoordinatesChart chart) {
        updateStatus(chart, false);
    }

    /**
     * Sets opacity, stroke and strokeWidth for the Path of this record
     * according to the various statuses this record has.
     *
     * @param chart The chart the record is contained in
     * @param tempHighlight Whether highlighting is temporal and should be drawn
     * regardless of highlighting Status
     */
    
    public void updateStatus(ParallelCoordinatesChart chart, boolean tempHighlight) {
        if (!isVisible()) {
            path.setOpacity(chart.getFilteredOutOpacity());
        } else if (highlightingStatus == Item.Status.VISIBLE || tempHighlight) {
            path.setOpacity(chart.getHighlightOpacity());
            path.setStrokeWidth(chart.getHighlightStrokeWidth());
//            path.setStroke(series.getColor());
        } else {
            path.setOpacity(chart.getFilteredOutOpacity());
            path.setStrokeWidth(chart.getPathStrokeWidth());
//            path.setStroke(series.getColor());
        }
    }

    public Object getAttByIndex(int index) {
        return values.get(index);
    }



    public ObservableList<Object> getValues() {
        return values;
    }

    public void setValues(ObservableList<Object> values) {
        this.values = values;
    }


    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public Item.Status getAxisFilterStatus() {
        return axisFilterStatus;
    }

    public void setAxisFilterStatus(Item.Status axisFilterStatus) {
        this.axisFilterStatus = axisFilterStatus;
    }

    public Item.Status getBrushingStatus() {
        return brushingStatus;
    }

    public void setBrushingStatus(Item.Status brushingStatus) {
        this.brushingStatus = brushingStatus;
    }

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    public Item.Status getHighlightingStatus() {
        return highlightingStatus;
    }


    public double[] getTsneValues() {
        return tsneValues;
    }

    public double[] getMdsValues() {
        return mdsValues;
    }


    public void setTsneValues(double[] tsneValues) {
        this.tsneValues = tsneValues;
    }

    public void setMdsValues(double[] mdsValues) {
        this.mdsValues = mdsValues;
    }

    
    
    public void setHighlightingStatus(Item.Status highlightingStatus) {
        this.highlightingStatus = highlightingStatus;
    }

    /**
     * this one is used for dimension reduction methods
     *
     * @param vector
     * @return
     */
    public boolean equals(double[] vector) {

        for (int i = 0; i < vector.length; i++) {
            if (!values.contains(vector[i])) {
                return false;
            }
        }

        return true;
    }

}
