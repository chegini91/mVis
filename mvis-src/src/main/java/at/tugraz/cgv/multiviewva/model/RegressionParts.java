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
public class RegressionParts {
    
    int nrOfParts;
    ArrayList<Point2D> partsX = new ArrayList<Point2D>();
    ArrayList<Double> fit = new ArrayList<Double>();
    
    public RegressionParts(){
        
    }
    
    public void addPart(Point2D pX,double fit){
        this.partsX.add(pX);
        this.fit.add(fit);
        nrOfParts++;
    }
    
    public ArrayList<Point2D> getParts(){
        return this.partsX;
    }
    
    public ArrayList<Double> getFit(){
        return this.fit;
    }
    
    public int getNrOfParts(){
        return nrOfParts;
    }
}
