package com.github.TKnudsen.ComplexDataObject.model.processors.features.mixedData;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeature;
import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeatureContainer;
import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeatureVector;
import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.DataProcessingCategory;

/**
 * <p>
 * Title: ImplausibleValueRemover
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
public class ImplausibleValueRemover<T extends Object> implements IMixedDataFeatureVectorProcessor {

	private String featureName;

	private List<T> implausibleValues;
	private T neutralElement;

	public ImplausibleValueRemover(String featureName, List<T> implausibleValues, T neutralElement) {
		this.featureName = featureName;
		this.implausibleValues = implausibleValues;
		this.neutralElement = neutralElement;
	}

	@Override
	public void process(MixedDataFeatureContainer container) {
		for (MixedDataFeatureVector object : container)
			if (object.getFeature(featureName) != null) {
				MixedDataFeature feature = object.getFeature(featureName);
				if (implausibleValues.contains(feature.getFeatureValue()))
					feature.setFeatureValue(neutralElement);
			}
	}

	@Override
	public void process(List<MixedDataFeatureVector> data) {
		process(new MixedDataFeatureContainer(data));
	}

	@Override
	public DataProcessingCategory getPreprocessingCategory() {
		return DataProcessingCategory.DATA_CLEANING;
	}

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public List<T> getImplausibleValues() {
		return implausibleValues;
	}

	public void setImplausibleValues(List<T> implausibleValues) {
		this.implausibleValues = implausibleValues;
	}

	public T getNeutralElement() {
		return neutralElement;
	}

	public void setNeutralElement(T neutralElement) {
		this.neutralElement = neutralElement;
	}
}
