package com.github.TKnudsen.DMandML.model.supervised.classifier.impl.numericalFeatures;

import java.util.ArrayList;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.DMandML.model.supervised.classifier.WekaClassifierWrapper;

/**
 * <p>
 * Title: RandomForest
 * </p>
 * 
 * <p>
 * Description: RandomForest implementation for numerical features. Based on
 * WEKA's RandomForest.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2017-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.03
 */
public class RandomForest extends WekaClassifierWrapper<NumericalFeatureVector> {

	/**
	 * Size of each bag, as a percentage of the training set size. (default 100)
	 */
	private int sizeOfEachBagInPercent = 100;

	/**
	 * 
	 */
	private int iterations = 100;

	/**
	 * number of randomly chosen attributes. If 0, int(log_2(#predictors) + 1) is
	 * used.
	 */
	private int numFeatures = 0;

	/**
	 * Set minimum number of instances per leaf.
	 */
	private int minInstancesPerLeaf = 1;

	/**
	 * minimum numeric class variance proportion of train variance for split
	 */
	private double minClassVariance = 0.001;

	/**
	 * maximum depth of the tree, 0 for unlimited.
	 */
	private int depthMax = 0;

	public RandomForest() {

	}

	@Override
	protected void initializeClassifier() {
		setWekaClassifier(new weka.classifiers.trees.RandomForest());

		List<String> optionsList = new ArrayList<String>();

		optionsList.add("-P");
		optionsList.add(sizeOfEachBagInPercent + "");

		optionsList.add("-I");
		optionsList.add(iterations + "");

		optionsList.add("-K");
		optionsList.add(numFeatures + "");

		optionsList.add("-M");
		optionsList.add(minInstancesPerLeaf + "");

		optionsList.add("-V");
		optionsList.add(minClassVariance + "");

		optionsList.add("-S");
		optionsList.add(1 + "");

		optionsList.add("-depth");
		optionsList.add(depthMax + "");

		String[] options = optionsList.toArray(new String[optionsList.size()]);

		try {
			((weka.classifiers.trees.RandomForest) getWekaClassifier()).setOptions(options);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getNumFeatures() {
		return numFeatures;
	}

	public void setNumFeatures(int numFeatures) {
		this.numFeatures = numFeatures;

		initializeClassifier();
	}

	public int getDepthMax() {
		return depthMax;
	}

	public void setDepthMax(int depthMax) {
		this.depthMax = depthMax;

		initializeClassifier();
	}

	public double getSizeOfEachBagInPercent() {
		return sizeOfEachBagInPercent;
	}

	public void setSizeOfEachBagInPercent(int sizeOfEachBagInPercent) {
		this.sizeOfEachBagInPercent = sizeOfEachBagInPercent;

		initializeClassifier();
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;

		initializeClassifier();
	}

	public int getMinInstancesPerLeaf() {
		return minInstancesPerLeaf;
	}

	public void setMinInstancesPerLeaf(int minInstancesPerLeaf) {
		this.minInstancesPerLeaf = minInstancesPerLeaf;

		initializeClassifier();
	}

	public double getMinClassVariance() {
		return minClassVariance;
	}

	public void setMinClassVariance(double minClassVariance) {
		this.minClassVariance = minClassVariance;

		initializeClassifier();
	}
}
