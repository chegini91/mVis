/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.gui.javaFXextension;

import java.util.ArrayList;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.shape.Path;

/**
 * @author mchegini
 */
public class ExtendedPath extends Path {

    ArrayList<Point2D> points = new ArrayList<>();
    ArrayList<Point2D> pointsScreen = new ArrayList<>();

    public ArrayList<Point2D> getPoints() {
        return points;
    }

    public ArrayList<Point2D> getPointsScreen() {
        return pointsScreen;
    }

    private double MaxHeight = Double.MIN_VALUE;
    private double MinHeight = Double.MAX_VALUE;
    private double MaxWidth = Double.MIN_VALUE;
    private double MinWidth = Double.MAX_VALUE;

    public void clearPathPoints() {
        MaxHeight = Double.MIN_VALUE;
        MinHeight = Double.MAX_VALUE;
        MaxWidth = Double.MIN_VALUE;
        MinWidth = Double.MAX_VALUE;
        points = new ArrayList<>();
        pointsScreen = new ArrayList<>();
    }

    public void printPath() {
        for (int i = 0; i < this.points.size(); i++) {
            System.out.println(this.points.get(i).getX() + "  " + this.points.get(i).getY());
        }
    }

    public void addPoint(Point2D point, Point2D screen) {
        //smoothing the rectangle
        points.add(point);
        boolean temp = true;

        if (pointsScreen.size() > 0) {
            while (temp) {
                if (!intermediatePoint(pointsScreen.get(pointsScreen.size() - 1), screen)) {
                    temp = false;
                }
            }
        }

        pointsScreen.add(screen);
        if (point.getX() > MaxWidth) {
            MaxWidth = point.getX();
        }
        if (point.getX() < MinWidth) {
            MinWidth = point.getX();
        }
        if (point.getY() > MaxHeight) {
            MaxHeight = point.getY();
        }
        if (point.getY() < MinHeight) {
            MinHeight = point.getY();
        }
    }

    /**
     * This function is used to smooth the path, since the path sometimes can be
     * really coarse if the user select the points quickly, this function can be
     * used to smooth it
     *
     * @param a
     * @param b
     * @return whether a new point is added or not
     */
    public boolean intermediatePoint(Point2D a, Point2D b) {
        double d = a.distance(b);
        if (d > 5) {
            //creat an intermediate point
            Point2D p = new Point2D((2 * (b.getX() - a.getX()) / (d)) + a.getX(), (2 * (b.getY() - a.getY()) / (d)) + a.getY());
            pointsScreen.add(p);
            return true;
        }
        return false;
    }

    public double getMaxHeight() {
        return MaxHeight;
    }

    public double getMinHeight() {
        return MinHeight;
    }

    public double getMaxWidth() {
        return MaxWidth;
    }

    public double getMinWidth() {
        return MinWidth;
    }

    public void setMaxHeight(double MaxHeight) {
        this.MaxHeight = MaxHeight;
    }

    public void setMinHeight(double MinHeight) {
        this.MinHeight = MinHeight;
    }

    public void setMaxWidth(double MaxWidth) {
        this.MaxWidth = MaxWidth;
    }

    public void setMinWidth(double MinWidth) {
        this.MinWidth = MinWidth;
    }

    /**
     * This function get a point and return a boolean to indicate if the point
     * is inside the path or not
     *
     * @param p
     * @return
     */
    public boolean pointInsidePath(Point2D p) {
        boolean above = false;
        boolean below = false;
        boolean right = false;
        boolean left = false;
        Rectangle2D primaryScreenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
        for (Point2D point2D : pointsScreen) {
            if (point2D.getY() < p.getY() && !above) {
                if (ExtendedPath.approxiTheSame(point2D.getX(), p.getX(), primaryScreenBounds.getHeight() / 200)) {
                    above = true;
                }
            } else if (point2D.getY() > p.getY() && !below) {
                if (ExtendedPath.approxiTheSame(point2D.getX(), p.getX(), primaryScreenBounds.getHeight() / 200)) {
                    below = true;
                }
            }

            if (point2D.getX() < p.getX() && !left) {
                if (ExtendedPath.approxiTheSame(point2D.getY(), p.getY(), primaryScreenBounds.getHeight() / 200)) {
                    left = true;
                }

            } else if (point2D.getX() > p.getX() && !right) {
                if (ExtendedPath.approxiTheSame(point2D.getY(), p.getY(), primaryScreenBounds.getHeight() / 200)) {
                    right = true;
                }
            }
        }
        return left && right && above && below;
//        Rectangle2D primaryScreenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
//        //if p is outside the y bound
//        ArrayList<Point2D> addedPointsA = new ArrayList<>();
//        ArrayList<Point2D> addedPointsB = new ArrayList<>();
//        if (p.getY() > this.getMaxHeight() || p.getY() < this.getMinHeight()) {
//            return false;
//        }
//        //if p is outside x bound
//        if (p.getX() > this.getMaxWidth() || p.getX() < this.getMinWidth()) {
//            return false;
//        }
//        boolean temp = true;
//        boolean temp2 = true;
//        //check if the point is inside using a line. from a point we draw a line
//        //if number of intersections is even then the point is outside
//        for (Point2D point2D : points) {
//            if (point2D.getY() < p.getY()) {
//                if (ExtendedPath.approxiTheSame(point2D.getX(), p.getX(), primaryScreenBounds.getHeight() / 100)) {
//
//                    for (int i = 0; i < addedPointsA.size(); i++) {
//                        if (addedPointsA.get(i).distance(point2D) < primaryScreenBounds.getHeight() / 100) {
//                            temp = false;
//                        }
//                    }
//                    if (temp) {
//                        addedPointsA.add(point2D);
//                        System.out.println(point2D);
//                    }
//                    temp = true;
//                }
//            } else if (point2D.getY() > p.getY()) {
//                if (ExtendedPath.approxiTheSame(point2D.getX(), p.getX(), 20)) {
//
//                    for (int i = 0; i < addedPointsB.size(); i++) {
//                        if (addedPointsB.get(i).distance(point2D) < 50) {
//                            temp2 = false;
//                        }
//                    }
//                    if (temp2) {
//                        addedPointsB.add(point2D);
//                        System.out.println(point2D);
//                    }
//                    temp2 = true;
//                }
//            }
//        }
//        System.out.println(addedPointsA.size());
//        System.out.println(addedPointsB.size());
//        if (addedPointsA.size() % 2 == 1 || addedPointsB.size() % 2 == 1) {
//            return true;
//        } else {
//            return false;
//        }
    }

    public static boolean approxiTheSame(double x, double y, double error) {
        return x + error > y && x - error < y;
    }

}
