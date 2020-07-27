package com.github.TKnudsen.DMandML.model.unsupervised.clustering.clusterValidity;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.IFeatureVectorObject;
import com.github.TKnudsen.ComplexDataObject.data.interfaces.ISelfDescription;
import com.github.TKnudsen.DMandML.data.cluster.featureVector.FeatureVectorClusteringResult;
import com.github.TKnudsen.DMandML.data.cluster.featureVector.IFeatureVectorClusteringResultSupplier;

/**
 * <p>
 * Title: IClusterValidityMeasure
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
 * @version 1.03
 * 
 * TODO_GENERIC The "getClusterResultSet" method does not seem to be used, and makes this very specific
 */
public interface IClusterValidityMeasure<FV extends IFeatureVectorObject<?, ?>> extends ISelfDescription {

	public IFeatureVectorClusteringResultSupplier<FeatureVectorClusteringResult<FV>> getClusterResultSet();

	public void run();

	public double getClusterValidity();
}
