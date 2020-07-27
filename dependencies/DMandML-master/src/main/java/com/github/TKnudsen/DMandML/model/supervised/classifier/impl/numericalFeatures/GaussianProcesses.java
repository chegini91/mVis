package com.github.TKnudsen.DMandML.model.supervised.classifier.impl.numericalFeatures;

import java.util.ArrayList;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.DMandML.model.supervised.classifier.WekaClassifierWrapper;

/**
 * <p>
 * Title: GaussianProcesses
 * </p>
 * 
 * <p>
 * Description: Gaussian processes for regression without hyperparameter-tuning.
 * Cannot handle multi-valued nominal class.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class GaussianProcesses extends WekaClassifierWrapper<NumericalFeatureVector> {

	/**
	 * Level of Gaussian Noise wrt transformed target. (default 1)
	 */
	private double levelOfGaussianNoise = 1.0;

	/**
	 * Whether to 0=normalize/1=standardize/2=neither.(default 0=normalize)
	 */
	private int normalizeType = 0;

	/**
	 * Random number seed.*(default 1)
	 */
	private int seed = 1;

	public GaussianProcesses() {

	}

	public GaussianProcesses(double levelOfGaussianNoise, int normalizeType, int seed) {
		this.levelOfGaussianNoise = levelOfGaussianNoise;
		this.normalizeType = normalizeType;
		this.seed = seed;
	}

	@Override
	protected void initializeClassifier() {
		setWekaClassifier(new weka.classifiers.functions.GaussianProcesses());

		List<String> optionsList = new ArrayList<String>();

		optionsList.add("-L");
		optionsList.add(levelOfGaussianNoise + "");

		optionsList.add("-N");
		optionsList.add(normalizeType + "");

		optionsList.add("-S");
		optionsList.add(seed + "");

		String[] options = optionsList.toArray(new String[optionsList.size()]);

		try {
			((weka.classifiers.functions.GaussianProcesses) getWekaClassifier()).setOptions(options);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public double getLevelOfGaussianNoise() {
		return levelOfGaussianNoise;
	}

	public void setLevelOfGaussianNoise(double levelOfGaussianNoise) {
		this.levelOfGaussianNoise = levelOfGaussianNoise;

		initializeClassifier();
	}

	public int getNormalizeType() {
		return normalizeType;
	}

	public void setNormalizeType(int normalizeType) {
		this.normalizeType = normalizeType;

		initializeClassifier();
	}

	public int getSeed() {
		return seed;
	}

	public void setSeed(int seed) {
		this.seed = seed;

		initializeClassifier();
	}

}
