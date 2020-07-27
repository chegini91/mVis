package com.github.TKnudsen.DMandML.model.supervised.classifier.impl.numericalFeatures;

import java.util.ArrayList;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.DMandML.model.supervised.classifier.WekaClassifierWrapper;

/**
 * <p>
 * Title: DecorateClassifier
 * </p>
 * 
 * <p>
 * Description: Ensemble classifier that tries to create diverse ensembles by
 * using specially constructed artificial training examples.
 * 
 * P. Melville, R. J. Mooney: Constructing Diverse Classifier Ensembles Using
 * Artificial Training Examples. In: Eighteenth International Joint Conference
 * on Artificial Intelligence, 505-510, 2003.
 * 
 * P. Melville, R. J. Mooney (2004). Creating Diversity in Ensembles Using
 * Artificial Data. Information Fusion: Special Issue on Diversity in
 * Multiclassifier Systems..
 * </p>
 * 
 * <p>
 * Copyright: (c) 2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class DecorateClassifier extends WekaClassifierWrapper<NumericalFeatureVector> {

	/**
	 * Desired size of ensemble.
	 */
	private int ensembleSize = 15;

	/**
	 * Factor that determines number of artificial examples to generate. Specified
	 * proportional to training set size.
	 */
	private double artificialExamplesSizeFactor = 1.0;

	/**
	 * Number of iterations.
	 */
	private int iterations = 50;

	public DecorateClassifier() {

	}

	@Override
	protected void initializeClassifier() {
		setWekaClassifier(new weka.classifiers.meta.Decorate());

		List<String> optionsList = createArgs();
		String[] options = optionsList.toArray(new String[optionsList.size()]);

		try {
			((weka.classifiers.meta.Decorate) getWekaClassifier()).setOptions(options);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<String> createArgs() {
		List<String> optionsList = new ArrayList<String>();

		optionsList.add("-E");
		optionsList.add(ensembleSize + "");

		optionsList.add("-R");
		optionsList.add(artificialExamplesSizeFactor + "");

		optionsList.add("-I");
		optionsList.add(iterations + "");

		return optionsList;
	}

	public int getEnsembleSize() {
		return ensembleSize;
	}

	public void setEnsembleSize(int ensembleSize) {
		this.ensembleSize = ensembleSize;

		initializeClassifier();
	}

	public double getArtificialExamplesSizeFactor() {
		return artificialExamplesSizeFactor;
	}

	public void setArtificialExamplesSizeFactor(double artificialExamplesSizeFactor) {
		this.artificialExamplesSizeFactor = artificialExamplesSizeFactor;

		initializeClassifier();
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;

		initializeClassifier();
	}

}
