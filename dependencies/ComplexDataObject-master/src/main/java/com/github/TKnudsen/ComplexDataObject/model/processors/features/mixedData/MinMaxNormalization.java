package com.github.TKnudsen.ComplexDataObject.model.processors.features.mixedData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.TKnudsen.ComplexDataObject.data.features.FeatureType;
import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeature;
import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeatureContainer;
import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeatureVector;
import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.DataProcessingCategory;
import com.github.TKnudsen.ComplexDataObject.model.tools.MathFunctions;

/**
 * <p>
 * Title: MinMaxNormalization
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
public class MinMaxNormalization implements IMixedDataFeatureVectorProcessor {

	public MinMaxNormalization() {
	}

	@Override
	public void process(MixedDataFeatureContainer container) {

		// retrieve numerical dimensions, store min and max
		Map<String, Double> minValues = new HashMap<>();
		Map<String, Double> maxValues = new HashMap<>();

		for (MixedDataFeatureVector fv : container)
			for (String featureName : fv.getFeatureKeySet()) {
				if (!fv.getFeature(featureName).getFeatureType().equals(FeatureType.DOUBLE))
					continue;
				if (minValues.get(featureName) == null)
					minValues.put(featureName, Double.POSITIVE_INFINITY);
				if (maxValues.get(featureName) == null)
					maxValues.put(featureName, Double.NEGATIVE_INFINITY);

				MixedDataFeature feature = fv.getFeature(featureName);
				Number n = (Number) feature.getFeatureValue();
				if (Double.isNaN(n.doubleValue()))
					continue;

				minValues.put(featureName, Math.min(minValues.get(featureName), n.doubleValue()));
				maxValues.put(featureName, Math.max(maxValues.get(featureName), n.doubleValue()));
			}

		for (MixedDataFeatureVector fv : container)
			for (String featureName : fv.getFeatureKeySet()) {
				if (!fv.getFeature(featureName).getFeatureType().equals(FeatureType.DOUBLE))
					continue;

				MixedDataFeature feature = fv.getFeature(featureName);
				Number n = (Number) feature.getFeatureValue();
				feature.setFeatureValue(MathFunctions.linearScale(minValues.get(featureName), maxValues.get(featureName), n.doubleValue()));
			}
	}

	@Override
	public void process(List<MixedDataFeatureVector> data) {
//		process(new MixedDataFeatureContainer(data));
		// retrieve numerical dimensions, store min and max
				Map<String, Double> minValues = new HashMap<>();
				Map<String, Double> maxValues = new HashMap<>();

				for (MixedDataFeatureVector fv : data)
					for (String featureName : fv.getFeatureKeySet()) {
						if (!fv.getFeature(featureName).getFeatureType().equals(FeatureType.DOUBLE))
							continue;
						if (minValues.get(featureName) == null)
							minValues.put(featureName, Double.POSITIVE_INFINITY);
						if (maxValues.get(featureName) == null)
							maxValues.put(featureName, Double.NEGATIVE_INFINITY);

						MixedDataFeature feature = fv.getFeature(featureName);
						Number n = (Number) feature.getFeatureValue();
						if (Double.isNaN(n.doubleValue()))
							continue;

						minValues.put(featureName, Math.min(minValues.get(featureName), n.doubleValue()));
						maxValues.put(featureName, Math.max(maxValues.get(featureName), n.doubleValue()));
					}

				for (MixedDataFeatureVector fv : data)
					for (String featureName : fv.getFeatureKeySet()) {
						if (!fv.getFeature(featureName).getFeatureType().equals(FeatureType.DOUBLE))
							continue;

						MixedDataFeature feature = fv.getFeature(featureName);
						Number n = (Number) feature.getFeatureValue();
						feature.setFeatureValue(MathFunctions.linearScale(minValues.get(featureName), maxValues.get(featureName), n.doubleValue()));
					}
	}

	@Override
	public DataProcessingCategory getPreprocessingCategory() {
		return DataProcessingCategory.DATA_NORMALIZATION;
	}
}