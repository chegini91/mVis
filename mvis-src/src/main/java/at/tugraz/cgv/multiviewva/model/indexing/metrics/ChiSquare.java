/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.model.indexing.metrics;

import at.tugraz.cgv.multiviewva.model.indexing.SimilarityMeasure;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;

/**
 *
 * Implements the Chi Square distance... for feature vectors resembling
 * histograms for which all bins are greater than zero, 
 * the retrieval results are quite likely to be better than those of L1 and
 * especially those from L2 
 * 
 * DANGER: when using with descriptors that contain values <= 0, the result may be NaN or Infinite
 *
 * @author Robert Gregor <r.gregor@cgv.tugraz.at>
 *
 * @see http://www.presious.eu/sites/default/files/cbmi.pdf for a paper ;-)
 */
public class ChiSquare implements SimilarityMeasure {

    @Override
    public Double apply(final DMatrixRMaj a, final DMatrixRMaj b) {

        final DMatrixRMaj sum = new DMatrixRMaj(a);
        final DMatrixRMaj diff = new DMatrixRMaj(a);
        CommonOps_DDRM.subtractEquals(diff, b);
        CommonOps_DDRM.addEquals(sum, b);
        CommonOps_DDRM.elementMult(diff, diff);
        CommonOps_DDRM.elementDiv(diff, sum);
        final double dist = 0.5 * CommonOps_DDRM.elementSum(diff);
        assert Double.isFinite(dist); //if unsure when using this metric, please turn on java assertions for this package (e.g. via JVM command line switches)
        return dist;
    }

}
