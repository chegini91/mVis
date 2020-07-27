/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.utility;

import at.tugraz.cgv.multiviewva.controllers.MainFXController;
import at.tugraz.cgv.multiviewva.gui.charts.Brushable;
import at.tugraz.cgv.multiviewva.model.DataModel;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.shape.Shape;
import at.tugraz.cgv.multiviewva.model.search.Box;
import com.anchorage.docks.stations.DockStation;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import org.ejml.data.DMatrixRMaj;

/**
 * a class full of quick and dirty static things :p
 * @author mohammad
 */
public class SearchUtility {
    
    //a dumb way to store parentcontroller
    public static MainFXController parentController;
    
    //a dumb way to store datamodel
    public static DataModel dataModel;

    //set how much the box should transfer every step
    public static double boxTransferStep = 0.2d;

    //set the main dock station
    public static DockStation station;
    
    /**
     * visual representation of heatmap, can be subset, all or area
     */
    public static String visualRepresentation = "subset";

    /**
     * store the last search query
     */
    public static Box lastSearchQuery = null;
    /**
     * color coding of heatmaps, can be combination, model or shape
     */
    public static String colorCoding = "combination";

    //set how much the box should scale in every step
    public static double boxScaleStep = 0.3d;
    public static double boxScaleStart = 0.3d;
    public static double boxScaleEnd = 1.0d;
    
    //number of points to compare 2 different 4 degree plot
    public static double num4DCompare = 100;
    
    //the ratio threshold difference between selected points and arbitary box
    public static double maxPointRatio = 2.0d;

    //min point threshold
    public static double minNumPoints = 10;

    public static int numbOfRow = 5;
    public static int numOfCol = 5;
    public static double searchInterval[][] = new double[100][100];

    public static DoubleProperty classSimilarity = new SimpleDoubleProperty(0.0);
    public static IntegerProperty numClusters = new SimpleIntegerProperty(4);
    /**
     * weight of descriptpor and Purity scores
     */
    public static double modelWeight = 0.5d;
    public static double shapeWeight = 0.5d;

    /**
     * shape and model minimum
     */
    public static double minModel = 30.0d;
    public static double minShape = 30.0d;
    public static double minSimilarity = 70.0d;
    
    /**
     * purity scores
     */
    public static double purity_1 = 0.0d;
    public static double purity_2 = 0.0d;

    /**
     * initialize all the intervals for further use
     */
    public static void init() {
        searchInterval[0][0] = 1;
        for (int i = 1; i < 100; i++) {
            for (int j = 0; j < i + 1; j++) {
                searchInterval[i][j] = (double) ((j + 1)) / (i + 1);
            }
        }
    }

    /**
     * get an observable list of points and return ArrayList of points2D
     *
     * @param points
     * @return
     */
    public static ArrayList<Point2D> getPoints2D(ObservableList<XYChart.Data<Number, Number>> points) {
        ArrayList<Point2D> ps2D = new ArrayList<>();

        points.stream().forEach(p -> {
            ps2D.add(new Point2D.Double(p.getXValue().doubleValue(), p.getYValue().doubleValue()));
        });
        return ps2D;
    }

    /**
     * create a similarity matrix for 2D histogram search based on quadratic
     * form distance
     *
     * @param num number of dimensions
     * @return a num x num similarity weight matrix
     */
    public static DMatrixRMaj getSimilarityWeightsMatrix(int num) {
        int dimension = num * num;
        DMatrixRMaj result = new DMatrixRMaj(dimension, dimension);
        int temp;
        double dis;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {

                temp = Math.abs(i - j);
                //distance between two points
                dis = (Math.sqrt((temp % num) * (temp % num) + (temp / num) * (temp / num)));
                //if distance is greater than 2, it doesn't matter
                if (dis > 2) {
                    result.set(i, j, 0);
                } else {
                    result.set(i, j, Math.pow(Math.E, 0 - dis));
                }
            }
        }
        return result;
    }
    

}
