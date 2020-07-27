package com.github.TKnudsen.DMandML.data.cluster.featureVector.numerical;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.DMandML.data.cluster.featureVector.FeatureVectorClusteringResult;

/**
 * <p>
 * Title: NumericalFeatureVectorClusterResult
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 */
public class NumericalFeatureVectorClusterResult extends FeatureVectorClusteringResult<NumericalFeatureVector> {

	public NumericalFeatureVectorClusterResult(List<NumericalFeatureVectorCluster> clusters) {
		this(clusters, null);
	}

	public NumericalFeatureVectorClusterResult(List<NumericalFeatureVectorCluster> clusters, String name) {
		super(clusters, name);
	}

}
