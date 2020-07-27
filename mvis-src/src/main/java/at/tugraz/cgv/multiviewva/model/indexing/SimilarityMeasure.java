/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.model.indexing;

import java.util.function.BiFunction;
import org.ejml.data.DMatrixRMaj;

/**
 *  Interface that provides a method for computing a dissimilarity function between two descriptors (i.e. Feature Vectors or matrices)
 * 
 *  For basic feature vectors, the two matrices should contain N dimensional row vectors (stored in matrices with 1 x N dimensionality)
 * 
 *  Instances of this interface may actually deviate from implementing a true metric 
 * (e.g. it is possible to use a semi-metric or something completely different.
 *  However, the effects when used with different Index instances may be unexpected,
 *  you should know what you are doing)
 * 
 * 
 * @author Robert Gregor <r.gregor@cgv.tugraz.at>
 */
@FunctionalInterface
public interface SimilarityMeasure extends BiFunction<DMatrixRMaj,DMatrixRMaj,Double> {
    
    @Override
    Double apply(final DMatrixRMaj descriptor1, final DMatrixRMaj descriptor2);
}
