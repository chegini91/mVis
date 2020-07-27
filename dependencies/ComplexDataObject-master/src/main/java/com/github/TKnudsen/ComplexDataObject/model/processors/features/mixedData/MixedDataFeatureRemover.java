package com.github.TKnudsen.ComplexDataObject.model.processors.features.mixedData;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeatureContainer;
import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeatureVector;
import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.DataProcessingCategory;

/**
 * <p>
 * Title: FeatureRemover
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
 * @version 1.01
 */
public class MixedDataFeatureRemover implements IMixedDataFeatureVectorProcessor {

	private String featureName;

	public MixedDataFeatureRemover(String featureName) {
		this.featureName = featureName;
	}

	@Override
	public void process(MixedDataFeatureContainer container) {
		container.remove(featureName);
	}

	@Override
	public void process(List<MixedDataFeatureVector> data) {
		for (MixedDataFeatureVector object : data)
			object.removeFeature(featureName);
	}

	@Override
	public DataProcessingCategory getPreprocessingCategory() {
		return DataProcessingCategory.DATA_REDUCTION;
	}

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}
}
