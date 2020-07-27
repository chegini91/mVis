/**
 * This class have some useful utility functions for dealing with HD.
 *
 * @author mchegini
 */
package at.tugraz.cgv.multiviewva.utility;

import at.tugraz.cgv.multiviewva.javafxapplication.JavaFXStart;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import at.tugraz.cgv.multiviewva.model.Pattern;
import at.tugraz.cgv.multiviewva.model.Point2D;
import at.tugraz.cgv.multiviewva.model.SPMModel;
import java.util.List;
import javafx.scene.paint.Color;
import at.tugraz.cgv.multiviewva.model.Series;
import at.tugraz.cgv.multiviewva.model.DataModel;
import java.util.prefs.Preferences;

/**
 * Functions for dealing with files and HD
 *
 * @author l.shao and modified by mchegini
 */
public class DataLoadUtility {

    /**
     * Define whether application is in test mode or not
     * very importatnt: don't make it true for real application
     * this is just a test attribute, if false the application
     * will be in labelling mode
     */
    public static boolean labellingDemo  = false;

    public static String delimiter = ";";

    /**
     * create and return a SPMModel. SPMModel store all the useful information
     * of a dataset
     *
     * @param file: any file that follows the correct format
     * @return
     */
    public static SPMModel loadData(File file) {

        HashMap<Integer, String> combinations = new HashMap<>();
        //parentpath of the file
        String parentPath = file.getParent();
        //creat a filemodel object
        DataModel model = new DataModel(file.getAbsolutePath(), delimiter, true); //normalize = false/true
        SearchUtility.dataModel = model;
        ArrayList<String> header = model.getDataHeader();
        ArrayList<Pattern> patternList = new ArrayList<>();
        int index = 0;

        for (int x = 0; x < header.size() - 1; x++) {
            for (int y = x + 1; y < header.size(); y++) {
                String axesCombination = header.get(x) + "#" + header.get(y);
                combinations.put(index, axesCombination);
                index++;
            }
        }

        /* Load Cluster Points */
        String filename = file.getName();
        filename = filename.substring(0, filename.indexOf("."));

        ArrayList<Object>[] data = model.getDataSet();

        // -2 dim -> last is for class labels [iris]
        for (int x = 0; x < data.length - 1 - model.getNrOfCatDims(); x++) {
            for (int y = x + 1; y < data.length - model.getNrOfCatDims(); y++) {
                int id = 0;
                ArrayList<Point2D> points = new ArrayList<>();
                if (data[x].size() == data[y].size()) {
                    for (int i = 0; i < data[x].size(); i++) {
                        try {
                            if (data[x].get(i) != null && data[y].get(i) != null) {
                                Point2D p = new Point2D((double) data[x].get(i), (double) data[y].get(i), i);
                                points.add(p);
                            }
                        } catch (Exception e) {
                        }

                    }
                } else {
                    System.out.println("bad!");
                }
                Pattern patt = new Pattern(String.valueOf(id), points, model.getHeaderAt(x) + "#" + model.getHeaderAt(y));
                patternList.add(patt);
            }
        }

        //load Pacor loading and init
        return new SPMModel(combinations, parentPath, model, header, patternList, filename);
    }

}
