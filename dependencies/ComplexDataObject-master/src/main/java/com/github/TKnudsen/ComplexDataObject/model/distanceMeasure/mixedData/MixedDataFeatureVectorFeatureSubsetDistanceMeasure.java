package com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.mixedData;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeature;
import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeatureVector;
import com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.IDistanceMeasure;

/**
 * <p>
 * Title: MixedDataFeatureVectorFeatureSubsetDistanceMeasure
 * </p>
 * 
 * <p>
 * Description: Allows the selection of a set of features for the calculation of
 * distances between MixedDataFeatureVectors.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class MixedDataFeatureVectorFeatureSubsetDistanceMeasure implements IDistanceMeasure<MixedDataFeatureVector> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6369617829261379204L;

	private List<String> featureNames;

	public MixedDataFeatureVectorFeatureSubsetDistanceMeasure(List<String> featureNames) {
		this.featureNames = featureNames;
	}

	@Override
	public String getName() {
		return "Calculates distances on the basis of a subset of the features";
	}

	@Override
	public String getDescription() {
		return getName();
	}

	@Override
	public String toString() {
		return getDescription();
	}

	@Override
	public double getDistance(MixedDataFeatureVector o1, MixedDataFeatureVector o2) {
		if (o1 == null || o2 == null)
			return Double.NaN;

		double dist = 0;

		for (String string : featureNames) {
			MixedDataFeature e1 = o1.getFeature(string);
			MixedDataFeature e2 = o2.getFeature(string);
			if (e1 == null || e2 == null)
				dist += 1;
			else if (e1.getFeatureValue() != e2.getFeatureValue())
				dist += 1;
		}

		return dist;
	}

	@Override
	public double applyAsDouble(MixedDataFeatureVector t, MixedDataFeatureVector u) {
		return getDistance(t, u);
	}

	public List<String> getFeatureNames() {
		return featureNames;
	}

	public void setFeatureNames(List<String> featureNames) {
		this.featureNames = featureNames;
	}
}
