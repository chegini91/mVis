package com.github.TKnudsen.DMandML.model.unsupervised.clustering.clusterValidity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.IFeatureVectorObject;
import com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.IDistanceMeasure;
import com.github.TKnudsen.DMandML.data.cluster.Cluster;
import com.github.TKnudsen.DMandML.data.cluster.featureVector.FeatureVectorCluster;
import com.github.TKnudsen.DMandML.data.cluster.featureVector.FeatureVectorClusteringResult;
import com.github.TKnudsen.DMandML.data.cluster.featureVector.IFeatureVectorClusteringResultSupplier;

/**
 * <p>
 * Title: DunnIndex
 * </p>
 * 
 * <p>
 * Description: Good clustering results contain well-separated clusters
 * (distances among clusters are large) as well as compact clusters (small
 * diameters).
 * 
 * Disadvantages: a) time consuming calculation, b) sensiticity to noise
 * (maximum cluster diameter)
 * 
 * J. C. Dunn: Well Separated Clusters and Optimal Fuzzy Partitions, Journal of
 * Cybernetica, Vol. 4, pp. 95-104, 1974
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 */
public class DunnIndex<FV extends IFeatureVectorObject<?, ?>> implements IClusterValidityMeasure<FV> {

	private IFeatureVectorClusteringResultSupplier<FeatureVectorClusteringResult<FV>> clusteringResultSupplier;

	private IDistanceMeasure<FV> featureVectorDistanceMeasure;

	private Double dunnIndex;

	public DunnIndex(IFeatureVectorClusteringResultSupplier<FeatureVectorClusteringResult<FV>> clusteringResultSupplier,
			IDistanceMeasure<FV> featureVectorDistanceMeasure) {

		this.clusteringResultSupplier = clusteringResultSupplier;
		this.featureVectorDistanceMeasure = featureVectorDistanceMeasure;
	}

	@Override
	public void run() {

		FeatureVectorClusteringResult<FV> featureVectorClusteringResult = clusteringResultSupplier.get();
		List<FeatureVectorCluster<FV>> clusters = featureVectorClusteringResult.getClusters();

		// calculate nearest cluster (excluding its own cluster)
		Map<FV, Cluster<FV>> nearestCluster = new LinkedHashMap<>();
		for (Cluster<FV> cluster1 : clusters)
			for (FV fv1 : cluster1) {
				double dist = Double.MAX_VALUE - 1;
				for (Cluster<FV> cluster2 : clusters) {
					if (cluster1.equals(cluster2))
						continue;
					else {
						double d = featureVectorDistanceMeasure.getDistance(fv1, cluster2.getCentroid().getData());
						if (d < dist) {
							dist = d;
							nearestCluster.put(fv1, cluster2);
						}
					}
				}
			}

		double silhouette = 0;
		double count = 0;

		for (Cluster<FV> cluster : clusters)
			for (FV featureVector : cluster) {
				double distInner = featureVectorDistanceMeasure.getDistance(featureVector,
						cluster.getCentroid().getData());
				double distOuter = featureVectorDistanceMeasure.getDistance(featureVector,
						nearestCluster.get(featureVector).getCentroid().getData());

				if (distInner < distOuter)
					silhouette += (1 - distInner / distOuter);
				else if (distInner == distOuter)
					silhouette += 0;
				else
					silhouette += (distOuter / distInner - 1);

				count++;
			}

		dunnIndex = silhouette / count;
	}

	@Override
	public double getClusterValidity() {
		if (dunnIndex == null) {
			System.err.println(getName() + ": index not calculated yet.");
			return Double.NaN;
		}

		return dunnIndex.doubleValue();
	}

	@Override
	public String getName() {
		return "Dunn Clustering Index";
	}

	@Override
	public String getDescription() {
		return "Cluster validity measure based on cluster compactness and separation measures";
	}

	@Override
	public IFeatureVectorClusteringResultSupplier<FeatureVectorClusteringResult<FV>> getClusterResultSet() {
		return clusteringResultSupplier;
	}

}
