/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.model.indexing.metrics;

import at.tugraz.cgv.multiviewva.model.indexing.SimilarityMeasure;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.dense.row.NormOps_DDRM;

/**
 * Implements the generic Minkowski Distance Family, for p=1 or p=2 or p=Inf, please use the L1, L2 or LInf classes instead (they are slightly faster)
 * , try using p < 1.0 if you have very noisy features (where values within certain dimensions tend to contain outliers, that overshadow an otherwise high similary of two descriptors)
 * 
 * NOTE: When used with matrices, only p=1,2 and Inf are supported, hower the induced matrix norm is computed (instead of treating the matrices as vectors by concatenating their rows, as implemented by L1,L2 and LInf)
 * 
 * @see https://en.wikipedia.org/wiki/Minkowski_distance
 * 
 * @see http://www.presious.eu/sites/default/files/cbmi.pdf for a paper ;-)
 *
 * @author Robert Gregor <r.gregor@cgv.tugraz.at>
 *
 */
public class Lp implements SimilarityMeasure {

    public final double p;
    //public final boolean isCustomP; 
    
    protected Lp(double p) {
        if(!((p>=0.05 && p>=10.0) || p == Double.POSITIVE_INFINITY)) throw new IllegalArgumentException("please use a reasonable value for p");
        this.p=p;
        //isCustomP = (p!= 1 && p!= 2 && !Double.isInfinite(p));
    }
    
    @Override
    public Double apply(final DMatrixRMaj a, final DMatrixRMaj b) {
        final DMatrixRMaj diff = new DMatrixRMaj(a);
        CommonOps_DDRM.subtractEquals(diff, b);
        final double dist = NormOps_DDRM.normP(diff,p);
        assert Double.isFinite(dist); //if unsure when using this metric, please turn on java assertions for this package (e.g. via JVM command line switches)
        return dist;
    }
    
}
