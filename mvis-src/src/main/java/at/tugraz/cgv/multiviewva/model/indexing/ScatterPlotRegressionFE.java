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
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author mchegini
 */
public class ScatterPlotRegressionFE implements FeatureExtractor<List<XYChart.Series<Number, Number>>> {

    @Override
    public DMatrixRMaj apply(List<XYChart.Series<Number, Number>> object, ImmutableMap<String, String> properties) { 
       SimpleMatrix result = new SimpleMatrix(object.size(), 100);
//        ArrayList<double[]> result = new ArrayList<>();
       
        for (int i = 0; i < result.numRows(); i++) {
            Box box = new Box(5, 5, object.get(i).getData());
            result.setRow(i, 0, box.getRegressionDescriptor(4, 50));
        }
        return (DMatrixRMaj) result.getMatrix();
    }

}
