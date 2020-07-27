package com.github.TKnudsen.ComplexDataObject.model.transformations.dimensionalityReduction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.IDistanceMeasure;
import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.DataTransformationCategory;

/**
 * <p>
 * Title: DimensionalityReduction
 * </p>
 * 
 * <p>
 * Description: baseline for dimensionality reduction algorithms. Maintains
 * generalizable data structures.
 * 
 * <p>
 * Copyright: Copyright (c) 2012-2018 Juergen Bernard,
 * https://github.com/TKnudsen/ComplexDataObject
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.03
 */
public abstract class DimensionalityReduction<X> implements IDimensionalityReduction<X> {

	/**
	 * used by many routines to calculate pairwise distances
	 */
	protected IDistanceMeasure<X> distanceMeasure;

	/**
	 * the dimensionality of the manifold to be learned
	 */
	protected int outputDimensionality;

	protected Map<X, NumericalFeatureVector> mapping;

	@Override
	public DataTransformationCategory getDataTransformationCategory() {
		return DataTransformationCategory.DIMENSION_REDUCTION;
	}

	@Override
	public Map<X, NumericalFeatureVector> getMapping() {
		return mapping;
	}

	@Override
	public List<NumericalFeatureVector> transform(X inputObject) {
		if (mapping == null)
			throw new NullPointerException("mapping is null: dimensionality-reduction model not calculated yet?");

		if (mapping.get(inputObject) != null)
			return Arrays.asList(new NumericalFeatureVector[] { mapping.get(inputObject) });

		System.err.println(
				"DimensionalityRedutcion: feature vector identified that was not used for model building. null value added.");

		List<X> lst = new ArrayList<>();
		lst.add(inputObject);
		return transform(lst);
	}

	/**
	 * Uses the model to tranform given data to the mapped space. Does not build a
	 * new model!
	 */
	@Override
	public List<NumericalFeatureVector> transform(List<X> inputObjects) {
		if (inputObjects == null)
			throw new NullPointerException("DimensionalityReduction: input objects must not be null");

		if (inputObjects.size() == 0)
			throw new IllegalArgumentException("DimensionalityReduction: input objects' size was 0");

		if (mapping == null)
			throw new NullPointerException("mapping is null: dimensionality-reduction model not calculated yet?");

		List<NumericalFeatureVector> output = new ArrayList<>();
		for (X x : inputObjects)
			if (mapping.containsKey(x))
				output.add(mapping.get(x));
			else {
				output.add(null);
				System.err.println(
						"DimensionalityRedutcion: input object identified that was not used for model building. null value added.");
			}

		return output;
	}

	@Override
	public int getOutputDimensionality() {
		return outputDimensionality;
	}

	public void setOutputDimensionality(int outputDimensionality) {
		this.outputDimensionality = outputDimensionality;

		this.mapping = null;
	}

}
