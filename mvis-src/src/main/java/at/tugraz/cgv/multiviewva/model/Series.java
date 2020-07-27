package at.tugraz.cgv.multiviewva.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * Series which contains list of items
 */
public class Series {

    /**
     * name of the series
     */
    private String name = "?";

    /**
     * The color of the series (BLACK default)
     */
    private Color color = Color.BLACK;

    /**
     * The opacity for the series (0.2 default)
     */
    private double opacity = 0.2;

    /**
     * List of items in the series
     */
    private ObservableList<Item> items = FXCollections.observableArrayList();

    public Series(String name, List<Item> items) {
        this(items);
        this.name = name;

    }

    public Series(List<Item> items) {
        this.items.addAll(items);
        for (Item item : this.items) {
            item.setSeries(this);
        }
    }

    public Series(String name, List<Item> items, Color color, double opacity) {
        this(name, items);
        this.color = color;
        this.opacity = opacity;

    }

    public Series(List<Item> items, Color color, double opacity) {
        this(items);
        this.color = color;
        this.opacity = opacity;
    }

    public int getItemIndex(Item item) {
        return items.indexOf(item);
    }

    public Item getItem(int index) {
        return items.get(index);
    }

    public String getName() {
        return name;
    }

    public ObservableList<Item> getItems() {
        return items;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public void setItems(ObservableList<Item> items) {
//        this.items = items;
//    }
    public int getSeriesSize() {
        return items.size();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getOpacity() {
        return opacity;
    }

    public void setOpacity(double opacity) {
        this.opacity = opacity;
    }

}
