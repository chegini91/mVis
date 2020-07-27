/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.model.indexing.metrics;

import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.dense.row.NormOps_DDRM;

/**
 * more numerically save version of cosine
 *
 * @author Robert Gregor <r.gregor@cgv.tugraz.at>
 */
public class CosineSave extends Cosine {

    @Override
    public Double apply(DMatrixRMaj a, DMatrixRMaj b) {
        final DMatrixRMaj c = new DMatrixRMaj(a);
        CommonOps_DDRM.elementMult(c, b);
        return CommonOps_DDRM.elementSum(c) / (NormOps_DDRM.normP2(a) * NormOps_DDRM.normP2(b));
    }
}
