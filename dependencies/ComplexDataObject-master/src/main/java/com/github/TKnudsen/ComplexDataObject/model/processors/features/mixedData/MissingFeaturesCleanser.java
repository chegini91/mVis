package com.github.TKnudsen.ComplexDataObject.model.processors.features.mixedData;

import java.util.ArrayList;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.FeatureType;
import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeature;
import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeatureContainer;
import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeatureVector;
import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.DataProcessingCategory;

/**
 * <p>
 * Title: MissingFeaturesCleanser
 * </p>
 * 
 * <p>
 * Description: guarantees that every mixed feature vector has the same
 * features/dimensions. missing features/dimensions are added and filled with
 * missing value indicators.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 */
public class MissingFeaturesCleanser implements IMixedDataFeatureVectorProcessor {

	private String missingStringValueIndicator = "";
	private Double missingDoubleValueIndicator = Double.NaN;

	public MissingFeaturesCleanser(String missingStringValueIndicator, Double missingDoubleValueIndicator) {
		this.missingStringValueIndicator = missingStringValueIndicator;
		this.missingDoubleValueIndicator = missingDoubleValueIndicator;
	}

	@Override
	public void process(MixedDataFeatureContainer container) {
		for (MixedDataFeatureVector fv : container)
			for (String featureName : container.getFeatureNames())
				if (fv.getFeature(featureName) == null) {
					if (container.isNumeric(featureName))
						fv.addFeature(new MixedDataFeature(featureName, missingDoubleValueIndicator, FeatureType.DOUBLE));
					else if (container.isBoolean(featureName))
						fv.addFeature(new MixedDataFeature(featureName, missingStringValueIndicator, FeatureType.BOOLEAN));
					else
						fv.addFeature(new MixedDataFeature(featureName, missingStringValueIndicator, FeatureType.STRING));
				}
	}

	@Override
	public void process(List<MixedDataFeatureVector> data) {
		process(new MixedDataFeatureContainer(data));

//		Map<String, Object> missingValueIndicators = new HashMap<>();
//
//		List<String> featureSchema = getFeatureSchema(data);
//
//		for (MixedDataFeatureVector fv : data)
//			for (String featureName : fv.getFeatureKeySet())
//				if (missingValueIndicators.get(featureName) == null) {
//					MixedDataFeature feature = fv.getFeature(featureName);
//					if (feature.getFeatureType().equals(FeatureType.DOUBLE))
//						missingValueIndicators.put(featureName, missingDoubleValueIndicator);
//					else
//						missingValueIndicators.put(featureName, missingStringValueIndicator);
//				}
//
//		for (MixedDataFeatureVector fv : data) {
//			Set<String> featureKeySet = fv.getFeatureKeySet();
//			for (String featureName : missingValueIndicators.keySet())
//				if (!featureKeySet.contains(featureName)) {
//					FeatureType featureType = MixedDataFeatureTools.guessFeatureType(missingValueIndicators.get(featureName));
//					fv.addFeature(new MixedDataFeature(featureName, missingValueIndicators.get(featureName), featureType));
//				}
//		}
	}

	public List<String> getFeatureSchema(List<MixedDataFeatureVector> data) {
		if (data == null || data.size() == 0)
			return null;

		List<String> schema = new ArrayList<>();

		for (MixedDataFeatureVector fv : data)
			for (String featureName : fv.getFeatureKeySet())
				if (schema.contains(featureName))
					schema.add(featureName);

		return schema;
	}

	@Override
	public DataProcessingCategory getPreprocessingCategory() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getMissingStringValueIndicator() {
		return missingStringValueIndicator;
	}

	public void setMissingStringValueIndicator(String missingStringValueIndicator) {
		this.missingStringValueIndicator = missingStringValueIndicator;
	}

	public Double getMissingDoubleValueIndicator() {
		return missingDoubleValueIndicator;
	}

	public void setMissingDoubleValueIndicator(Double missingDoubleValueIndicator) {
		this.missingDoubleValueIndicator = missingDoubleValueIndicator;
	}
}
