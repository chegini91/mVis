/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.model.indexing.metrics;

import static java.util.Objects.requireNonNull;
import at.tugraz.cgv.multiviewva.model.indexing.SimilarityMeasure;
import org.ejml.data.DMatrixRMaj;
import at.tugraz.cgv.multiviewva.utility.math.MatrixUtils;
import static at.tugraz.cgv.multiviewva.utility.math.MatrixUtils.splitToRowVectors;

/**
 * Implements the Hausdorff distance between two sets of vectors. If both input
 * matrices are just row vectors, the result is identical to the distance
 * computed by the <SM> metric. Note that the number of row vectors in both
 * arguments can be arbitrary (>=1)
 *
 * the number of columns must be equal for both arguments
 *
 * This metric might come in handy if you goal is to detect partial matches
 * between two sets of vectors, where the smaller vector set is not overly
 * polluted with outliers
 *
 * When computing the Hausdorff distance between a row vector and a set of row
 * vectors, the result is the distance from single vector to the closest vector
 * in the set
 *
 * When computing the Hausdorff distance between to row vector sets, the result
 * is the maximum "closest" distance of the individual vectors from the first
 * set to the second set (or vice versa)
 *
 * intuitively speaking: the closes distance of the most outlying point (vector)
 * in one of the two sets to the other point cloud (vector set)
 *
 * The Hausdorff distance is a true metric (symmetric, transitive and positive
 * definite when a true metric is used for the point distance itself (such as L1
 * or L2 or LInf))
 *
 * ATTENTION: make sure that the vectors are stored in row major format (i.e. as
 * row vectors)
 *
 * @see https://en.wikipedia.org/wiki/Hausdorff_distance
 * @see
 * http://cgm.cs.mcgill.ca/~godfried/teaching/cg-projects/98/normand/main.html
 *
 * @author Robert Gregor <r.gregor@cgv.tugraz.at>
 * @param <SM> A Similarity metric that is used for computing individual point
 * distances
 *
 */
public class Hausdorff<SM extends SimilarityMeasure> implements SimilarityMeasure {

    protected final SM pointMetric;

    public Hausdorff(SM pointMetric) {
        this.pointMetric = requireNonNull(pointMetric, "passed Point Metric is null! NULL DOES NOT COMPUTE!");
    }

    @Override
    public Double apply(final DMatrixRMaj a, final DMatrixRMaj b) {
        if (MatrixUtils.isRowVector(a)) {
            return minPointToPointsDist(a, splitToRowVectors(b), pointMetric); //CommonOps_DDRM.rowsToVector()
        }
        if (MatrixUtils.isRowVector(b)) {
            return minPointToPointsDist(b, splitToRowVectors(a), pointMetric); //CommonOps_DDRM.rowsToVector()
        }
        if (a.numRows <= b.numRows) {
            return maxMinPointToPointsDist(a, b, pointMetric);
        }
        return maxMinPointToPointsDist(b, a, pointMetric);
    }

    public static final double minPointToPointsDist(final DMatrixRMaj point, final DMatrixRMaj[] points, final SimilarityMeasure sm) {
        assert MatrixUtils.isRowVector(point) && points.length > 0 && MatrixUtils.isRowVector(points[0]);

        double min = Double.MAX_VALUE;
        for (int i = 0; i < points.length; ++i) {
            final double curMin = sm.apply(point, points[i]);
            min = (curMin < min) ? curMin : min;
        }
//        assert min < Double.MAX_VALUE;
        return min;
    }

    public static final double maxMinPointToPointsDist(final DMatrixRMaj a, final DMatrixRMaj b, final SimilarityMeasure sm) {

        final DMatrixRMaj fewPoints[] = MatrixUtils.splitToRowVectors(a);
        final DMatrixRMaj morePoints[] = MatrixUtils.splitToRowVectors(b);

        assert fewPoints.length > 0 && morePoints.length > 0;

        double max = -Double.MIN_VALUE;
        for (int i = 0; i < fewPoints.length; ++i) {
            final double curMax = minPointToPointsDist(fewPoints[i], morePoints, sm);
            max = (curMax > max) ? curMax : max;
        }
        assert max > -Double.MIN_VALUE;
        return max;
    }
}
