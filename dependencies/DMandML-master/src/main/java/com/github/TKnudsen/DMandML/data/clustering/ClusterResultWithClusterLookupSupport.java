package com.github.TKnudsen.DMandML.data.clustering;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.IDObject;
import com.github.TKnudsen.DMandML.data.cluster.Cluster;
import com.github.TKnudsen.DMandML.data.cluster.ClusteringResult;

/**
 * <p>
 * Title: ClusterResultWithClusterLookupSupport
 * </p>
 * 
 * <p>
 * Description: ClusterResult with an additional lookup of clusters for the
 * baseline elements.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2017 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class ClusterResultWithClusterLookupSupport<T extends IDObject, C extends Cluster<T>>
		extends ClusteringResult<T, C> {

	private Map<T, C> clusterLookup;

	public ClusterResultWithClusterLookupSupport(List<C> clusters) {
		this(clusters, null);
	}

	public ClusterResultWithClusterLookupSupport(List<C> clusters, String name) {
		super(clusters, name);

		initializeClusterLookup();
	}

	public ClusterResultWithClusterLookupSupport(ClusteringResult<T, C> clusteringResult) {
		this(clusteringResult, null);
	}

	public ClusterResultWithClusterLookupSupport(ClusteringResult<T, C> clusteringResult, String name) {
		super(clusteringResult.getClusters(), name);

		initializeClusterLookup();
	}

	private void initializeClusterLookup() {
		clusterLookup = new HashMap<>();

		for (C c : getClusters())
			for (T t : c.getElements())
				clusterLookup.put(t, c);
	}

	@Override
	public C getCluster(T t) {
		if (t == null)
			return null;

		return clusterLookup.get(t);
	}

}
