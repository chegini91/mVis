/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.model;

import java.awt.geom.Point2D;

/**
 *
 * @author Lin Shao
 */
public class CubicPoint {
    
    boolean insideSelection;
    Point2D point;
    double distance = -1.0;

    public CubicPoint(Point2D p, boolean insideSelection){
        this.point = p;
        this.insideSelection = insideSelection;
    }

    public double getDistance()
    {
        return distance;
    }

    public void setDistance(double distance)
    {
        this.distance = distance;
    }
    
    public boolean getColorMode()
    {
        return insideSelection;
    }

    public void setColorMode(boolean insideSelection)
    {
        this.insideSelection = insideSelection;
    }

    public Point2D getPoint()
    {
        return point;
    }

    public void setPoint(Point2D point)
    {
        this.point = point;
    }
    
    public double getX(){
        return point.getX();
    }
    
    public double getY(){
        return point.getY();
    }
}
