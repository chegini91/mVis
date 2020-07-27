package com.github.TKnudsen.DMandML.model.unsupervised.clustering.impl;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.DMandML.model.unsupervised.clustering.WekaClusteringAlgorithm;

/**
 * <p>
 * Title: KMeans
 * </p>
 * 
 * <p>
 * Description: implementation is based on WEKAs XMeans.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2017-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.03
 */
public class XMeans extends WekaClusteringAlgorithm {

	/**
	 * minimum number of clusters
	 */
	private int k_min;

	/**
	 * maximum number of clusters
	 */
	private int k_max;

	/**
	 * final number of clusters (output) of the model
	 */
	private int k;

	protected XMeans() {
		this(1, 10, null);
	}

	public XMeans(int k_min, int k_max, List<? extends NumericalFeatureVector> featureVectors) {
		super(featureVectors);
		this.k_min = k_min;
		this.k_max = k_max;
	}

	@Override
	public String getName() {
		return "XMeans";
	}

	@Override
	public String getDescription() {
		return getName();
	}

	@Override
	protected void initializeClusteringAlgorithm() {
		wekaClusterer = new weka.clusterers.XMeans();

		String[] options = new String[6];
		options[0] = "-I";
		options[1] = "" + 1;
		options[2] = "-L";
		options[3] = "" + getK_min();
		options[4] = "-H";
		options[5] = "" + getK_max();

		try {
			wekaClusterer.setOptions(options);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void calculateClustering() {
		super.calculateClustering();

		if (clusterResult != null)
			k = clusterResult.size();
	}

	public int getK_min() {
		return k_min;
	}

	public void setK_min(int k_min) {
		this.k_min = k_min;
	}

	public int getK_max() {
		return k_max;
	}

	public void setK_max(int k_max) {
		this.k_max = k_max;
	}

	public int getK() {
		return k;
	}
}
