package com.github.TKnudsen.DMandML.model.supervised.classifier.impl.mixedFeatures;

import java.util.ArrayList;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeatureVector;
import com.github.TKnudsen.DMandML.model.supervised.classifier.WekaClassifierWrapper;

/**
 * <p>
 * Title: BayesNet
 * </p>
 * 
 * <p>
 * Description: RandomForest implementation for MixedDataFeatureVectors.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class RandomForest extends WekaClassifierWrapper<MixedDataFeatureVector> {

	/**
	 * number of trees
	 */
	private final int trees;

	/**
	 * number of randomly chosen attributes
	 */
	private final int numFeatures;

	/**
	 * maximum depth of the tree
	 */
	private final int depthMax;

	public RandomForest() {
		this(10, 0, 0);
	}

	public RandomForest(int trees, int numFeatures, int depthMax) {
		this.trees = trees;
		this.numFeatures = numFeatures;
		this.depthMax = depthMax;
	}

	@Override
	protected void initializeClassifier() {

		setWekaClassifier(new weka.classifiers.trees.RandomForest());

		List<String> aryOpts = new ArrayList<String>();
		aryOpts.add("-I");
		aryOpts.add(trees + "");

		aryOpts.add("-K");
		aryOpts.add(numFeatures + "");

		aryOpts.add("-S");
		aryOpts.add(1 + "");

		aryOpts.add("-depth");
		aryOpts.add(depthMax + "");

		String[] opts = aryOpts.toArray(new String[aryOpts.size()]);

		try {
			((weka.classifiers.trees.RandomForest) getWekaClassifier()).setOptions(opts);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
