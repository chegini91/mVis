/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.model.indexing;

import com.google.common.collect.ImmutableMap;
import java.util.function.BiFunction;
import org.ejml.data.DMatrixRMaj;

/**
 *
 * @author Robert Gregor <r.gregor@cgv.tugraz.at>
 * @param <OT> The Object type from which feature vectors can be extracted by an
 * implementation
 */
@FunctionalInterface
public interface FeatureExtractor<OT> extends BiFunction<OT, ImmutableMap<String,String>, DMatrixRMaj> {

    /**
     *
     * @param object
     * @param properties
     *
     * @return the feature vector as row vector in a Nx1 matrix (rows x columns)
     * NOTE: when used in conjunction with an appropriate SimilarityMeasure, the
     * feature could also be an arbitrary matrix if the matrix is suppoes to
     * represent a list of vectors, it is in almost all cases more reasonable to
     * please store them as row Vectors (that is with taking into account that
     * DMatrixRMaj is row major)
     *
     * check the implementations in model.indexing.metrics
     */
    @Override
    DMatrixRMaj apply(final OT object,final ImmutableMap<String, String> properties);


}
