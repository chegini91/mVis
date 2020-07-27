package com.github.TKnudsen.DMandML.model.unsupervised.clustering.splitting;

import java.util.ArrayList;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.IDistanceMeasure;
import com.github.TKnudsen.DMandML.data.cluster.Cluster;
import com.github.TKnudsen.DMandML.data.cluster.ClusterTools;
import com.github.TKnudsen.DMandML.data.cluster.featureVector.numerical.NumericalFeatureVectorClusterResult;
import com.github.TKnudsen.DMandML.model.unsupervised.clustering.IClusterSplittingAlgorithm;
import com.github.TKnudsen.DMandML.model.unsupervised.clustering.impl.KMeans;

/**
 * <p>
 * Title: MaximumDistanceSplitting
 * </p>
 * 
 * <p>
 * Description: splits a cluster into *n* clusters
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.03
 */
public class KMeansBasedClusterSplitting
		implements IClusterSplittingAlgorithm<NumericalFeatureVector, Cluster<NumericalFeatureVector>> {

	private int splitCount;

	private IDistanceMeasure<NumericalFeatureVector> distanceMeasure;

	private KMeans kmeans;

	public KMeansBasedClusterSplitting(int splitCount, IDistanceMeasure<NumericalFeatureVector> distanceMeasure) {
		this.splitCount = splitCount;
		this.distanceMeasure = distanceMeasure;
	}

	@Override
	public String getName() {
		return "KMeansBasedClusterSplitting";
	}

	@Override
	public String getDescription() {
		return "KMeansBasedClusterSplitting splits a cluster using the kmeans clustering algorithm";
	}

	public int getSetSplitCount() {
		return splitCount;
	}

	@Override
	public void setSplitCount(int splitCount) {
		this.splitCount = splitCount;
	}

	@Override
	public IDistanceMeasure<NumericalFeatureVector> getDistanceMeasure() {
		return distanceMeasure;
	}

	@Override
	public List<Cluster<NumericalFeatureVector>> splitCluster(Cluster<NumericalFeatureVector> cluster) {
		kmeans = new KMeans(splitCount, 17, ClusterTools.getElementList(cluster));
		kmeans.calculateClustering();
		NumericalFeatureVectorClusterResult clusterResultSet = kmeans.getClusteringResult();

		List<Cluster<NumericalFeatureVector>> ret = new ArrayList<>();

		int i = 0;
		for (Cluster<NumericalFeatureVector> c : clusterResultSet) {
			c.setName(c.getName() + "[" + i++ + "]");
			ret.add(c);
		}

		return ret;
	}

}
