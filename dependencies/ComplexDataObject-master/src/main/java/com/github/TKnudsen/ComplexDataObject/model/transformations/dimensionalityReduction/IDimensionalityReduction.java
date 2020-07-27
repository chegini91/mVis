package com.github.TKnudsen.ComplexDataObject.model.transformations.dimensionalityReduction;

import java.util.Map;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.model.transformations.IDataTransformation;

/**
 * <p>
 * Title: IDimensionalityReduction
 * </p>
 * 
 * <p>
 * Description:
 * 
 * <p>
 * Copyright: Copyright (c) 2012-2018 Juergen Bernard,
 * https://github.com/TKnudsen/ComplexDataObject
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.04
 * 
 */
public interface IDimensionalityReduction<X> extends IDataTransformation<X, NumericalFeatureVector> {

	public int getOutputDimensionality();

	public void calculateDimensionalityReduction();

	public Map<X, NumericalFeatureVector> getMapping();
}