/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.gui.charts;

import at.tugraz.cgv.multiviewva.model.Dimension;

/**
 * It is a interface to manage all brushing regarded the dimensions
 * @author chegini
 */
public interface DimBrushable {
    
    /**
     * brush all the dimensions (highlight them)
     */
    public void brushAllDim();
    
    /**
     * highlight or not a specific dimension
     * @param dimensions to be highlighted
     * @param highligh if true then the dimension should be brushed
     */
    public void brushDimension(Dimension dimensions, boolean highligh);
    
    /**
     * clean all the dimensions
     */
    public void unbrushAll();
}
