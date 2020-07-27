/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.model.indexing.metrics;

import at.tugraz.cgv.multiviewva.model.indexing.SimilarityMeasure;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.dense.row.MatrixFeatures_DDRM;
import org.ejml.dense.row.SpecializedOps_DDRM;
import at.tugraz.cgv.multiviewva.utility.SearchUtility;

/**
 *
 * @author Robert Gregor <r.gregor@cgv.tugraz.at>
 */
public class QuadraticForm implements SimilarityMeasure {

    private final DMatrixRMaj A;

    public QuadraticForm(DMatrixRMaj similarityWeightsMatrix) {
        A = similarityWeightsMatrix;
        if (A.numCols != A.numRows) {
            throw new IllegalArgumentException("A similarity MAtrix for a Quadratic FOrm distance should always be quadratic in shape");
        }
        if (A.numCols == 0) {
            throw new IllegalArgumentException("A similarity Matrix should contain values... it makes no sense to compare zero dimensional vectors!");
        }
        if (!MatrixFeatures_DDRM.isPositiveSemidefinite(A)) {
//            throw new IllegalArgumentException("A similarioty Matrix must always be positive semi-definite, otherwise, the resulting distance function will not be a metric! In practice, this translates to symmetric matrices with elements >= 0");
        }
    }

    @Override
    public Double apply(DMatrixRMaj descriptor1, DMatrixRMaj descriptor2) {

        final int dim = descriptor1.numCols * descriptor1.numRows;
        assert dim == A.numRows; // if this isn't the case you are doing something wrong.... how should the distance between different bins be weighted if there simple no information about it in the matrix
        //also if the simWieghtsMatrix A is larger, there is a pretty high chance that something is wrong with your implementation (hence we just fail here!)

        final DMatrixRMaj diff = new DMatrixRMaj(1, dim);
        CommonOps_DDRM.subtract(descriptor1, descriptor2, diff);

        final DMatrixRMaj temp = new DMatrixRMaj(1, dim);

        // temp = (descriptor1-descriptor2)^T x A
        CommonOps_DDRM.mult(diff, A, temp);

//        diff.reshape(dim, 1, true); // transposing a EJML vector from row to column for cheap skates

        //matrix mult between a row and a column vector == scalar product of the vectors, 
        // note that the square root is just computed for sticking to the formal definition of the quadratic form distance
        //... for retrieval/ranking tasks, computing sqrt could be omitted to slightly improve efficiency
        CommonOps_DDRM.elementMult(temp, diff);
        return Math.sqrt(CommonOps_DDRM.elementSum(temp)); // return sqrt ( temp x (descriptor1-descriptor2))
    }
}
