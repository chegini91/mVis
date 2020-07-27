/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.model;

/**
 *
 * @author mchegini
 */
public class Point2D {

    private double x = -1;
    private double y = -1;
    private int index = -1;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getIndex() {
        return index;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Point2D(double x, double y, int index) {
        this.x = x;
        this.y = y;
        this.index = index;
    }
}
