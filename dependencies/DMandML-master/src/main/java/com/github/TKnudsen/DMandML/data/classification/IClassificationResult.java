package com.github.TKnudsen.DMandML.data.classification;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.ISelfDescription;

/**
 * <p>
 * Title: IClassificationResult
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.05
 */
public interface IClassificationResult<X> extends ISelfDescription {

	/**
	 * Returns a (possibly unmodifiable) collection containing all feature vectors
	 * that this result has been computed for.
	 * 
	 * @return The feature vectors
	 */
	public Collection<X> getFeatureVectors();

	/**
	 * Returns the most likely class that the given vector belongs to, or
	 * <code>null</code> if the given vector is not element of the
	 * {@link #getFeatureVectors() feature vectors of this result}
	 * 
	 * @param featureVector
	 *            The feature vector
	 * @return The class
	 */
	public String getClass(X featureVector);

	/**
	 * Returns a (possibly unmodifiable) mapping from class labels to the lists of
	 * feature vectors for which the respective class is the most likely one.<br>
	 * <br>
	 * Note that the key set of this map will not necessarily contain all possible
	 * class labels. It will only contain the class labels that are the most likely
	 * one for <i>any</i> of the feature vectors.
	 * 
	 * @return The class distributions
	 */
	public Map<String, List<X>> getClassDistributions();

	/**
	 * Returns the {@link LabelDistribution} for the given feature vector, or
	 * <code>null</code> if the given feature vector is not contained in the
	 * {@link #getFeatureVectors() feature vectors of this result}. For
	 * non-probabilistic classifiers the distribution is 100% w.r.t the winning
	 * label.
	 * 
	 * @param x
	 *            The feature vector
	 * @return The {@link LabelDistribution}
	 */
	public LabelDistribution getLabelDistribution(X x);

	/**
	 * every classification result must know the label alphabet of the
	 * classification model in its current state. The label alphabet may be larger
	 * than the predictions made in the concrete classification result.
	 * 
	 * @return
	 */
	public Set<String> getLabelAlphabet();
}
