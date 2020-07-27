/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.model.indexing;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import javafx.scene.chart.XYChart;
import at.tugraz.cgv.multiviewva.model.search.Box;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.DMatrixVisualization;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author mchegini
 */
public class ScatterPlot2DHistogramFE implements FeatureExtractor<List<XYChart.Series<Number, Number>>> {

    @Override
    public DMatrixRMaj apply(List<XYChart.Series<Number, Number>> object, ImmutableMap<String, String> properties) {
        int dimension = 4;
        SimpleMatrix result = new SimpleMatrix(object.size(), dimension * dimension);
//        ArrayList<double[]> result = new ArrayList<>();
        for (int i = 0; i < result.numRows(); i++) {
            Box box = new Box(dimension, dimension, object.get(i).getData());
            result.setRow(i, 0, box.getOne1DFV(dimension - 1, dimension - 1));
//            result.setRow(i, 0, box.get1DQuantileFV());
        }
//        DMatrixVisualization.show((DMatrixRMaj) result.getMatrix(), "title");
        return (DMatrixRMaj) result.getMatrix();
    }

}
