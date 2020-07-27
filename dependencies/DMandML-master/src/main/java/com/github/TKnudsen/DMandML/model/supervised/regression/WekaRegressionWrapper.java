package com.github.TKnudsen.DMandML.model.supervised.regression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.IFeatureVectorObject;
import com.github.TKnudsen.ComplexDataObject.model.tools.WekaConversion;

import weka.classifiers.AbstractClassifier;
import weka.core.Attribute;
import weka.core.Instances;

/**
 * <p>
 * Title: WekaRegressionWrapper
 * </p>
 * 
 * <p>
 * Description: basic algorithmic model that wraps regression models from Weka.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard, Christian Ritter
 * @version 1.04
 * 
 */
public abstract class WekaRegressionWrapper<FV extends IFeatureVectorObject<?, ?>> extends Regression<FV> {

	protected AbstractClassifier wekaRegressionModel;

	protected Instances instances;

	private Map<String, Set<String>> featureAlphabets;

	public WekaRegressionWrapper() {
		featureAlphabets = new HashMap<>();
	}

	@Override
	protected void prepareData() {
		if (getTargetValues() != null)
			instances = WekaConversion.getRegressionValueInstances(trainFeatureVectors, getTargetValues(),
					featureAlphabets);
		else if (getTargetAttribute() != null) {
			instances = WekaConversion.getInstances(trainFeatureVectors, false);

			Attribute attribute = instances.attribute(getTargetAttribute());

			instances.setClass(attribute);
			System.out.println(instances.classIndex());
		} else
			throw new NullPointerException("WekaRegressionWrapper: no target variable defined.");
	}

	@Override
	protected void buildRegression() {
		if (instances == null)
			throw new NullPointerException("need to initialize data instances first.");

		try {
			wekaRegressionModel.buildClassifier(instances);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void resetResults() {
		wekaRegressionModel = null;
	}

	// @Override
	// public void train(List<NumericalFeatureVector> featureVectors,
	// List<Double> labels) {
	// setTrainFeatureVectors(featureVectors);
	//
	// instances = WekaConversion.getLabeledInstances(featureVectors, labels);
	//
	// buildRegressionModel();
	// }
	//
	// @Override
	// public void train(List<NumericalFeatureVector> featureVectors, String
	// targetVariable) {
	// setTrainFeatureVectors(featureVectors);
	//
	// Attribute attribute = instances.attribute(targetVariable);
	//
	// instances.setClass(attribute);
	// System.out.println(instances.classIndex());
	//
	// buildRegressionModel();
	// }

	@Override
	public List<Double> test(List<FV> featureVectors) {

		Instances testData = WekaConversion.getInstances(featureVectors, false);
		if (testData.size() != featureVectors.size())
			throw new IllegalArgumentException("WekaConversion failed");

		testData = WekaConversion.addLabelAttributeToInstance(testData, "class", null);
		if (testData.size() != testData.size())
			throw new IllegalArgumentException("WekaConversion failed");

		List<Double> values = new ArrayList<>();

		for (int i = 0; i < testData.size(); i++)
			try {
				values.add(wekaRegressionModel.classifyInstance(testData.get(i)));
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		return values;
	}

	public void putFeatureAlphabet(String featureName, Set<String> alphabet) {
		featureAlphabets.put(featureName, alphabet);
	}

	public void setFeatureAlphabets(Map<String, Set<String>> featureAlphabets) {
		this.featureAlphabets = featureAlphabets;
	}
}
