package com.github.TKnudsen.ComplexDataObject.data.features;

import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeature;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeature;

/**
 * <p>
 * Title: FeatureTools
 * </p>
 *
 * <p>
 * Description:
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 *
 * @author Juergen Bernard
 * @version 1.0
 */
public class FeatureTools {

	public static Feature<?> createDefaultFeature(String featureName, FeatureType featureType) {
		switch (featureType) {
		case DOUBLE:
			return new NumericalFeature(featureName, Double.NaN);
		default:
			return new MixedDataFeature(featureName, null, featureType);
		}
	}
}
