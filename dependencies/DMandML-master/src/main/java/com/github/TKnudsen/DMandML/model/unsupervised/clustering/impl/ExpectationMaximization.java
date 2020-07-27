package com.github.TKnudsen.DMandML.model.unsupervised.clustering.impl;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.DMandML.model.unsupervised.clustering.WekaClusteringAlgorithm;

/**
 * <p>
 * Title: ExpectationMaximization
 * </p>
 * 
 * <p>
 * Description: implementation is based on WEKAs EM.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2017-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 */
public class ExpectationMaximization extends WekaClusteringAlgorithm {

	/**
	 * number of iterations to be trained.
	 */
	private int maxIterations = 100;

	/**
	 * number of clusters to be calculated.
	 */
	private int k;

	protected ExpectationMaximization() {
		this(3, 100);
	}

	public ExpectationMaximization(int k) {
		this(k, 100);
	}

	public ExpectationMaximization(int k, int maxIterations) {
		setK(k);
		setMaxIterations(maxIterations);
	}

	public ExpectationMaximization(int k, List<? extends NumericalFeatureVector> featureVectors) {
		this(k);

		setFeatureVectors(featureVectors);
	}

	@Override
	public String getName() {
		return "Expectation Maximization";
	}

	@Override
	public String getDescription() {
		return getName();
	}

	@Override
	protected void initializeClusteringAlgorithm() {
		wekaClusterer = new weka.clusterers.EM();

		String[] options = new String[4];
		options[0] = "-I";
		options[1] = getMaxIterations() + "";
		options[2] = "-N";
		options[3] = getK() + "";
		try {
			wekaClusterer.setOptions(options);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getMaxIterations() {
		return maxIterations;
	}

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}
}
