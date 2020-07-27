package com.github.TKnudsen.DMandML.data.distanceMatrix.test;

import java.util.HashMap;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.distanceMatrix.DistanceMatrix;
import com.github.TKnudsen.ComplexDataObject.data.interfaces.IFeatureVectorObject;
import com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.IDistanceMeasure;
import com.github.TKnudsen.DMandML.data.cluster.Cluster;
import com.github.TKnudsen.DMandML.data.cluster.ICluster;
import com.github.TKnudsen.DMandML.data.cluster.IClusteringResult;
import com.github.TKnudsen.DMandML.data.clustering.ClusterResultWithClusterLookupSupport;
import com.github.TKnudsen.DMandML.model.unsupervised.clustering.IClusteringAlgorithm;
import com.github.TKnudsen.DMandML.model.unsupervised.clustering.impl.KMeans;

/**
 * <p>
 * Title: AggregationBasedDistanceMatrix
 * </p>
 * 
 * <p>
 * Description: Distance matrix that uses aggregation to avoid space allocation
 * problems. The aggregation level is a user parameter.
 * 
 * Please note that aggregation reduces the precision of distance computation.
 * Depending on the aggregation level the distance results can be inaccurate by
 * several percent.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2017 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 */
public class AggregationBasedDistanceMatrix<FV extends IFeatureVectorObject<?, ?>> extends DistanceMatrix<FV> {

	private int aggregationLevel = 1000;

	private IClusteringAlgorithm<FV> clusteringAlgorithm;

	private ClusterResultWithClusterLookupSupport<FV, Cluster<FV>> clusteringResult;

	@Deprecated
	public AggregationBasedDistanceMatrix(List<FV> objects, IDistanceMeasure<FV> distanceMeasure) {
		super(objects, distanceMeasure);
	}

	@Deprecated
	public AggregationBasedDistanceMatrix(List<FV> objects, IDistanceMeasure<FV> distanceMeasure,
			int aggregationLevel) {
		super(objects, distanceMeasure);

		throw new IllegalArgumentException(
				"AggregationBasedDistanceMatrix: design pitfall. initialize is executed before aggregationLevel is set.");

		// this.aggregationLevel = aggregationLevel;
	}

	@Deprecated
	public AggregationBasedDistanceMatrix(List<FV> objects, IDistanceMeasure<FV> distanceMeasure,
			IClusteringAlgorithm<FV> clusteringAlgorithm) {
		this(objects, distanceMeasure);

		this.clusteringAlgorithm = clusteringAlgorithm;
	}

	protected void aggregateData() {
		// TODO include MixedFeatureVector Clustering and then separate between
		// the two types
		if (clusteringAlgorithm == null) {
			clusteringAlgorithm = (IClusteringAlgorithm<FV>) new KMeans(aggregationLevel);
			clusteringAlgorithm.setFeatureVectors(getElements());
		}

		clusteringAlgorithm.calculateClustering();
		IClusteringResult<FV, ? extends ICluster<FV>> clusteringResult = clusteringAlgorithm.getClusteringResult();

	}

	protected ClusterResultWithClusterLookupSupport<FV, Cluster<FV>> getClusterResult() {
		if (clusteringResult == null)
			aggregateData();

		return clusteringResult;
	}

	protected FV getElementAggregate(FV t) {
		// make sure that no cluster element is queried.
		// if so a cluster is the lookup of itself.
		if (objectIndex.containsKey(t))
			return t;

		ClusterResultWithClusterLookupSupport<FV, Cluster<FV>> clusterResult = getClusterResult();
		Cluster<FV> cluster = clusterResult.getCluster(t);
		return cluster.getCentroid().getData();
	}

	@Override
	protected void initializeObjectIndex() {
		// create index
		objectIndex = new HashMap<>();

		ClusterResultWithClusterLookupSupport<FV, Cluster<FV>> clusterResult = getClusterResult();
		for (int i = 0; i < clusterResult.getClusters().size(); i++) {
			FV t = clusterResult.getClusters().get(i).getCentroid().getData();
			objectIndex.put(t, i);
		}
	}

	@Override
	protected Integer getObjectIndex(FV object) {
		if (objectIndex == null)
			initializeDistanceMatrix();

		return objectIndex.get(getElementAggregate(object));
	}

	public int getAggregationLevel() {
		return aggregationLevel;
	}

	public void setAggregationLevel(int aggregationLevel) {
		this.aggregationLevel = aggregationLevel;

		clusteringAlgorithm = null;
		clusteringResult = null;

		initializeDistanceMatrix();
	}

	public IClusteringAlgorithm<?> getClusteringAlgorithm() {
		return clusteringAlgorithm;
	}

}
