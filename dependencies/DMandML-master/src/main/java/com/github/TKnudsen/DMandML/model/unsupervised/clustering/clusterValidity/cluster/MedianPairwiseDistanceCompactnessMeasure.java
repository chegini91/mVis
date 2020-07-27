package com.github.TKnudsen.DMandML.model.unsupervised.clustering.clusterValidity.cluster;

import com.github.TKnudsen.ComplexDataObject.data.distanceMatrix.DistanceMatrixTools;
import com.github.TKnudsen.ComplexDataObject.data.distanceMatrix.IDistanceMatrix;
import com.github.TKnudsen.ComplexDataObject.data.interfaces.IDObject;
import com.github.TKnudsen.ComplexDataObject.model.tools.StatisticsSupport;
import com.github.TKnudsen.DMandML.data.cluster.Cluster;

/**
 * Computes the compactness as the median of all pairwise distances
 */
public class MedianPairwiseDistanceCompactnessMeasure<FV extends IDObject> extends ClusterCompactnessMeasure<FV> {

	@Override
	public double getMeasure(Cluster<FV> cluster) {

		IDistanceMatrix<FV> pairwiseDistances = this.getPairwiseDistances(cluster);
		StatisticsSupport distanceStatistics = DistanceMatrixTools.getPairwiseDistances(pairwiseDistances, true, true);
		return distanceStatistics.getMedian();
	}

}