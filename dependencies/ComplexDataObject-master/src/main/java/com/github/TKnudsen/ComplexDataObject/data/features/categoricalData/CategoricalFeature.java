package com.github.TKnudsen.ComplexDataObject.data.features.categoricalData;

import com.github.TKnudsen.ComplexDataObject.data.features.Feature;
import com.github.TKnudsen.ComplexDataObject.data.features.FeatureType;

/**
 * <p>
 * Title: CategoricalFeature
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

public class CategoricalFeature extends Feature<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2550677654103245531L;

	@SuppressWarnings("unused")
	private CategoricalFeature() {
		super(FeatureType.STRING);
	}

	public CategoricalFeature(String featureName, String featureValue) {
		super(featureName, featureValue, FeatureType.STRING);
	}

	@Override
	public CategoricalFeature clone() {
		return new CategoricalFeature(featureName, featureValue);
	}

}
