/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.utility.math;

import at.tugraz.cgv.multiviewva.utility.PolynomialRegression;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import org.apache.commons.lang3.ArrayUtils;

/**
 * provides essential functions to calculate regression models (using R)
 *
 * @author mohammad chegini (m.chegini@cgv.tugraz.at)
 */
public class Regression {


    /**
     * Input is set of 2d points and output is a linear regression
     *
     * @param points
     * @param inverted
     * @return
     */
    public static double[] calculateLinearRegression(ArrayList<Point2D> points, boolean inverted) {
        double intercept = 0.0d;
        double slope = 0.0d;
        try {
            PolynomialRegression regression = new PolynomialRegression(points, 1);
            intercept = regression.beta(0);
            slope = regression.beta(1);
            System.err.println(intercept + " " + slope);
        } catch (Exception e) {
            System.out.println("e");
        }
        return new double[]{slope, intercept};
    }

    /**
     * This function get set of points and calculate the quad regression line
     *
     * @param points
     * @param inverted
     * @return the parameters of quad regression model (2nd degree). Last cube
     * in array is constant value of model
     */
    public static double[] calculateQuadRegression(ArrayList<Point2D> points, boolean inverted) {
        PolynomialRegression regression = new PolynomialRegression(points, 2);

        return new double[]{regression.beta(2), regression.beta(1), regression.beta(0)};

    }

    public static double[] calculateCubicRegression(ArrayList<Point2D> points, boolean inverted) {

        PolynomialRegression regression = new PolynomialRegression(points, 3);

        return new double[]{regression.beta(3), regression.beta(2), regression.beta(1), regression.beta(0)};
    }

    public static double[] calculate4DegreeRegression(ArrayList<Point2D> points, boolean inverted) {

        PolynomialRegression regression = new PolynomialRegression(points, 4);

        return new double[]{regression.beta(4), regression.beta(3), regression.beta(2), regression.beta(1), regression.beta(0)};
    }

}
