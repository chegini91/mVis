/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.model.indexing.metrics;

import at.tugraz.cgv.multiviewva.model.indexing.SimilarityMeasure;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.SpecializedOps_DDRM;

/**
 * Implements the Manhattan Distance (a.k.a. Cityblock distance)
 * if totally unsure about your descriptor characteristics, try this one first
 * 
 * if you know that your descriptors are known to not contain zeros, try ChiSquare instead
 * 
 * Note: When used with matrices, they are treated as if they were vectors, try using the Lp class if you want the induced L1 norm
 * 
 * @author Robert Gregor <r.gregor@cgv.tugraz.at>
 * 
 * @see http://www.presious.eu/sites/default/files/cbmi.pdf for a paper ;-)
 */
public class L1 implements SimilarityMeasure {

    public static final double dist(final DMatrixRMaj a, final DMatrixRMaj b) {
        return SpecializedOps_DDRM.diffNormP1(a,b);
    }
    
    @Override
    public Double apply(final DMatrixRMaj a, final DMatrixRMaj b) {
        return dist(a,b);
    }
    
}
