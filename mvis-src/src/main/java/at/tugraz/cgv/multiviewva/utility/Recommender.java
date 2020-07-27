/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.OptionalDouble;
import java.util.stream.IntStream;
import javafx.scene.shape.Shape;

/**
 *
 * @author mchegini
 */
public class Recommender {

    /**
     * all the boxes that are selected by the user
     */
    public static ArrayList<Shape> recommendedRec = new ArrayList<>();

    /**
     * all the rectangles in the result
     */
    public static ArrayList<Shape> searchResult = new ArrayList<>();

    /**
     * different grid sizes for shape based search
     */
    public static int[] gridCoarse = {2, 3, 4, 5};

    /**
     * Coefficient of Model distance
     */
    public static double modelCo = 3.0;

    /**
     * Coefficients for model and shape descriptors
     */
    public static double[] coeff = {0, 0.2, 0.4, 0.5, 0.6, 0.8, 1};

    /**
     *
     */
    public static int alphaIndex = 0;

    /**
     * Coefficients for model descriptors (alpha)
     */
    public static double[] alpha = {1, 1.5, 2.3, 3.4};

    /**
     * current index of suitable regressionModel 0 for degree 1 1 for degree 3 2
     * for degree 4
     */
    public static int regressionDegree = 1;

    /**
     * index of grid size based on gridCoarse
     */
    public static int gridSizeIndex = 1;

    /**
     * adjust variables for a better search result, based on selected search
     * results
     */
    public static int indexOfSimilarityArray = 1 + Recommender.alpha.length * 3 + (Recommender.alpha.length * Recommender.coeff.length) * 1 + (Recommender.alpha.length * Recommender.coeff.length * 3) * 2;

    public static void adjustVars() {
        if (Recommender.recommendedRec.size() > 0) {
            //for purity
            double purity_1_min = Double.MAX_VALUE, purity_2_min = Double.MAX_VALUE;
            double maxDistance = 0.0;
            for (Shape rec : Recommender.recommendedRec) {
                purity_1_min = ((purity_1_min > (double) rec.getProperties().get("purity_1")) ? (double) rec.getProperties().get("purity_1") : purity_1_min);
                purity_2_min = ((purity_2_min > (double) rec.getProperties().get("purity_2")) ? (double) rec.getProperties().get("purity_2") : purity_2_min);
            }
            SearchUtility.purity_1 = purity_1_min - 0.2;
            SearchUtility.purity_2 = purity_2_min - 0.2;

            //other vars
            //3 for number of different models 
            ArrayList<Coordinates> scores = new ArrayList<>();
            int size = 3 * Recommender.coeff.length * Recommender.gridCoarse.length * Recommender.alpha.length;
            IntStream.range(0, size).forEach(i -> {
                scores.add(new Coordinates(i, scoreOfVars(i)));
            });

            int minIndex = Collections.min(scores).getIndex();
            int shape = minIndex / (3 * Recommender.coeff.length * Recommender.alpha.length);
            int model = (minIndex / (Recommender.coeff.length * Recommender.alpha.length)) % 3;
            int coeffTemp = (minIndex / Recommender.alpha.length) % Recommender.coeff.length;
            int coeffAlpha = minIndex % (Recommender.alpha.length);
            System.out.println(minIndex + " " + shape + " " + model + " " + coeffTemp + " " + coeffAlpha);
            //set vars
            indexOfSimilarityArray = minIndex;
            regressionDegree = model;
            gridSizeIndex = shape;
            alphaIndex = coeffAlpha;
            SearchUtility.shapeWeight = Recommender.coeff[coeffTemp];
            for (Shape rec : Recommender.recommendedRec) {
                maxDistance += ((ArrayList<Double>) rec.getProperties().get("similarityArray")).get(minIndex);
            }
            maxDistance = (double) maxDistance / recommendedRec.size();
            SearchUtility.minSimilarity = maxDistance + 20;
        }

        clearRecommender();
    }

    /**
     * based on the selected patterns and a specific index (which indicates a
     * set of variables) a score is calculated. This score should be min to be
     * considered later for the best set of variables.
     *
     * @param index index in searchResult
     * @return score of ranking, which is sum of standing of selected rectangles
     */
    public static int scoreOfVars(int index) {
        int score = 0;
        int tempRank = 0;
        double tempSelectedSim = 0;
        for (Shape selected : recommendedRec) {
            tempSelectedSim = ((ArrayList<Double>) selected.getProperties().get("similarityArray")).get(index);
            for (Shape result : searchResult) {
                if (tempSelectedSim > ((ArrayList<Double>) result.getProperties().get("similarityArray")).get(index)) {
                    tempRank++;
                }
            }
            score += tempRank;
            tempRank = 0;
            tempSelectedSim = 0;
        }
        return score;
    }

    public static void clearRecommender() {
        recommendedRec.clear();
        searchResult.clear();
    }

    public static void resetVars() {

    }
}

class Coordinates implements Comparable<Coordinates> {

    private int index;
    private int value;

    public Coordinates(int index, int value) {
        this.index = index;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(Coordinates o) {
        return (this.getValue() < o.getValue()) ? -1 : ((this.getValue() == o.getValue()) ? 0 : 1);
    }
}
