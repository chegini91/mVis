package com.github.TKnudsen.ComplexDataObject.data.features.mixedData;

import java.util.ArrayList;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.Feature;
import com.github.TKnudsen.ComplexDataObject.data.features.FeatureType;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeature;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;

public class MixedDataFeatureTools {

	public static FeatureType guessFeatureType(Object feature) throws IllegalArgumentException {
		if (feature == null)
			throw new IllegalArgumentException("MixedDataFeatureTools.guessFeatureType: object was null - unable to guess");
		if (feature instanceof Number)
			return FeatureType.DOUBLE;
		else if (feature instanceof Boolean)
			return FeatureType.BOOLEAN;
		else if (feature instanceof String)
			return FeatureType.STRING;
		else if (feature instanceof Character)
			return FeatureType.STRING;
		throw new IllegalArgumentException("MixedDataFeatureTools.guessFeatureType: undefined object type");
	}

	public static void addClassAttribute(List<MixedDataFeatureVector> featureVectors, List<String> labels, String classAttribute) {
		for (int i = 0; i < featureVectors.size(); i++)
			featureVectors.get(i).add(classAttribute, labels.get(i));
	}

	public static void addNumericAttribute(List<MixedDataFeatureVector> features, List<Double> labels, String attributeName) {
		for (int i = 0; i < features.size(); i++)
			features.get(i).add(attributeName, labels.get(i));
	}

	/**
	 * converts a NumericalFeatureVector into a MixedDataFeatureVector
	 * 
	 * @param fv
	 * @return
	 */
	public static MixedDataFeatureVector convert(NumericalFeatureVector fv) {
		List<MixedDataFeature> features = new ArrayList<>();
		for (String featureName : fv.getFeatureKeySet()) {
			NumericalFeature feature = fv.getFeature(featureName);
			if (feature == null)
				features.add(new MixedDataFeature(featureName, Double.NaN, FeatureType.DOUBLE));
			else
				features.add(new MixedDataFeature(featureName, feature.getFeatureValue(), FeatureType.DOUBLE));
		}

		return new MixedDataFeatureVector(features);
	}

	public static MixedDataFeature convert(Feature<?> feature) {
		if (feature == null)
			return null;

		if (feature instanceof MixedDataFeature)
			return (MixedDataFeature) feature;

		return new MixedDataFeature(feature.getFeatureName(), feature.getFeatureValue(), feature.getFeatureType());
	}
}
