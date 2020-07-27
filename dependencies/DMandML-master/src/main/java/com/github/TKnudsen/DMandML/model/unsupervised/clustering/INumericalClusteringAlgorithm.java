package com.github.TKnudsen.DMandML.model.unsupervised.clustering;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.DMandML.data.cluster.featureVector.numerical.NumericalFeatureVectorClusterResult;

/**
 * <p>
 * Title: INumericalClusteringAlgorithm
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: (c) 2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.03
 */
public interface INumericalClusteringAlgorithm extends IClusteringAlgorithm<NumericalFeatureVector> {

	@Override
	public NumericalFeatureVectorClusterResult getClusteringResult();
}