package com.github.TKnudsen.DMandML.model.unsupervised.clustering.clusterValidity.cluster;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.IFeatureVectorObject;
import com.github.TKnudsen.ComplexDataObject.model.tools.StatisticsSupport;
import com.github.TKnudsen.DMandML.data.cluster.Cluster;

/**
 * <p>
 * Title: MaxCentroidDistanceCompactnessMeasure
 * </p>
 * 
 * <p>
 * Description: Compactess based on the maximum distance to the cluster
 * centroid.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2017 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class MaxCentroidDistanceCompactnessMeasure<FV extends IFeatureVectorObject<?, ?>> extends ClusterCompactnessMeasure<FV> {

	@Override
	public double getMeasure(Cluster<FV> cluster) {

		StatisticsSupport distancesToCentroid = this.getDistancesToCentroid(cluster);

		return distancesToCentroid.getMax();
	}

}