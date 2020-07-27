package com.github.TKnudsen.DMandML.model.supervised;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.ISelfDescription;

/**
 * <p>
 * Title: ILearningModel
 * </p>
 * 
 * <p>
 * Description: basic algorithmic model that learns label information for
 * features. Both features and labels are to be defined in extending interfaces
 * / implementing classes.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.05
 * 
 */
public interface ILearningModel<X, Y> extends ISelfDescription {

	/**
	 * Perform a training on this learning model using the given elements
	 * 
	 * @param featureVectors The feature vectors
	 * @throws IllegalArgumentException If the given list has less than 2 elements
	 */
	public void train(List<X> featureVectors);

	/**
	 * Perform a test/classification of the given elements. The returned
	 * list will be a (possibly unmodifiable) list containing the labels
	 * for the corresponding feature vectors. <br>
	 * <br>
	 * The returned list will always have the same size as the given list.<br>
	 * <br>
	 * If the learning model has not been {@link #train(List) trained}, then
	 * the returned list may contain only <code>null</code> elements. 
	 *  
	 * @param featureVectors The feature vectors
	 * @return The list containing one label for each feature vector
	 */
	public List<Y> test(List<X> featureVectors);
}
