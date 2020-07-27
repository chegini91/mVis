package tugraz.ivis.parcoord;

import java.io.File;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import at.tugraz.cgv.multiviewva.gui.charts.ParallelCoordinatesChart;
import at.tugraz.cgv.multiviewva.model.Series;
import at.tugraz.cgv.multiviewva.model.DataModel;
import at.tugraz.cgv.multiviewva.model.Item;

// FXML interaction goes here
public class ParcoordController {
    // === FXML params
    @FXML
    private ParallelCoordinatesChart parcoordChart = null;

    @FXML
    RadioMenuItem toggle_legend;

    // === other helper params
    private Stage stage;
    

    // this is called by Main
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void onFileOpen(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Open CSV File");
        // for now, select code repo - better for testing
        // changed to current directory
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV", "*.csv"),
                new FileChooser.ExtensionFilter("All", "*.*"));
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            /*
                wanted to do some kind of feedback while loading, didn't succeed

                //parcoordChart.clear();
                parcoordChart.setTitle("loading ...");
            */
            importDataFromFile(file.getAbsolutePath());


            parcoordChart.setTitle(file.getName());
        }
    }

    @FXML
    public void onShowInfo(ActionEvent actionEvent) {
        showInfoDialog("Project made over the course of the lecture " +
                "'Information Visualisation'\nat Graz University of Technology by:" +
                "\n  Thomas Absenger\n  Mohammad Chegini\n  Thorsten Ruprechter\n  Helmut Zöhrer");
    }

    @FXML
    public void onResetBrushing(ActionEvent actionEvent) {
        if (parcoordChart != null)
            parcoordChart.resetBrushing();
    }

    private void importDataFromFile(String absolutePath) {
        // just print for testing for now
        DataModel dm = null;

        try {
            dm = new DataModel(absolutePath, ";", true);
        } catch (Exception e) {
            showErrorDialog("Error while importing",
                    "An error occured while parsing the input file.");

            e.printStackTrace();
        }

        if (dm != null) {
//            dm.printDataSet();
            setDataModelToGraph(dm);
        }

        parcoordChart.enableBrushing();
    }

    private void showErrorDialog(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.show();
    }

    private void showInfoDialog(String headerText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(headerText);
        alert.show();
    }

    public void setDataModelToGraph(DataModel dm) {
        // example data usage
        List<Item> series = dm.getItems();
        List<Item> series1 = series.subList(0, series.size() / 3);
        List<Item> series2 = series.subList(series.size() / 3 + 1, 2 * series.size() / 3);
        List<Item> series3 = series.subList(2 * series.size() / 3 + 1, series.size() - 1);

        Series s1 = new Series("Series1", series1, Color.RED, 0.2);
        Series s2 = new Series("Series2", series2, Color.BLUE, 0.2);
        Series s3 = new Series("Series3", series3, Color.GREEN, 0.2);

        parcoordChart.clear();
        parcoordChart.setMinMaxValuesFromArray(dm.getMinMaxValues());
        parcoordChart.setAxisLabels(dm.getDataHeader());

        parcoordChart.addSeries(s1);
        parcoordChart.addSeries(s2);
        parcoordChart.addSeries(s3);

        parcoordChart.setHighlightColor(Color.BLACK);
        parcoordChart.setHighlightStrokeWidth(3);

        toggle_legend.setDisable(false);

//        parcoordChart.drawLegend();
    }

    public void toggleLegend(ActionEvent actionEvent) {
        if (parcoordChart != null) {
            parcoordChart.toggleShowLegend();
        }
    }
}
