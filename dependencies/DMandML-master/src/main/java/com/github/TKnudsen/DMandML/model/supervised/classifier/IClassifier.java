package com.github.TKnudsen.DMandML.model.supervised.classifier;

import java.util.List;
import java.util.Map;

import com.github.TKnudsen.DMandML.data.classification.IClassificationResult;
import com.github.TKnudsen.DMandML.model.supervised.ILearningModel;

/**
 * <p>
 * Title: IClassifier
 * </p>
 * 
 * <p>
 * Description: basic classifier interface. Classifiers learn categorical label
 * information.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.10
 * 
 */
public interface IClassifier<X> extends ILearningModel<X, String> {

	IClassificationResult<X> createClassificationResult(List<? extends X> featureVectors);

	/**
	 * Returns the name of the attribute that will serve as the basis for the
	 * classification.
	 * 
	 * @return The class attribute name
	 */
	String getClassAttribute();

	/**
	 * Returns an unmodifiable view on the label alphabet.<br>
	 * <br>
	 * This represents the set of unique labels that have been found in the feature
	 * vectors that have been passed to the last call to
	 * {@link #train(List, String)}, in an unspecified order.<br>
	 * <br>
	 * If no training has taken place yet, this will be the empty list.
	 * 
	 * @return The label alphabet
	 */
	public List<String> getLabelAlphabet();

	/**
	 * Returns a (possibly unmodifiable) map containing the mapping of all class
	 * labels to the probability that the given vector belongs to the respective
	 * class.<br>
	 * <br>
	 * If the classifier has not been trained yet, this will be an empty map.
	 * 
	 * @param featureVector
	 *            The feature vector
	 * @return The label distribution. LinkedHashMap preserves the order of labels,
	 *         useful for larger contexts.
	 */
	Map<String, Double> getLabelDistribution(X featureVector);
}
