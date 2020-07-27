package com.github.TKnudsen.ComplexDataObject.model.transformations.mergeAndJoin.features.mixedData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.TKnudsen.ComplexDataObject.data.features.Feature;
import com.github.TKnudsen.ComplexDataObject.data.features.FeatureType;
import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeature;
import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeatureVector;
import com.github.TKnudsen.ComplexDataObject.model.transformations.mergeAndJoin.IObjectMerger;
import com.github.TKnudsen.ComplexDataObject.model.transformations.mergeAndJoin.Boolean.BooleanEqualsFunction;
import com.github.TKnudsen.ComplexDataObject.model.transformations.mergeAndJoin.Double.SubtractionFunction;
import com.github.TKnudsen.ComplexDataObject.model.transformations.mergeAndJoin.string.BigramGenerator;

/**
 * <p>
 * Title: MixedDataFeatureDeltaCreator
 * </p>
 * 
 * <p>
 * Description: Creates a so-called 'delta-feature vector'. The value domains of
 * two MixedDataFeatures are differentiated for every feature. The resulting
 * values define a new deltaFV.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class MixedDataFeatureDeltaCreator implements IObjectMerger<MixedDataFeatureVector> {

	private Map<FeatureType, IObjectMerger<?>> deltaFunctions;

	private IObjectMerger<Boolean> deltaFunctionsBoolean = new BooleanEqualsFunction();
	private IObjectMerger<Double> deltaFunctionsDouble = new SubtractionFunction();
	private IObjectMerger<String> deltaFunctionsString = new BigramGenerator("->");

	public MixedDataFeatureDeltaCreator() {
		initializeMeregeFunctions();
	}

	private void initializeMeregeFunctions() {
		deltaFunctions = new HashMap<>();
		deltaFunctions.put(FeatureType.BOOLEAN, new BooleanEqualsFunction());
		deltaFunctions.put(FeatureType.DOUBLE, new SubtractionFunction());
		deltaFunctions.put(FeatureType.STRING, new BigramGenerator("->"));
	}

	@Override
	public MixedDataFeatureVector merge(MixedDataFeatureVector object1, MixedDataFeatureVector object2) {
		if (object1 == null && object2 == null)
			return null;

		List<MixedDataFeature> features = new ArrayList<>();

		Set<String> attributes = new LinkedHashSet<>();

		if (object1 != null)
			attributes.addAll(object1.getFeatureKeySet());

		if (object2 != null)
			attributes.addAll(object2.getFeatureKeySet());

		for (String attribute : attributes) {
			MixedDataFeature feature = null;
			if (object1 != null)
				feature = object1.getFeature(attribute);
			if (feature == null && object2 != null)
				feature = object2.getFeature(attribute);

			if (feature != null) {
				Feature<?> featureO1 = null;
				if (object1 != null)
					featureO1 = object1.getFeature(attribute);

				Feature<?> featureO2 = null;
				if (object2 != null)
					featureO2 = object2.getFeature(attribute);

				Object v1 = null;
				if (featureO1 != null)
					v1 = featureO1.getFeatureValue();

				Object v2 = null;
				if (featureO2 != null)
					v2 = featureO2.getFeatureValue();

				FeatureType featureType = feature.getFeatureType();
				MixedDataFeature mixedDataFeature = new MixedDataFeature(attribute, null, featureType);

				switch (featureType) {
				case BOOLEAN:
					mixedDataFeature.setFeatureValue(deltaFunctionsBoolean.merge((boolean) v1, (boolean) v2));
					break;
				case DOUBLE:
					mixedDataFeature.setFeatureValue(deltaFunctionsDouble.merge((double) v1, (double) v2));
					break;
				case STRING:
					mixedDataFeature.setFeatureValue(deltaFunctionsString.merge((String) v1, (String) v2));
					break;
				default:
					throw new IllegalArgumentException("MixedDataFeatureDeltaCreator: problems with unknown feature type");
				}

				features.add(mixedDataFeature);
			}
		}

		MixedDataFeatureVector mixedDataFeatureVector = new MixedDataFeatureVector(features);

		return mixedDataFeatureVector;
	}
}
