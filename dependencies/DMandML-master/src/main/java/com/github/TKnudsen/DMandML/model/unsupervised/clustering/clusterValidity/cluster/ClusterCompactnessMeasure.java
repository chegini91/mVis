package com.github.TKnudsen.DMandML.model.unsupervised.clustering.clusterValidity.cluster;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.distanceMatrix.DistanceMatrix;
import com.github.TKnudsen.ComplexDataObject.data.distanceMatrix.IDistanceMatrix;
import com.github.TKnudsen.ComplexDataObject.data.interfaces.IDObject;
import com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.IDistanceMeasure;
import com.github.TKnudsen.ComplexDataObject.model.tools.StatisticsSupport;
import com.github.TKnudsen.DMandML.data.cluster.Cluster;
import com.github.TKnudsen.DMandML.data.cluster.ClusterTools;

/**
 * <p>
 * Title: WithinClusterDistancesMeasure
 * </p>
 * 
 * <p>
 * Description: Interface for the series of measures that can be applied on a
 * single cluster. Compactness is a prominent example.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2017 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public abstract class ClusterCompactnessMeasure<FV extends IDObject> implements IFeatureVectorClusterMeasure<FV> {

	protected StatisticsSupport getDistancesToCentroid(Cluster<FV> cluster) {

		IDistanceMeasure<FV> distanceMeasure = cluster.getDistanceMeasure();
		FV centroid = cluster.getCentroid().getData();

		List<Double> distances = new ArrayList<>(cluster.size());

		Iterator<FV> iterator = cluster.getElements().iterator();
		while (iterator.hasNext()) {
			FV next = iterator.next();
			distances.add(distanceMeasure.getDistance(centroid, next));
		}

		return new StatisticsSupport(distances);
	}

	/**
	 * constructs a distance matrix for the FVs. Aggregation can be used to cope
	 * with large matrix sizes. Recommendation: use aggregation for n >1000
	 * 
	 * @param cluster
	 * @param sizeThresholdToUseAggregation
	 *            above: an AggregationBasedDistanceMatrix is used
	 * @return
	 */
	protected IDistanceMatrix<FV> getPairwiseDistances(Cluster<FV> cluster) {

		return new DistanceMatrix<>(ClusterTools.getElementList(cluster), cluster.getDistanceMeasure());
	}
}
