package com.github.TKnudsen.ComplexDataObject.model.processors.features;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.TKnudsen.ComplexDataObject.data.features.Feature;
import com.github.TKnudsen.ComplexDataObject.data.features.FeatureTools;
import com.github.TKnudsen.ComplexDataObject.data.features.FeatureType;
import com.github.TKnudsen.ComplexDataObject.data.features.FeatureVectorContainerTools;
import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeatureContainer;
import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeatureTools;
import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeatureVector;
import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.DataProcessingCategory;
import com.github.TKnudsen.ComplexDataObject.model.processors.features.mixedData.IMixedDataFeatureVectorProcessor;

/**
 * <p>
 * Title: Feature
 * </p>
 *
 * <p>
 * Description: two goals:
 * 
 * (1) achieve equal size of all featureVectors
 * 
 * (2) guarantee same order
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 *
 * @author Juergen Bernard
 * @version 1.01
 */
public class IdenticalFeaturesProvider implements IMixedDataFeatureVectorProcessor {

	@Override
	public void process(List<MixedDataFeatureVector> data) {
		Set<String> featureNames = new LinkedHashSet<>();
		Map<String, FeatureType> featureTypes = new HashMap<>();

		for (MixedDataFeatureVector fv : data)
			for (Feature<?> f : fv.getVectorRepresentation())
				featureNames.add(f.getFeatureName());

		for (String featureName : featureNames)
			featureTypes.put(featureName, guessFeatureType(featureName, data));

		List<String> names = new ArrayList<>(featureNames);
		for (MixedDataFeatureVector fv : data) {
			for (int i = 0; i < names.size(); i++) {
				String featureName = names.get(i);
				FeatureType featureType = featureTypes.get(featureName);
				if (fv.sizeOfFeatures() <= i)
					fv.addFeature(MixedDataFeatureTools.convert(FeatureTools.createDefaultFeature(featureName, featureType)));
				else if (fv.getFeature(i) == null || fv.getFeature(i).getFeatureName() == null) {
					fv.removeFeature(i);
					fv.setFeature(i, MixedDataFeatureTools.convert(FeatureTools.createDefaultFeature(featureName, featureType)));
				} else if (!fv.getFeature(i).getFeatureName().equals(featureName)) {
					Feature<?> feature = fv.getFeature(featureName);
					if (feature == null)
						fv.setFeature(i, MixedDataFeatureTools.convert(FeatureTools.createDefaultFeature(featureName, featureType)));
					else
						fv.setFeature(i, fv.removeFeature(featureName));
				}
			}
		}
	}

	@Override
	public void process(MixedDataFeatureContainer container) {
		List<MixedDataFeatureVector> fvs = FeatureVectorContainerTools.getObjectList(container);

		process(fvs);
	}

	/**
	 * guesses the type of a feature, according to the number of occurrences in
	 * a list of featureVectors.
	 * 
	 * @param featureName
	 * @param featureVectors
	 * @return
	 */
	private FeatureType guessFeatureType(String featureName, List<MixedDataFeatureVector> featureVectors) {
		Map<FeatureType, Integer> counts = new HashMap<>();
		for (MixedDataFeatureVector fv : featureVectors)
			if (fv.getFeature(featureName) != null && fv.getFeature(featureName).getFeatureType() != null)
				if (counts.get(fv.getFeature(featureName).getFeatureType()) == null)
					counts.put(fv.getFeature(featureName).getFeatureType(), new Integer(0));
				else
					counts.put(fv.getFeature(featureName).getFeatureType(), counts.get(fv.getFeature(featureName).getFeatureType()) + 1);

		FeatureType ret = null;
		Integer maxCount = 0;
		for (FeatureType featureType : counts.keySet())
			if (counts.get(featureType) > maxCount) {
				maxCount = counts.get(featureType);
				ret = featureType;
			}

		return ret;
	}

	@Override
	public DataProcessingCategory getPreprocessingCategory() {
		return DataProcessingCategory.DATA_CLEANING;
	}
}
