package com.github.TKnudsen.ComplexDataObject.model.transformations.descriptors;

import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeatureVector;
import com.github.TKnudsen.ComplexDataObject.data.interfaces.IDObject;

/**
 * <p>
 * Title: IMixedDataFeatureVectorDescriptor
 * </p>
 * 
 * <p>
 * Description: Basic Interface to transform real-world data (represented as a
 * ComplexDataObject) into the mixed data feature space.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2016-2018
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.03
 */
public interface IMixedDataFeatureVectorDescriptor<I extends IDObject> extends IDescriptor<I, MixedDataFeatureVector> {

}
