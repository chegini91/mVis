/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.model;

import java.util.ArrayList;

/**
 *
 * @author Shao
 */
public class Pattern {
    
    private String id;
    private String xDimension;
    private String yDimension;
    private ArrayList<Point2D> points = new ArrayList<>();
    
    
    
    public Pattern(String id, ArrayList<Point2D> points){
        this.id = id;
        this.points = points;
    }
    
    public Pattern(String id, ArrayList<Point2D> points, String dimCombination){
        this.id = id;
        this.points = points;
        
        this.xDimension = dimCombination.substring(0, dimCombination.indexOf("#"));         //bug!!!:  = raster:pgb-derived.Temperature
        this.yDimension = dimCombination.substring(dimCombination.indexOf("#")+1);
    }
    
    public String getId() {
        return id;
    }

    public String getxDimension() {
        return xDimension;
    }
    
    public String getyDimension() {
        return yDimension;
    }

    public ArrayList<Point2D> getPoints() {
        return points;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setxDimension(String xDimension) {
        this.xDimension = xDimension;
    }

    public void setyDimension(String yDimension) {
        this.yDimension = yDimension;
    }

    public void setPoints(ArrayList<Point2D> points) {
        this.points = points;
    }
    
}

