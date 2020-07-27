/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.gui.charts;

import javafx.collections.ObservableList;
import at.tugraz.cgv.multiviewva.model.Item;

/**
 * All charts with brushing ability should implement this interface. Later with
 * a list of {@link Brushable}, it is possible to call functions and do brushing across
 * multiple views
 *
 * @author mchegini
 */
public interface Brushable {

    /**
     * redraw everything according to selected items
     */
    public void redrawSelected();

    /**
     * draw all the items, usually with a gray neutral color in background for
     * performance
     */
    public void drawAllBackground();
    
    /**
     * redraw all plot based on new categories colors
     */
    public void redrawCategories();
    
    /**
     * highlight or unhighlight one single item based on on hover
     * @param highlight to highlight or unhighlight 
     * @param item which item to highlight
     */
    public void highLightRecordTemp(boolean highlight, Item item);

}
