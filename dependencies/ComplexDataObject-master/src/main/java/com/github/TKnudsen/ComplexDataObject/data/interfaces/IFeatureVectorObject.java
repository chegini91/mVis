package com.github.TKnudsen.ComplexDataObject.data.interfaces;

import java.util.List;
import java.util.Set;

import com.github.TKnudsen.ComplexDataObject.data.features.Feature;
import com.github.TKnudsen.ComplexDataObject.data.features.FeatureType;

/**
 * <p>
 * Title: IAlgorithmDataObject
 * </p>
 *
 * <p>
 * Description: Interface for all objects that can be accepted by algorithms
 * (data mining, machine learning, information retrieval, etc.).
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2016-2018
 * </p>
 *
 * @author Juergen Bernard
 * @version 1.02
 */

public interface IFeatureVectorObject<O, F extends Feature<O>> extends IDObject, IKeyValueProvider<Object> {

	public F getFeature(int index);

	public F getFeature(String featureName);

	public void addFeature(F feature);

	public void addFeature(String featureName, O value, FeatureType type);

	public void setFeature(int index, F feature);

	public F removeFeature(String featureName);

	public IFeatureVectorObject<O, F> subTuple(int fromIndex, int toIndex);

	public List<F> getVectorRepresentation();

	public Set<String> getFeatureKeySet();

	public int sizeOfFeatures();

	/**
	 * convenient function that is sometimes required when working with FVs without
	 * modifying them
	 * 
	 * @return
	 */
	public IFeatureVectorObject<O, F> clone();
}
