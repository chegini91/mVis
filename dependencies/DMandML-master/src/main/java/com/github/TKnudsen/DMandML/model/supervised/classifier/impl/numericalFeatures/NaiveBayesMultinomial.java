package com.github.TKnudsen.DMandML.model.supervised.classifier.impl.numericalFeatures;

import java.util.ArrayList;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.DMandML.model.supervised.classifier.WekaClassifierWrapper;

/**
 * <p>
 * Title: NaiveBayesMultinomial
 * </p>
 * 
 * <p>
 * Description: Class for building and using a multinomial Naive Bayes
 * classifier. For more information see,
 * 
 * Andrew Mccallum, Kamal Nigam: A Comparison of Event Models for Naive Bayes
 * Text Classification. In: AAAI-98 Workshop on 'Learning for Text
 * Categorization', 1998.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class NaiveBayesMultinomial extends WekaClassifierWrapper<NumericalFeatureVector> {

	public NaiveBayesMultinomial() {

	}

	@Override
	protected void initializeClassifier() {
		setWekaClassifier(new weka.classifiers.bayes.NaiveBayesMultinomial());

		List<String> optionsList = new ArrayList<String>();
		String[] options = optionsList.toArray(new String[optionsList.size()]);

		try {
			((weka.classifiers.bayes.NaiveBayesMultinomial) getWekaClassifier()).setOptions(options);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
