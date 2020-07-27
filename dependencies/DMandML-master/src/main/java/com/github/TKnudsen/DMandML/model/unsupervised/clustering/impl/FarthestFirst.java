package com.github.TKnudsen.DMandML.model.unsupervised.clustering.impl;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.DMandML.model.unsupervised.clustering.WekaClusteringAlgorithm;

/**
 * <p>
 * Title: FarthestFirst
 * </p>
 * 
 * <p>
 * Description: implementation is based on WEKAs FarthestFirst.
 * 
 * Hochbaum, Shmoys (1985). A best possible heuristic for the k-center problem.
 * Mathematics of Operations Research. 10(2):180-184.
 * 
 * Sanjoy Dasgupta: Performance Guarantees for Hierarchical Clustering. In: 15th
 * Annual Conference on Computational Learning Theory, 351-363, 2002.
 * 
 * Notes: - works as a fast simple approximate clusterer - modelled after
 * SimpleKMeans, might be a useful initializer for it
 * </p>
 * 
 * <p>
 * Copyright: (c) 2017-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 */
public class FarthestFirst extends WekaClusteringAlgorithm {

	private int k;

	private int seed;

	protected FarthestFirst() {
		this(2, 1);
	}

	protected FarthestFirst(int k) {
		this(k, 1);
	}

	public FarthestFirst(int k, List<? extends NumericalFeatureVector> featureVectors) {
		this(k, 1);

		setFeatureVectors(featureVectors);
	}

	protected FarthestFirst(int k, int seed) {
		setK(k);
		setSeed(seed);
	}

	@Override
	public String getName() {
		return "FarthestFirst";
	}

	@Override
	public String getDescription() {
		return getName();
	}

	@Override
	protected void initializeClusteringAlgorithm() {
		// Valid options are:
		//
		// -N <num>
		// number of clusters. (default = 2).
		//
		// -S <num>
		// Random number seed.
		// (default 1)

		wekaClusterer = new weka.clusterers.FarthestFirst();

		String[] options = new String[4];
		options[0] = "-N";
		options[1] = "" + getK();
		options[2] = "-S";
		options[3] = "" + getSeed();

		try {
			wekaClusterer.setOptions(options);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}

	public int getSeed() {
		return seed;
	}

	public void setSeed(int seed) {
		this.seed = seed;
	}

}
