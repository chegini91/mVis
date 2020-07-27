package com.github.TKnudsen.ComplexDataObject.data.features.mixedData;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import com.github.TKnudsen.ComplexDataObject.data.features.AbstractFeatureVector;
import com.github.TKnudsen.ComplexDataObject.data.features.Feature;
import com.github.TKnudsen.ComplexDataObject.data.features.FeatureType;

/**
 * <p>
 * Title: MixedDataVector
 * </p>
 *
 * <p>
 * Description: AlgorithmDataObject representation for mixed data involving
 * Double, String, and Boolean. Used for objects containing numerical,
 * categorical, and binary attributes.
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2015-2016
 * </p>
 *
 * @author Juergen Bernard
 * @version 1.0
 */

public class MixedDataFeatureVector extends AbstractFeatureVector<Object, MixedDataFeature> {

	public MixedDataFeatureVector(List<MixedDataFeature> features) {
		super(features);
	}

	public MixedDataFeatureVector(MixedDataFeature[] features) {
		super(features);
	}

	public MixedDataFeatureVector(SortedMap<String, MixedDataFeature> featuresMap) {
		super(featuresMap);
	}

	@Override
	public MixedDataFeatureVector subTuple(int fromIndex, int toIndex) {
		if (featuresList != null)
			return new MixedDataFeatureVector(featuresList.subList(fromIndex, toIndex));
		return null;
	}

	@Override
	public void addFeature(String featureName, Object value, FeatureType type) {
		addFeature(new MixedDataFeature(featureName, value, type));
	}

	@Override
	public MixedDataFeatureVector clone() {
		List<MixedDataFeature> features = new ArrayList<>();
		for (MixedDataFeature f : featuresList)
			features.add(f.clone());

		MixedDataFeatureVector clone = new MixedDataFeatureVector(features);

		// attributes and meta information
		clone.setMaster(getMaster());
		for (String s : attributes.keySet())
			clone.add(s, getAttribute(s));

		// name and description
		clone.setName(getName());
		clone.setDescription(getDescription());

		return clone;
	}

	@Override
	public String toString() {
		String output = "";
		for (Feature<?> f : featuresList)
			if (f != null && f.getFeatureValue() != null)
				output += (f.getFeatureName() + ": " + f.getFeatureValue().toString() + "\n");
		return output;
	}

	/**
	 * retrieves the featureNames of a given FeatureType.
	 * 
	 * @param featureType
	 * @return
	 */
	public Iterable<String> getFeatureNames(FeatureType featureType) {
		List<String> featureNames = new ArrayList<>();

		for (Feature<?> f : featuresList)
			if (f != null)
				if (f.getFeatureType() != null)
					if (f.getFeatureType().equals(featureType))
						featureNames.add(f.getFeatureName());

		return new ArrayList<>(featureNames);
	}
}
