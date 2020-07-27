package com.github.TKnudsen.DMandML.model.unsupervised.clustering.clusterValidity.cluster;

import com.github.TKnudsen.ComplexDataObject.data.distanceMatrix.DistanceMatrixTools;
import com.github.TKnudsen.ComplexDataObject.data.distanceMatrix.IDistanceMatrix;
import com.github.TKnudsen.ComplexDataObject.data.interfaces.IFeatureVectorObject;
import com.github.TKnudsen.ComplexDataObject.model.tools.StatisticsSupport;
import com.github.TKnudsen.DMandML.data.cluster.Cluster;

/**
 * <p>
 * Title: MaxPairwiseDistanceCompactnessMeasure
 * </p>
 * 
 * <p>
 * Description: Compactess based on the average pairwise distance of all
 * instances.
 * 
 * Disadvantage: time-consuming computation.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2017 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class MaxPairwiseDistanceCompactnessMeasure<FV extends IFeatureVectorObject<?, ?>> extends ClusterCompactnessMeasure<FV> {

	@Override
	public double getMeasure(Cluster<FV> cluster) {

		IDistanceMatrix<FV> pairwiseDistances = this.getPairwiseDistances(cluster);
		StatisticsSupport distanceStatistics = DistanceMatrixTools.getPairwiseDistances(pairwiseDistances, true, true);

		return distanceStatistics.getMax();
	}

}