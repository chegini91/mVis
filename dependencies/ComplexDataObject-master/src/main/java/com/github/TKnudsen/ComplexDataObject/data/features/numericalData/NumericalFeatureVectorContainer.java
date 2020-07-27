package com.github.TKnudsen.ComplexDataObject.data.features.numericalData;

import java.util.Map;

import com.github.TKnudsen.ComplexDataObject.data.features.FeatureContainer;
import com.github.TKnudsen.ComplexDataObject.data.features.FeatureSchema;

/**
 * <p>
 * Title: NumericalFeatureVectorContainer
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
 * @version 1.02
 */
public class NumericalFeatureVectorContainer extends FeatureContainer<NumericalFeatureVector> {

	public NumericalFeatureVectorContainer(FeatureSchema featureSchema) {
		super(featureSchema);
	}

	public NumericalFeatureVectorContainer(Map<Long, NumericalFeatureVector> featureVectorMap) {
		super(featureVectorMap);
	}

	public NumericalFeatureVectorContainer(Iterable<NumericalFeatureVector> objects) {
		super(objects);
	}
}
