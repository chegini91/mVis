/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.model.search;

/**
 *
 * @author mchegini
 */
public class FeatureVector implements Comparable<FeatureVector> {

    public int getNumOfRowLine() {
        return numOfRowLine;
    }

    public int getNumOfColLine() {
        return numOfColLine;
    }

    public double[][] getFv() {
        return fv;
    }

    public FeatureVector(int numOfRowLine, int numOfColLine) {
        fv = new double[numOfRowLine + 1][numOfColLine + 1];
        this.numOfRowLine = numOfRowLine;
        this.numOfColLine = numOfColLine;
        for (int i = 0; i < numOfRowLine; i++) {
            for (int j = 0; j < numOfColLine; j++) {
                fv[i][j] = 0;
            }
        }
    }

    public void setNumOfRowLine(int numOfRowLine) {
        this.numOfRowLine = numOfRowLine;
    }

    public void setNumOfColLine(int numOfColLine) {
        this.numOfColLine = numOfColLine;
    }

    public void setFv(double[][] fv) {
        this.fv = fv;
    }

    private int numOfRowLine;
    private int numOfColLine;
    double fv[][];

    /**
     * return a value from 0 to 100 that shows the similarity of two feature
     * vectors
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(FeatureVector o) {
        Double result = 0.0;
        for (int i = 0; i < numOfRowLine + 1; i++) {
            for (int j = 0; j < numOfColLine + 1; j++) {
                result = result + Math.abs(this.fv[i][j] - o.fv[i][j]);
            }
        }
        //normalize result to 0-100
        result = 100 * ((result) / (2));
        return result.intValue();

    }

    @Override
    public String toString() {
        String result = "FeatureVector: \n";
        for (int i = 0; i < numOfRowLine + 1; i++) {
            for (int j = 0; j < numOfColLine + 1; j++) {
                result = result + fv[i][j] + " ";
            }
            result = result + "\n";
        }
        return result;
    }

    /**
     * convert 2D original feature vector into 1D double array
     *
     * @return
     */
    public double[] get1DFeatureVector() {
        double result[] = new double[fv.length * fv.length];

        for (int i = 0; i < fv.length; i++) {
            for (int j = 0; j < fv.length; j++) {
                result[(i * fv.length) + j] = fv[i][j];
            }
        }
        return result;
    }
}
