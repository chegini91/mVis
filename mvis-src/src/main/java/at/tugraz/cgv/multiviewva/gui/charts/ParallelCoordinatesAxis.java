package at.tugraz.cgv.multiviewva.gui.charts;

import at.tugraz.cgv.multiviewva.gui.charts.ParallelCoordinatesChart;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.controlsfx.control.RangeSlider;

public class ParallelCoordinatesAxis {

    private int id;
    private int axisIndex;
    private NumberAxis axis;
    private boolean inverted = false;
    private String label;
    private HBox labelBox;
    private RangeSlider filterSlider;
    private double filterHigh;
    private double filterLow;
    private Button btnInvert;
    private Button btnLeft;
    private Button btnRight;
    private Label axisLabel;
    private int filterSliderFixation = 0;

    private ParallelCoordinatesChart.DragAndDropLabel labelDragAndDrop;


    /**
     * A basic constructor which allows for initial creation of the object
     *
     * @param axisIndex the initial index and id of the chart
     */
    public ParallelCoordinatesAxis(int axisIndex) {
        this.id = axisIndex;
        this.axisIndex = axisIndex;
    }

    /**
     * supports the basic constructor by setting the values AFTER already
     * creating the axis for now, this is the approach which is used when
     * calling bindAxes in the chart
     *
     * @param axis
     * @param label
     * @param labelBox
     * @param filterSlider
     * @param btnInvert
     * @param btnLeft
     * @param btnRight
     */
    public void initialize(NumberAxis axis, String label, HBox labelBox, RangeSlider filterSlider, Button btnInvert, Button btnLeft, Button btnRight) {
        this.axis = axis;
        this.label = label;
        this.labelBox = labelBox;
        this.filterSlider = filterSlider;

        if (filterSlider != null) {
            filterHigh = filterSlider.getHighValue();
            filterLow = filterSlider.getLowValue();
        }

        this.btnInvert = btnInvert;
        this.btnLeft = btnLeft;
        this.btnRight = btnRight;

        setTickLabelFormatter(filterLow, filterHigh);
    }

    /**
     * Register
     *
     * @param chart            the ParallelCoordinatesChart on which the drag and drop
     *                         listener for this axis will be registered
     * @param labelDragAndDrop the label between the axis which connects the
     *                         DragAndDrop listeners
     */
    public void registerDragAndDropListener(ParallelCoordinatesChart chart, ParallelCoordinatesChart.DragAndDropLabel labelDragAndDrop) {
        this.labelDragAndDrop = labelDragAndDrop;
        /* === set filter slider drag and drops === */
        EventHandler<MouseEvent> handlerDragDetected = event -> {
            /* drag was detected, start a drag-and-drop gesture*/
            /* allow any transfer mode */
            Dragboard db = axis.startDragAndDrop(TransferMode.MOVE);

            /* Put a string on a dragboard */
            ClipboardContent content = new ClipboardContent();
            content.putString(axisIndex + "");
            db.setContent(content);

            ParallelCoordinatesAxis.this.highlightAxis(true);
            // set drag image
            Image dragImage = new Image("/icons/drag.png");
            db.setDragView(dragImage, dragImage.getWidth() / 4.0, -20);
            //  event.consume();
        };
        btnInvert.setOnDragDetected(handlerDragDetected);
        btnLeft.setOnDragDetected(handlerDragDetected);
        btnRight.setOnDragDetected(handlerDragDetected);
        labelBox.setOnDragDetected(handlerDragDetected);

        // this is needed to register which transfer modes are allowed
        EventHandler<DragEvent> handlerDragOver = event -> {
            /* data is dragged over the target */
            /* if it has a string data */
            if (event.getDragboard().hasString()) {
                /* allow for both copying and moving, whatever user chooses */
                event.acceptTransferModes(TransferMode.MOVE);
            }

            // event.consume();
        };

        btnInvert.setOnDragOver(handlerDragOver);
        btnLeft.setOnDragOver(handlerDragOver);
        btnRight.setOnDragOver(handlerDragOver);
        labelBox.setOnDragOver(handlerDragOver);
        filterSlider.setOnDragOver(handlerDragOver);

        EventHandler<DragEvent> handlerDragEntered = event -> ParallelCoordinatesAxis.this.highlightAxis(true);

        btnInvert.setOnDragEntered(handlerDragEntered);
        btnLeft.setOnDragEntered(handlerDragEntered);
        btnRight.setOnDragEntered(handlerDragEntered);
        labelBox.setOnDragEntered(handlerDragEntered);
        filterSlider.setOnDragEntered(handlerDragEntered);

        EventHandler<DragEvent> handlerDragExited = event -> {
            String oldAxisAsString = event.getDragboard().getString();
            if (event.getDragboard().hasString() && oldAxisAsString != null) {
                // System.out.println("drag exited at:" + axisIndex + " from " + oldAxisAsString);

                int oldAxisIndex = Integer.parseInt(oldAxisAsString);
                if (oldAxisIndex != axisIndex) {
                    ParallelCoordinatesAxis.this.highlightAxis(false);
                }
            }

            // event.consume();
        };

        btnInvert.setOnDragExited(handlerDragExited);
        btnLeft.setOnDragExited(handlerDragExited);
        btnRight.setOnDragExited(handlerDragExited);
        labelBox.setOnDragExited(handlerDragExited);
        filterSlider.setOnDragExited(handlerDragExited);

        EventHandler<DragEvent> dropHandlerAxis = event -> {
            /* data dropped */
            /* if there is a string data on dragboard, read it and use it */
            boolean success = false;
            String oldAxisAsString = event.getDragboard().getString();
            if (event.getDragboard().hasString() && oldAxisAsString != null) {
                int oldAxisIndex = Integer.parseInt(oldAxisAsString);
                ParallelCoordinatesAxis.this.highlightAxis(false);

                chart.getAxisByIndex(oldAxisIndex).highlightAxis(false);
                success = true;
                //  System.out.println("drag dropped at:" + axisIndex + " from " + oldAxisAsString);

                if (oldAxisIndex != axisIndex) {
                    chart.swapAxes(oldAxisIndex, axisIndex);
                }
            }


            /* let the source know whether the string was successfully
             * transferred and used */
            event.setDropCompleted(success);
            //  event.consume();
        };

        filterSlider.setOnDragDropped(dropHandlerAxis);
        labelBox.setOnDragDropped(dropHandlerAxis);
        btnInvert.setOnDragDropped(dropHandlerAxis);
        btnLeft.setOnDragDropped(dropHandlerAxis);
        btnRight.setOnDragDropped(dropHandlerAxis);

        /* ==== set label drag and drops === */
        labelDragAndDrop.setOnDragOver(event -> {
            /* data is dragged over the target */
            /* if it has a string data */
            if (event.getDragboard().hasString()) {
                /* allow for both copying and moving, whatever user chooses */
                event.acceptTransferModes(TransferMode.MOVE);
            }

            // event.consume();
        });

        labelDragAndDrop.setOnDragEntered(event -> {
            labelDragAndDrop.show(true);
        });

        labelDragAndDrop.setOnDragExited(event -> {
            labelDragAndDrop.show(false);
        });

        labelDragAndDrop.setOnDragDropped(event -> {
            /* data dropped */
            /* if there is a string data on dragboard, read it and use it */
            boolean success = false;
            String oldAxisAsString = event.getDragboard().getString();
            if (event.getDragboard().hasString() && oldAxisAsString != null) {
                success = true;
                //System.out.println("drag dropped between:" + labelDragAndDrop.getAxisLeft() + " and " + labelDragAndDrop.getAxisRight());

                int oldIndex = Integer.parseInt(oldAxisAsString);

                ParallelCoordinatesAxis axisRight = labelDragAndDrop.getAxisRight();
                int newIndex;
                if (axisRight != null) {
                    newIndex = axisRight.getAxisIndex();
                    if (oldIndex < newIndex) {
                        newIndex--;
                    }
                } else {
                    newIndex = chart.getAxisByIndex(chart.getAttributeCount() - 1).getAxisIndex();
                }

                if (newIndex != oldIndex) {
                    chart.moveAxis(oldIndex, newIndex);
                }

                chart.getAxisByIndex(oldIndex).highlightAxis(false);
                labelDragAndDrop.show(false);
            }


            /* let the source know whether the string was successfully
             * transferred and used */
            event.setDropCompleted(success);
            // event.consume();
        });
    }

    /**
     * Registers a TickLabelFormatter which also correctly displays values for
     * the inverted axis
     */
    private void setTickLabelFormatter(Double low, Double high) {
        axis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(axis) {
            @Override
            public String toString(Number value) {
                // if the lower and upper bound were negated, the displayed value has to be fixed
                double val = value.doubleValue();
                if (inverted) {
                    val = -val;
                }

                // call original formatter with inverted value, check to decide precision
                //if low and high are close
                if (high - low < 1) {
                    return super.toString(new Double(val));
                } else if (high - low > 1 && high - low < 10) {
                    return String.format("%.2f", val);
                } else {
                    return String.format("%.0f", val);
                }
            }
        });
    }

    /**
     * Inverts the axis by negating both lower and upper values this seems to be
     * the only efficient and easy way to correctly display the values
     */
    @SuppressWarnings("unchecked")
    public void invert() {
        double lower = axis.getLowerBound();
        double higher = axis.getUpperBound();
        double temp = lower;

        lower = -higher;
        higher = -temp;

        axis.setUpperBound(higher);
        axis.setLowerBound(lower);
        inverted = !inverted;

        if (filterSlider != null) {

            // adjust filters
            double filterHighTmp = 1.0 - filterLow;
            double filterLowTmp = 1.0 - filterHigh;

            if (!inverted) {
                //when inverting back to normal, change values
                filterHighTmp = filterHigh;
                filterLowTmp = filterLow;
            }

            // remove listeners and add them again afterwards
            ChangeListener<Number> highListener = (ChangeListener<Number>) filterSlider.getProperties().get("highListener");
            ChangeListener<Number> lowListener = (ChangeListener<Number>) filterSlider.getProperties().get("lowListener");
            filterSlider.highValueProperty().removeListener(highListener);
            filterSlider.lowValueProperty().removeListener(lowListener);

            if (!inverted) {
                filterLow = filterLowTmp;
                filterHigh = filterHighTmp;
            }
            filterSlider.setLowValue(filterLowTmp);
            filterSlider.setHighValue(filterHighTmp);
            filterSlider.setLowValue(filterLowTmp);
            filterSlider.setHighValue(filterHighTmp);

            filterSlider.highValueProperty().addListener(highListener);
            filterSlider.lowValueProperty().addListener(lowListener);
        }
    }

    public NumberAxis getAxis() {
        return axis;
    }

    public boolean isInverted() {
        return inverted;
    }

    public int getAxisIndex() {
        return axisIndex;
    }

    public Label getAxisLabel() {
        return axisLabel;
    }

    public void setAxisLabel(Label axisLabel) {
        this.axisLabel = axisLabel;
    }

    public void setAxisIndex(int axisIndex) {
        this.axisIndex = axisIndex;
    }

    public String getLabel() {
        return label;
    }

    public HBox getLabelBox() {
        return labelBox;
    }

    public RangeSlider getFilterSlider() {
        return filterSlider;
    }

    public double getFilterHigh() {
        return filterHigh;
    }

    public void setFilterHigh(double filterHigh) {
        this.filterHigh = filterHigh;
    }

    public double getFilterLow() {
        return filterLow;
    }

    public void setFilterLow(double filterLow) {
        this.filterLow = filterLow;
    }

    public Button getBtnInvert() {
        return btnInvert;
    }

    public void setBtnInvert(Button btnInvert) {
        this.btnInvert = btnInvert;
    }

    public int getId() {
        return id;
    }

    public Button getBtnLeft() {
        return btnLeft;
    }

    public void setBtnLeft(Button btnLeft) {
        this.btnLeft = btnLeft;
    }

    public Button getBtnRight() {
        return btnRight;
    }

    public void setBtnRight(Button btnRight) {
        this.btnRight = btnRight;
    }

    public ParallelCoordinatesChart.DragAndDropLabel getLabelDragAndDrop() {
        return labelDragAndDrop;
    }

    /**
     * Highlights the given axis using a background color
     *
     * @param axisHighlighted
     */
    public void highlightAxis(boolean axisHighlighted) {
        Background background = null;

        if (axisHighlighted) {
            background = new Background(new BackgroundFill(Color.NAVY, CornerRadii.EMPTY, Insets.EMPTY));
        }

        // has to be done this way, not via opacity!
        axis.setBackground(background);
    }

    public int getFilterSliderFixation() {
        return filterSliderFixation;
    }

    public void setFilterSliderFixation(int filterSliderFixation) {
        this.filterSliderFixation = filterSliderFixation;
    }

    public void increaseFilterSliderFixation(int t) {
        filterSliderFixation = t + filterSliderFixation;

        String labelColor;

        switch (filterSliderFixation) {
            case 100:
                labelColor = "#F2D9D9";
                axisLabel.setStyle("-fx-border-width: 2 2 4 2;"
                        + "-fx-background-color: " + labelColor + ";"
                        + "-fx-border-color: #EBEBEB #EBEBEB " + labelColor + " #EBEBEB");
                break;
            case 300:
                labelColor = "#CC6666";
                axisLabel.setStyle("-fx-border-width: 2 2 4 2;"
                        + "-fx-background-color: " + labelColor + ";"
                        + "-fx-border-color: #EBEBEB #EBEBEB " + labelColor + " #EBEBEB");
                break;
            case 1000:
                labelColor = "#BF4040";
                axisLabel.setStyle("-fx-border-width: 2 2 4 2;"
                        + "-fx-background-color: " + labelColor + ";"
                        + "-fx-border-color: #EBEBEB #EBEBEB " + labelColor + " #EBEBEB");
                break;
            case 5000:
                labelColor = "#993333";
                axisLabel.setStyle("-fx-border-width: 2 2 4 2;"
                        + "-fx-background-color: " + labelColor + ";"
                        + "-fx-border-color: #EBEBEB #EBEBEB " + labelColor + " #EBEBEB");
                break;

            case 10000:
                labelColor = "#732626";
                axisLabel.setStyle("-fx-border-width: 2 2 4 2;"
                        + "-fx-background-color: " + labelColor + ";"
                        + "-fx-border-color: #EBEBEB #EBEBEB " + labelColor + " #EBEBEB");
                break;
        }

//        }
    }
}
