package com.github.TKnudsen.DMandML.data.cluster.featureVector;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.IFeatureVectorObject;
import com.github.TKnudsen.DMandML.data.cluster.ClusteringResult;

/**
 * <p>
 * Title: FeatureVectorClusteringResult
 * </p>
 * 
 * <p>
 * Description: Feature Vector extension of a clusteringResult. with the
 * extension one of the generics parameters is removed.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.03
 */
public class FeatureVectorClusteringResult<FV extends IFeatureVectorObject<?, ?>>
		extends ClusteringResult<FV, FeatureVectorCluster<FV>> {

	public FeatureVectorClusteringResult(List<? extends FeatureVectorCluster<FV>> clusters) {
		super(clusters);
	}

	public FeatureVectorClusteringResult(List<? extends FeatureVectorCluster<FV>> clusters, String name) {
		super(clusters, name);
	}
}
