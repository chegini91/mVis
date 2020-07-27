package com.github.TKnudsen.ComplexDataObject.model.transformations.descriptors.numericalFeatures;

import java.util.ArrayList;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeature;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.data.interfaces.IDObject;
import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.DataTransformationCategory;
import com.github.TKnudsen.ComplexDataObject.model.transformations.descriptors.IDescriptor;
import com.github.TKnudsen.ComplexDataObject.model.transformations.featureExtraction.IFeatureExtractor;

/**
 * <p>
 * Title: StatisticalFeaturesExtractorDescriptor
 * </p>
 * 
 * <p>
 * Description: Baseline implementation for a statistical descriptor. Uses
 * IFeatureExtractors for building a set of features.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2016-2017
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 */
public class StatisticalFeaturesExtractorDescriptor<T extends IDObject> implements INumericFeatureVectorDescriptor<T> {

	List<IFeatureExtractor<T, NumericalFeature>> featureExtractors = new ArrayList<>();

	public StatisticalFeaturesExtractorDescriptor() {
		featureExtractors = null;
	}

	public StatisticalFeaturesExtractorDescriptor(IFeatureExtractor<T, NumericalFeature> featureExtractor) {
		this.featureExtractors.add(featureExtractor);
	}

	public StatisticalFeaturesExtractorDescriptor(List<IFeatureExtractor<T, NumericalFeature>> featureExtractors) {
		this.featureExtractors.addAll(featureExtractors);
	}

	@Override
	public String getName() {
		return "Statistical Features Extractor Descriptor";
	}

	@Override
	public String getDescription() {
		return "Extracts features from a given object using pre-defined feature extractors";
	}

	@Override
	public List<IDescriptor<T, NumericalFeatureVector>> getAlternativeParameterizations(int count) {
		return null;
	}

	@Override
	public List<NumericalFeatureVector> transform(T input) {
		if (featureExtractors == null)
			throw new IllegalArgumentException(getName() + ": featureExtractors null");

		List<NumericalFeature> features = new ArrayList<>();

		for (IFeatureExtractor<T, NumericalFeature> featureExtractor : featureExtractors)
			features.addAll(featureExtractor.transform(input));

		NumericalFeatureVector numericalFeatureVector = new NumericalFeatureVector(features);
		numericalFeatureVector.setMaster(input);

		List<NumericalFeatureVector> numericalFeatureVectors = new ArrayList<>();
		numericalFeatureVectors.add(numericalFeatureVector);

		return numericalFeatureVectors;
	}

	@Override
	public List<NumericalFeatureVector> transform(List<T> inputObjects) {
		List<NumericalFeatureVector> numericalFeatureVectors = new ArrayList<>();

		for (T t : inputObjects)
			numericalFeatureVectors.addAll(transform(t));

		return numericalFeatureVectors;
	}

	@Override
	public DataTransformationCategory getDataTransformationCategory() {
		return DataTransformationCategory.DESCRIPTOR;
	}

	public void addFeatureExtractor(IFeatureExtractor<T, NumericalFeature> featureExtractor) {
		if (featureExtractors == null)
			featureExtractors = new ArrayList<>();

		featureExtractors.add(featureExtractor);
	}

}
