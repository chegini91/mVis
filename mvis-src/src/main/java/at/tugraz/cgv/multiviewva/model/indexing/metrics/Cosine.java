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
 *
 * Distance metric relying on the (normed) cosine between two vectors... note
 * that the current implementation treats matrices as if they were vectors
 * when using with matrices, make sure that they are both in the same format (RowVectors vs ColVectors, dimensionality etc.)
 *
 * If you encounter numerical instabilities, consider using CosineSave as a
 * metric instead
 *
 * @author Robert Gregor <r.gregor@cgv.tugraz.at>
 *
 */
public class Cosine implements SimilarityMeasure {

    @Override
    public Double apply(DMatrixRMaj a, DMatrixRMaj b) {
        final DMatrixRMaj c = new DMatrixRMaj(a);
        CommonOps_DDRM.elementMult(c,b);
        return CommonOps_DDRM.elementSum(c) / (NormOps_DDRM.fastNormP2(a) * NormOps_DDRM.fastNormP2(b));

    }

}
