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
 * Implements the Minkowski distance where p= 2 
 * also known as Euclidean distance (for vectors),
 * also known as Frobenius norm (for matrices)
 * 
 * This metric might be handy if neighboring dimensions of your descriptor are closely related to each other concerning their semantics.
 * The effect is more emphasized when using p>2, but computational costs and numerical stability will be more of an issue with p > 2.
 * 
 * Note: Earth Mover's Distance might be worth implementing when using such descriptors (and other histogram descriptors) ... however EMD is extremely expensive in comparison 
 * 
 * Note: If used with matrices, they are treated as if they were vectors, If you want the induced norm, try using Lp with P=2 instead
 * 
 * For histogram features, this might make sense if for overall just slightly different objects, peaks in the histogram are shifted to adjacent bins   
 * (e.g. try using this this when your descriptor is the result of a Fourier Transform)
 * @author Robert Gregor <r.gregor@cgv.tugraz.at>
 */
public class L2 implements SimilarityMeasure {

    public static final double dist(final DMatrixRMaj a, final DMatrixRMaj b) {
        final double d = SpecializedOps_DDRM.diffNormF_fast(a,b);
        assert Double.isFinite(d); //if unsure when using this norm, enable java assertions for this package to check if things are numerically stable
        return d;
    }
    
    @Override
    public Double apply(final DMatrixRMaj a, final DMatrixRMaj b) {
        return dist(a,b);
    }
    
}
