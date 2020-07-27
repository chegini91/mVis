/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.model.search;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import at.tugraz.cgv.multiviewva.utility.SearchUtility;
import at.tugraz.cgv.multiviewva.utility.math.Regression;

/**
 *
 * @author mchegini
 */
public class Box {

    /**
     * 4nd regression model of the box
     */
    private double[] fourDegreeRegressionModel = new double[5];

    /**
     * cubic regression model of the box
     */
    private double[] CubicRegressionModel = new double[4];
    /**
     * quad regression model of the box
     */
    private double[] quadRegressionModel = new double[3];
    /**
     * linear regression model of the box
     */
    private double[] linearRegressionModel = new double[2];

    /**
     * return plot points in XYChart format
     *
     * @return
     */
    public ObservableList<XYChart.Data<Number, Number>> getPoints() {
        return points;
    }

    public ArrayList<Point2D> getPoints2D() {
        ArrayList<Point2D> ps2D = new ArrayList<>();

        points.stream().forEach(p -> {
            ps2D.add(new Point2D.Double(p.getXValue().doubleValue(), p.getYValue().doubleValue()));
        });
        return ps2D;
    }

    /**
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

    private double minX = Double.MAX_VALUE;
    private double maxX = Double.MIN_VALUE;
    private double minY = Double.MAX_VALUE;
    private double maxY = Double.MIN_VALUE;

    private ObservableList<XYChart.Data<Number, Number>> realPoints;

    private Set<Integer> indexSet = new HashSet<Integer>();

    public double getMinX() {
        return minX;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMaxY() {
        return maxY;
    }

    public double[] getFourDegreeRegressionModel() {
        return fourDegreeRegressionModel;
    }

    public double[] getCubicRegressionModel() {
        return CubicRegressionModel;
    }

    public double[] getQuadRegressionModel() {
        return quadRegressionModel;
    }

    public double[] getLinearRegressionModel() {
        return linearRegressionModel;
    }

    ObservableList<XYChart.Data<Number, Number>> points = FXCollections.observableArrayList();
    private int maxRowLine = 1;
    private int maxColLine = 1;
    private final FeatureVector[][] fvs;

    /**
     * a quantile feature vector
     */
    private final FeatureVector quantileFV;

    public Box(int maxRowLine, int maxColLine, ObservableList<XYChart.Data<Number, Number>> points) {

        this.maxColLine = maxColLine;
        this.maxRowLine = maxRowLine;
        realPoints = points;

        points.stream().forEach((point) -> {
            this.points.add(new XYChart.Data<>(point.getXValue(), point.getYValue()));
            indexSet.add((int) point.getExtraValue());
        });
        fvs = new FeatureVector[maxRowLine][maxColLine];

        quantileFV = new FeatureVector(maxRowLine - 1, maxColLine - 1);

        normalizePoints();
        calculateAllFVVectors(maxRowLine, maxColLine);
//        calculateQuantileFV();
        //calculate regression models
        fourDegreeRegressionModel = Regression.calculate4DegreeRegression(this.getPoints2D(), false);
        CubicRegressionModel = Regression.calculateCubicRegression(this.getPoints2D(), false);
        quadRegressionModel = Regression.calculateQuadRegression(this.getPoints2D(), false);
        linearRegressionModel = Regression.calculateLinearRegression(this.getPoints2D(), false);

    }

    public Box(ObservableList<XYChart.Data<Number, Number>> points, int regDegree) {

        realPoints = points;

        points.stream().forEach((point) -> {
            this.points.add(new XYChart.Data<>(point.getXValue(), point.getYValue()));
        });
        fvs = null;

        quantileFV = null;

        normalizePoints();

//        fourDegreeRegressionModel = Regression.calculate4DegreeRegression(this.getPoints2D(), false);
//        CubicRegressionModel = Regression.calculateCubicRegression(this.getPoints2D(), false);
//        quadRegressionModel = Regression.calculateQuadRegression(this.getPoints2D(), false);
        linearRegressionModel = Regression.calculateLinearRegression(this.getPoints2D(), false);

    }

//    /**
//     * calculate all the descriptors for the box
//     */
//    public void calculateDescriptors() {
//        calculateFVs();
//        calculateModel();
//    }
//
//    /**
//     * calculate the feature vectors of the box
//     */
//    public void calculateFVs() {
//
//    }
//
//    /**
//     * calculates the regression model (4th) of the box
//     */
//    public void calculateModel() {
//        
//    }
    /**
     * calculate all feature vectors for the box
     *
     * @param maxRow
     * @param maxCol
     */
    public void calculateAllFVVectors(int maxRow, int maxCol) {
        for (int i = 1; i < maxRow; i++) {
            for (int j = 1; j < maxCol; j++) {
                fvs[i][j] = calculateFVValue(i, j);
            }
        }
    }

    /**
     * calculate the quantile feature vector and update the field
     */
    public void calculateQuantileFV() {
        int lowQuantile = 10;
        int hightQuantile = 90;
        int dimension = maxColLine - 1;
        int size = realPoints.size();
        double[] xData = new double[size];
        double[] yData = new double[size];

        for (int i = 0; i < size; i++) {
            xData[i] = realPoints.get(i).getXValue().doubleValue();
            yData[i] = realPoints.get(i).getYValue().doubleValue();
        }
        double[] sortXData = new double[size];
        double[] sortYData = new double[size];
        System.arraycopy(xData, 0, sortXData, 0, size);
        System.arraycopy(yData, 0, sortYData, 0, size);
        Arrays.sort(sortXData);
        Arrays.sort(sortYData);

        double xRange = sortXData[(int) Math.round(size * hightQuantile / 100)]
                - sortXData[(int) Math.round(size * lowQuantile / 100)];
        double yRange = sortYData[(int) Math.round(size * hightQuantile / 100)]
                - sortYData[(int) Math.round(size * lowQuantile / 100)];
        double recRange = xRange > yRange ? xRange : yRange;

        double recMinX, recMinY;
        double xCenter = sortXData[(int) Math.round(size * lowQuantile / 100)] + (double) (xRange / 2);
        double yCenter = sortYData[(int) Math.round(size * lowQuantile / 100)] + (double) (yRange / 2);

        if (recRange == xRange) {
            recMinX = sortXData[(int) Math.round(size * lowQuantile / 100)];
            recMinY = yCenter - (double) (recRange / 2);
        } else {
            recMinY = sortYData[(int) Math.round(size * lowQuantile / 100)];
            recMinX = xCenter - (double) (recRange / 2);
        }

        double[] xSteps = new double[dimension];
        double[] ySteps = new double[dimension];
        double stepSize = (double) (recRange / dimension);
        xSteps[0] = recMinX + stepSize;
        ySteps[0] = recMinY + stepSize;

        for (int i = 1; i < dimension; i++) {
            xSteps[i] = stepSize + xSteps[i - 1];
        }

        for (int i = 1; i < dimension; i++) {
            ySteps[i] = stepSize + ySteps[i - 1];
        }
        for (XYChart.Data<Number, Number> realPoint : realPoints) {
            int x = -1;
            int y = -1;
            for (int j = 0; j < dimension; j++) {
                if (realPoint.getXValue().doubleValue() < xSteps[j] + 0.00001) {
                    x = j;
                    break;
                }
            }
            for (int j = 0; j < dimension; j++) {
                if (realPoint.getYValue().doubleValue() < ySteps[j] + 0.00001) {
                    y = j;
                    break;
                }
            }
            if (x == -1) {
                x = dimension;
            }
            if (y == -1) {
                y = dimension;
            }
            quantileFV.fv[x][y] += 1;
        }

        //normalize fv
        for (int i = 0; i < quantileFV.getNumOfRowLine() + 1; i++) {
            for (int j = 0; j < quantileFV.getNumOfColLine() + 1; j++) {
                quantileFV.fv[i][j] = quantileFV.fv[i][j] / points.size();
            }

        }
    }

    /**
     * normalize all points to 0-1
     */
    public void normalizePoints() {
        for (XYChart.Data<Number, Number> point : points) {
            if (point.getXValue().doubleValue() > maxX) {
                maxX = point.getXValue().doubleValue();
            }
            if (point.getXValue().doubleValue() < minX) {
                minX = point.getXValue().doubleValue();
            }

            if (point.getYValue().doubleValue() > maxY) {
                maxY = point.getYValue().doubleValue();
            }

            if (point.getYValue().doubleValue() < minY) {
                minY = point.getYValue().doubleValue();
            }
        }

        //to avoid divide by 0 problem
        if (minX == maxX) {
            minX = minX - 0.1;
            maxX = maxX + 0.1;
        }
        if (minY == maxY) {
            maxY = maxY + 0.1;
            minY = minY - 0.1;
        }

        for (int i = 0; i < points.size(); i++) {
            points.get(i).setXValue((double) ((points.get(i).getXValue().doubleValue() - minX) / (maxX - minX)));
            points.get(i).setYValue((double) ((points.get(i).getYValue().doubleValue() - minY) / (maxY - minY)));
        }
    }

    /**
     * normalize all points to 0-1
     *
     * @param points
     * @return
     */
    public static ObservableList<XYChart.Data<Number, Number>> normalizePoints(ObservableList<XYChart.Data<Number, Number>> points) {
        ObservableList<XYChart.Data<Number, Number>> result = FXCollections.observableArrayList();
        double minX = 0, maxX = 0, minY = 0, maxY = 0;
        for (XYChart.Data<Number, Number> point : points) {
            if (point.getXValue().doubleValue() > maxX) {
                maxX = point.getXValue().doubleValue();
            }
            if (point.getXValue().doubleValue() < minX) {
                minX = point.getXValue().doubleValue();
            }

            if (point.getYValue().doubleValue() > maxY) {
                maxY = point.getYValue().doubleValue();
            }

            if (point.getYValue().doubleValue() < minY) {
                minY = point.getYValue().doubleValue();
            }
        }

        //to avoid divide by 0 problem
        if (minX == maxX) {
            minX = minX - 0.1;
            maxX = maxX + 0.1;
        }
        if (minY == maxY) {
            maxY = maxY + 0.1;
            minY = minY - 0.1;
        }

        for (XYChart.Data<Number, Number> point : points) {
            result.add(new XYChart.Data<>((double) ((point.getXValue().doubleValue() - minX) / (maxX - minX)),
                    (double) ((point.getYValue().doubleValue() - minY) / (maxY - minY))));
        }
        return result;
    }

    public FeatureVector calculateFVValue(int rowNum, int colNum) {
        FeatureVector fv = new FeatureVector(rowNum, colNum);
        int x = -1;
        int y = -1;
        for (XYChart.Data<Number, Number> p : points) {
            for (int i = 0; i < rowNum + 1; i++) {
                if (p.getXValue().doubleValue() < SearchUtility.searchInterval[rowNum][i] + 0.0001) {
                    x = i;
                    break;
                }
            }
            for (int i = 0; i < colNum + 1; i++) {
                if (p.getYValue().doubleValue() < SearchUtility.searchInterval[colNum][i] + 0.0001) {
                    y = i;
                    break;
                }
            }
            //plus the number of fv
            try {
                if (x == -1) {
                    x = rowNum;
                }
                if (y == -1) {
                    y = colNum;
                }
                fv.fv[x][y] = fv.fv[x][y] + 1;
            } catch (Exception e) {

            }

        }
        for (int i = 0; i < fv.getNumOfRowLine() + 1; i++) {
            for (int j = 0; j < fv.getNumOfColLine() + 1; j++) {
                fv.fv[i][j] = fv.fv[i][j] / points.size();
            }

        }
        return fv;
    }

    /**
     * return a value between 0 and 100 that shows the similarity of two boxes
     *
     * @param o
     * @return
     */
    public int compareToShape(Box o, int colNum, int rowNum) {
        return fvs[rowNum - 1][colNum - 1].compareTo(o.fvs[rowNum - 1][colNum - 1]);
    }

    public ArrayList<Integer> compareToShapeAll(Box o, int[] gridNum) {
//        Set<Integer> temp = new HashSet<>(this.getIndexSet());
//        temp.retainAll(o.getIndexSet());
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < gridNum.length; i++) {
            result.add(fvs[gridNum[i] - 1][gridNum[i] - 1].compareTo(o.fvs[gridNum[i] - 1][gridNum[i] - 1]));
        }
       
        return result;
    }

    /**
     *
     * @param i
     * @param j
     * @return just one feature vector
     */
    public double[] getOne1DFV(int i, int j) {
        return fvs[i][j].get1DFeatureVector();
    }

    /**
     *
     *
     */
    public double[] get1DQuantileFV() {
        return quantileFV.get1DFeatureVector();
    }

    /**
     * This function return a descriptor by sampling the regression model of the
     * points
     *
     * @param degree degree of Regression model
     * @param precision indicates number of points describing the regression
     * model
     * @return a descriptor based on regression model
     */
    public double[] getRegressionDescriptor(int degree, int precision) {

        double[] result = new double[precision];

        double xTemp = 0;
        double yTemp = 0;
        double temp1, temp2, temp3, temp4;

        for (int i = 0; i < precision; i++) {
            xTemp = xTemp + 1 / ((double) precision);
            temp1 = xTemp;
            temp2 = temp1 * xTemp;
            temp3 = temp2 * xTemp;
            temp4 = temp3 * xTemp;

            switch (degree) {
                case 1:
                    yTemp = this.fourDegreeRegressionModel[0] * temp4 + this.fourDegreeRegressionModel[1] * temp3
                            + this.fourDegreeRegressionModel[2] * temp2 + this.fourDegreeRegressionModel[3] * temp1 + this.fourDegreeRegressionModel[4];
                    break;
                case 2:
                    yTemp = this.fourDegreeRegressionModel[0] * temp3
                            + this.fourDegreeRegressionModel[1] * temp2 + this.fourDegreeRegressionModel[2] * temp1 + this.fourDegreeRegressionModel[3];
                    break;
                case 3:
                    yTemp = this.fourDegreeRegressionModel[0] * temp2 + this.fourDegreeRegressionModel[1] * temp1 + this.fourDegreeRegressionModel[2];
                    break;
                case 4:
                    yTemp = this.fourDegreeRegressionModel[0] * temp1 + this.fourDegreeRegressionModel[1];
                    break;
            }

            result[i] = yTemp;
        }
        return result;
    }

    /**
     * compare to the box with regard to regression model (4 degree)
     *
     * @param box
     * @param degree degree of regression model (can be 1, 3 or 4)
     * @return
     */
    public double compareToModel(Box box, int degree) {
        double[] b1 = null;
//        double[] b1 = box.fourDegreeRegressionModel;

        double result = 0.0d;
        double xTemp = 0;
        double y1Temp = 0, y2Temp = 0;
        double temp1, temp2, temp3, temp4 = 0;
        //do it for SearchUtility.num4DCompare number of pionts

        switch (degree) {
            case 1:
                b1 = box.linearRegressionModel;
                break;
            case 3:
                b1 = box.CubicRegressionModel;
                break;
            case 4:
                b1 = box.fourDegreeRegressionModel;
                break;
        }

        for (int i = 0; i < SearchUtility.num4DCompare; i++) {
            xTemp = xTemp + 1 / SearchUtility.num4DCompare;
            temp1 = xTemp;
            temp2 = temp1 * xTemp;
            temp3 = temp2 * xTemp;
            temp4 = temp3 * xTemp;
            switch (degree) {
                case 1:
                    y1Temp = b1[0] * temp1 + b1[1];
                    y2Temp = this.linearRegressionModel[0] * temp1 + this.linearRegressionModel[1];
                    break;
                case 3:
                    y1Temp = b1[0] * temp3 + b1[1] * temp2 + b1[2] * temp1 + b1[3];
                    y2Temp = this.CubicRegressionModel[0] * temp3 + this.CubicRegressionModel[1] * temp2
                            + this.CubicRegressionModel[2] * temp1 + this.CubicRegressionModel[3];
                    break;
                case 4:
                    y1Temp = b1[0] * temp4 + b1[1] * temp3 + b1[2] * temp2 + b1[3] * temp1 + b1[4];
                    y2Temp = this.fourDegreeRegressionModel[0] * temp4 + this.fourDegreeRegressionModel[1] * temp3
                            + this.fourDegreeRegressionModel[2] * temp2 + this.fourDegreeRegressionModel[4] * temp1 + this.fourDegreeRegressionModel[5];
                    break;
            }
            result = result + Math.abs(y1Temp - y2Temp);
        }

//        System.out.println(result);
        return result;
    }

    /**
     * compare to the box with regard to regression model and return all
     * comparisons with different degrees
     *
     * @param box
     * @return regression comparison with degrees 1, 3 and 4
     */
    public ArrayList<Double> compareToModelAll(Box box) {
        double[] b1;
        double[] b3;
        double[] b4;
//        double[] b1 = box.fourDegreeRegressionModel;

        double result1 = 0.0d, result3 = 0.0d, result4 = 0.0d;
        double xTemp = 0;
        double y1Temp = 0, y2Temp = 0;
        double temp1, temp2, temp3, temp4;
        //do it for SearchUtility.num4DCompare number of pionts

        b1 = box.linearRegressionModel;
        b3 = box.CubicRegressionModel;
        b4 = box.fourDegreeRegressionModel;

        for (int i = 0; i < SearchUtility.num4DCompare; i++) {
            xTemp = xTemp + 1 / SearchUtility.num4DCompare;
            temp1 = xTemp;
            temp2 = temp1 * xTemp;
            temp3 = temp2 * xTemp;
            temp4 = temp3 * xTemp;
//                case 1:
            y1Temp = b1[0] * temp1 + b1[1];
            y2Temp = this.linearRegressionModel[0] * temp1 + this.linearRegressionModel[1];
            result1 = result1 + Math.abs(y1Temp - y2Temp);
//                case 3:
            y1Temp = b3[0] * temp3 + b3[1] * temp2 + b3[2] * temp1 + b3[3];
            y2Temp = this.CubicRegressionModel[0] * temp3 + this.CubicRegressionModel[1] * temp2
                    + this.CubicRegressionModel[2] * temp1 + this.CubicRegressionModel[3];
            result3 = result3 + Math.abs(y1Temp - y2Temp);
//                case 4:
            y1Temp = b4[0] * temp4 + b4[1] * temp3 + b4[2] * temp2 + b4[3] * temp1 + b4[4];
            y2Temp = this.fourDegreeRegressionModel[0] * temp4 + this.fourDegreeRegressionModel[1] * temp3
                    + this.fourDegreeRegressionModel[2] * temp2 + this.fourDegreeRegressionModel[3] * temp1 + this.fourDegreeRegressionModel[4];
            result4 = result4 + Math.abs(y1Temp - y2Temp);
        }

//        System.out.println(result);
        ArrayList<Double> arrayResult = new ArrayList<>();
        arrayResult.add(result1);
        arrayResult.add(result3);
        arrayResult.add(result4);
        return arrayResult;
    }

    /**
     * This function check if the ratio of two boxes are in the same range or
     * not
     *
     * @param box
     * @return whether the ratio is in the same rate or not
     */
    public boolean checkPointRatio(Box box) {
        double min, max;
        if (box.getPoints2D().size() > this.getPoints2D().size()) {
            max = box.getPoints2D().size();
            min = this.getPoints2D().size();
        } else {
            max = this.getPoints2D().size();
            min = box.getPoints2D().size();
        }
        return (max / min) <= SearchUtility.maxPointRatio;
    }

    public ObservableList<XYChart.Data<Number, Number>> getRealPoints() {
        return realPoints;
    }

    public Set<Integer> getIndexSet() {
        return indexSet;
    }

}
