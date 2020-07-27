/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.utility;

import at.tugraz.cgv.multiviewva.gui.charts.Brushable;
import at.tugraz.cgv.multiviewva.gui.charts.DimBrushable;
import at.tugraz.cgv.multiviewva.model.Dimension;
import at.tugraz.cgv.multiviewva.model.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author mchegini
 */
public class BrushMngUtility {

    //all brushable charts
    public static ObservableList<Brushable> brushables = FXCollections.observableArrayList();

    public static void redrawSelectedAll() {
        brushables.forEach(brushable -> {
            brushable.redrawSelected();
        });
    }

    public static void redrawAllCategories() {
        brushables.forEach(brushable -> {
            brushable.redrawCategories();
        });
    }

    public static void highLightRecordTemp(boolean highlight, Item item) {
        brushables.forEach(brushable -> {
            brushable.highLightRecordTemp(highlight, item);
        });
    }

    //brushing
    public static boolean brushing = true;

    public static boolean brushingDim = true;

    public static ObservableList<DimBrushable> dimBrushables = FXCollections.observableArrayList();

    public static void brushAllDim() {
        dimBrushables.forEach(brushable -> {
            brushable.brushAllDim();
        });
    }

    public static void brushDimension(Dimension dimension, boolean brush) {
        dimBrushables.forEach(brushable -> {
            brushable.brushDimension(dimension, brush);
        });
    }

    public static void unbrushAllDim() {
        dimBrushables.forEach(brushable -> {
            brushable.unbrushAll();
        });
    }
}
