package com.github.TKnudsen.DMandML.model.unsupervised.clustering.clusterValidity.cluster;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.IDObject;

/**
 * <p>
 * Title: IFeatureVectorClusterMeasure
 * </p>
 * 
 * <p>
 * Description: Interface for the series of measures that can be applied on a
 * single cluster. Compactness is a prominent example.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2017 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 * 
 * TODO_GENERICS This is now equivalent to IClusterMeasure
 */
public interface IFeatureVectorClusterMeasure<FV extends IDObject> extends IClusterMeasure<FV> {

}
