package com.github.TKnudsen.DMandML.model.supervised.classifier.impl.mixedFeatures;

import java.util.ArrayList;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeatureVector;
import com.github.TKnudsen.DMandML.model.supervised.classifier.WekaClassifierWrapper;

import weka.classifiers.mi.MINND;

/**
 * <p>
 * Title: MINNDClassifier
 * </p>
 * 
 * <p>
 * Description: Multiple-Instance Nearest Neighbour with Distribution learner.
 * 
 * Attention: Cannot handle numeric attributes!
 * 
 * WEKA: "It uses gradient descent to find the weight for each dimension of each
 * exeamplar from the starting point of 1.0. In order to avoid overfitting, it
 * uses mean-square function (i.e. the Euclidean distance) to search for the
 * weights. It then uses the weights to cleanse the training data. After that it
 * searches for the weights again from the starting points of the weights
 * searched before. Finally it uses the most updated weights to cleanse the test
 * exemplar and then finds the nearest neighbour of the test exemplar using
 * partly-weighted Kullback distance. But the variances in the Kullback distance
 * are the ones before cleansing."
 * 
 * Xin Xu (2001). A nearest distribution approach to multiple-instance learning.
 * Hamilton, NZ.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class MINNDClassifier extends WekaClassifierWrapper<MixedDataFeatureVector> {

	/**
	 * Number of nearest neighbors for prediction
	 */
	private int kNN;

	/**
	 * Number of nearest neighbours for cleansing training data
	 */
	private int kNNCleansingTraining;

	/**
	 * Number of nearest neighbours for cleansing testing data
	 */
	private int kNNCleansingTesting;

	public MINNDClassifier() {

	}

	public MINNDClassifier(int kNN, int kNNCleansingTraining, int kNNCleansingTesting) {
		this.kNN = kNN;
		this.kNNCleansingTraining = kNNCleansingTraining;
		this.kNNCleansingTesting = kNNCleansingTesting;
	}

	@Override
	protected void initializeClassifier() {
		setWekaClassifier(new MINND());

		List<String> optionsList = new ArrayList<String>();

		optionsList.add("-K");
		optionsList.add(kNN + "");

		optionsList.add("-S");
		optionsList.add(kNNCleansingTraining + "");

		optionsList.add("-E");
		optionsList.add(kNNCleansingTesting + "");

		String[] options = optionsList.toArray(new String[optionsList.size()]);

		try {
			((MINND) getWekaClassifier()).setOptions(options);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getDescription() {
		return "Multiple-Instance Nearest Neighbour with Distribution learner";
	}

	public int getkNN() {
		return kNN;
	}

	public void setkNN(int kNN) {
		this.kNN = kNN;

		initializeClassifier();
	}

	public int getkNNCleansingTraining() {
		return kNNCleansingTraining;
	}

	public void setkNNCleansingTraining(int kNNCleansingTraining) {
		this.kNNCleansingTraining = kNNCleansingTraining;

		initializeClassifier();
	}

	public int getkNNCleansingTesting() {
		return kNNCleansingTesting;
	}

	public void setkNNCleansingTesting(int kNNCleansingTesting) {
		this.kNNCleansingTesting = kNNCleansingTesting;

		initializeClassifier();
	}

}
