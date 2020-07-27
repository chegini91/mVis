package com.github.TKnudsen.DMandML.data.cluster.featureVector;

/**
 * <p>
 * Title: FeatureVectorClusteringResultSupplier
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2017 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class FeatureVectorClusteringResultSupplier<FVCR extends FeatureVectorClusteringResult<?>> implements IFeatureVectorClusteringResultSupplier<FVCR> {

	FVCR featureVectorClusteringResult;

	public FeatureVectorClusteringResultSupplier(FVCR featureVectorClusteringResult) {
		this.featureVectorClusteringResult = featureVectorClusteringResult;
	}

	@Override
	public FVCR get() {
		return featureVectorClusteringResult;
	}

}
