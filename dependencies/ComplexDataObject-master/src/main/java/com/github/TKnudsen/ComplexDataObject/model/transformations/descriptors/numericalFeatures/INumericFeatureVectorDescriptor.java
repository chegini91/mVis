package com.github.TKnudsen.ComplexDataObject.model.transformations.descriptors.numericalFeatures;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.model.transformations.descriptors.IDescriptor;

/**
 * <p>
 * Title: INumericFeatureVectorDescriptor
 * </p>
 * 
 * <p>
 * Description: Basic Interface to transform real-world data (represented as a
 * ComplexDataObject) into numerical feature spaces.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2016-2018
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.04
 */
public interface INumericFeatureVectorDescriptor<I> extends IDescriptor<I, NumericalFeatureVector> {

}
