package com.github.TKnudsen.ComplexDataObject.data.features.numericalData;

import com.github.TKnudsen.ComplexDataObject.data.features.Feature;
import com.github.TKnudsen.ComplexDataObject.data.features.FeatureType;

/**
 * <p>
 * Title: NumericalFeature
 * </p>
 *
 * <p>
 * Description:
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2016-2017
 * </p>
 *
 * @author Juergen Bernard
 * @version 1.02
 */

public class NumericalFeature extends Feature<Double> {

	/**
	 *
	 */
	private static final long serialVersionUID = 7244765037515290604L;

	private NumericalFeature() {
		super(FeatureType.DOUBLE);
	}

	public NumericalFeature(String featureName, Double featureValue) {
		super(featureName, featureValue, FeatureType.DOUBLE);
	}

	@Override
	public String toString() {
		if (featureName != null)
			return featureName + ", " + featureValue + " (" + featureType.name() + ") ";
		return featureValue + " (" + featureType.name() + ") ";
	}

	public double doubleValue() {
		return featureValue.doubleValue();
	}

	@Override
	public NumericalFeature clone() {
		return new NumericalFeature(featureName, featureValue);
	}
}
