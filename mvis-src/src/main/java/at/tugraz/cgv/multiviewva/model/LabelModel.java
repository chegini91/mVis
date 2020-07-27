/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

/**
 *
 * @author mohammad
 */
public class LabelModel {

    /**
     * name tag of the label
     */
    private String name;

    /**
     * color of the label Hex
     */
    private String color;

    /**
     * if the thing is visible
     */
    private boolean visible = true;

    /**
     * List of items in the label
     */
    private ObservableSet<Integer> items = FXCollections.observableSet(new HashSet<Integer>());

    /**
     * all the dimensions that the cluster interacted with
     */
    private final Set<String> dimeInteraction = new HashSet();

    public LabelModel(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public ObservableSet<Integer> getItems() {
        return items;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Set<String> getDimeInteraction() {
        return dimeInteraction;
    }

    public void setItems(ObservableSet<Integer> items) {
        this.items = items;
    }

    public void printDimenstionsInt() {
        dimeInteraction.forEach(dim -> {
            System.out.println(dim);
        });
    }

}
