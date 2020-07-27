/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.model;

/**
 *
 * @author chegini
 */
public class Dimension {

    private String name;
    private boolean activeML = true;
    private Double[] minMax;

    public Dimension(String name) {
        this.minMax = new Double[]{Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isActiveML() {
        return activeML;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActiveML(boolean activeML) {
        this.activeML = activeML;
    }

    @Override
    public boolean equals(Object dim) {
        if (dim.getClass().equals(this.getClass())) {
            return this.getName().equals(((Dimension) dim).getName());
        } else {
            return false;
        }
    }

    public Double[] getMinMax() {
        return minMax;
    }

    public void setMinMax(Double[] minMax) {
        this.minMax = minMax;
    }

}
