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
 * Implements the Minkowski distance Lp where  p=infinity
 * 
 * also knows as supremum norm
 * 
 * this might come in handy for descriptors where few, large differences along certain dimensions are more relevant for 'dissimilarity' than a high number of small differences along many dimensions
 * 
 * also, it is numerically more stable than Lp where p >= 2.0 but slower to compute than L2
 * 
 * NOTE: When used with matrices, this does not compute the induced supremum norm but treats the matrix as if it were a vector, use the Lp class with Double.POSITIVE_INFINITY you wish to do 
 * 
 * @author Robert Gregor <r.gregor@cgv.tugraz.at>
 */
public class Linf implements SimilarityMeasure {

    public static final double dist(final DMatrixRMaj a, final DMatrixRMaj b) {
        DMatrixRMaj c = new DMatrixRMaj(a);
        CommonOps_DDRM.subtractEquals(c, b);
        return CommonOps_DDRM.elementMaxAbs(c);	
    }
    
    @Override
    public Double apply(final DMatrixRMaj a, final DMatrixRMaj b) {
        return dist(a,b);	
    }
    
}
