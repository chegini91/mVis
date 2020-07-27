package com.github.TKnudsen.DMandML.data.cluster;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Title: ClusterDistanceDistribution
 * </p>
 * 
 * <p>
 * Description: baseline data structure for modeling centroids.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.03
 */
public class ClusterDistanceDistribution<T, C extends ICluster<T>> {

	private Map<C, Double> clusterDistances;

	public ClusterDistanceDistribution(Map<C, Double> clusterDistances) {
		this.clusterDistances = clusterDistances;
	}

	public Map<C, Double> getClusterDistances() {
		return clusterDistances;
	}

	public Double get(C cluster) {
		return clusterDistances.get(cluster);
	}

	public Set<C> keySet() {
		return clusterDistances.keySet();
	}
}
