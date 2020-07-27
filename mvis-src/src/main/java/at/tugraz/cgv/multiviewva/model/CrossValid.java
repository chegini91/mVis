/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.model;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 *
 * @author Lin Shao
 */
public class CrossValid {
    
    ArrayList<Point2D>[] splittedData;
    double errorX;
    double errorY;
    ArrayList<Double> coefficients;
    
    public CrossValid(ArrayList<Point2D>[] splittedData, double errorX, double errorY, ArrayList<Double> coefficients){
        this.splittedData = splittedData;
        this.errorX = errorX;
        this.errorY = errorY;
        this.coefficients = coefficients;
    }

    public ArrayList<Point2D>[] getSplittedData()
    {
        return splittedData;
    }

    public double getErrorX()
    {
        return errorX;
    }

    public double getErrorY()
    {
        return errorY;
    }

    public ArrayList<Double> getCoefficients()
    {
        return coefficients;
    }
    
}
