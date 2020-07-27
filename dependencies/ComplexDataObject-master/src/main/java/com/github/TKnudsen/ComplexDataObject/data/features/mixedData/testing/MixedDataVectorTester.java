package com.github.TKnudsen.ComplexDataObject.data.features.mixedData.testing;

import java.util.ArrayList;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.FeatureType;
import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeature;
import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeatureVector;

/**
 * <p>
 * Title:
 * </p>
 *
 * <p>
 * Description:
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2016
 * </p>
 *
 * @author Juergen Bernard
 * @version 1.0
 */

public class MixedDataVectorTester {

	public static void main(String[] args) {

		List<MixedDataFeature> mixedDataFeatures = new ArrayList<>();
		mixedDataFeatures.add(new MixedDataFeature("A", true, FeatureType.BOOLEAN));
		mixedDataFeatures.add(new MixedDataFeature("B", 1.33, FeatureType.DOUBLE));
		mixedDataFeatures.add(new MixedDataFeature("C", "Peter", FeatureType.STRING));

		// mixedDataFeatures.add(new MixedDataFeature("A", false));

		MixedDataFeatureVector vector = new MixedDataFeatureVector(mixedDataFeatures);

		vector.removeFeature("B");
	}

}
