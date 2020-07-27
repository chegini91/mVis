package com.github.TKnudsen.ComplexDataObject.data.features;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.IDObject;
import com.github.TKnudsen.ComplexDataObject.data.interfaces.IFeatureVectorObject;
import com.github.TKnudsen.ComplexDataObject.data.interfaces.IMasterProvider;
import com.github.TKnudsen.ComplexDataObject.data.interfaces.ISelfDescription;
import com.github.TKnudsen.ComplexDataObject.data.keyValueObject.KeyValueObject;

/**
 * <p>
 * Title: AbstractFeatureVector
 * </p>
 * 
 * <p>
 * Description: general feature representation of a given object. Provides basic
 * attributes and functionality.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2016-2018
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.03
 */
public abstract class AbstractFeatureVector<O, F extends Feature<O>> extends KeyValueObject<Object>
		implements ISelfDescription, IMasterProvider, Cloneable, IFeatureVectorObject<O, F> {

	private String name;

	private String description;

	private IDObject master;

	protected List<F> featuresList;

	protected SortedMap<String, F> featuresMap;

	protected AbstractFeatureVector() {
		featuresList = new ArrayList<>();
		createFeatureNamesMap();
	}

	public AbstractFeatureVector(List<F> features) {
		this.featuresList = features;

		generalizeFromList();
	}

	public AbstractFeatureVector(F[] features) {
		generalizeFromArray(features);
	}

	public AbstractFeatureVector(SortedMap<String, F> featuresMap) {
		this.featuresMap = featuresMap;

		generalizeFromMap();
	}

	@Override
	public abstract AbstractFeatureVector<O, F> clone();

	protected void createFeatureNamesMap() {
		featuresMap = null;

		if (featuresList == null)
			return;

		featuresMap = new TreeMap<>();

		for (int i = 0; i < featuresList.size(); i++)
			if (featuresList.get(i) != null && featuresList.get(i).getFeatureName() != null)
				featuresMap.put(featuresList.get(i).getFeatureName(), featuresList.get(i));
		checkFeatureNameConsistency();
	}

	protected void generalizeFromArray(F[] featuresArray) {
		featuresList = null;
		featuresMap = null;

		if (featuresArray == null)
			return;

		featuresList = new ArrayList<>();
		featuresMap = null;

		for (F feature : featuresArray) {
			featuresList.add(feature);
			featuresMap.put(feature.getFeatureName(), feature);
			// refactoring. lazy implementation to save computation time
			// if (feature != null && feature.getFeatureName() != null)
			// featuresMap.put(feature.getFeatureName(), feature);
		}
	}

	protected void generalizeFromMap() {
		featuresList = null;

		if (featuresMap == null)
			return;

		featuresList = new ArrayList<>();
		for (String s : featuresMap.keySet())
			featuresList.add(featuresMap.get(s));
	}

	protected void generalizeFromList() {
		featuresMap = new TreeMap<>();

		for (F feature : featuresList)
			featuresMap.put(feature.getFeatureName(), feature);
	}

	/**
	 * really nececssary?
	 * 
	 * @param featureName
	 * @return
	 */
	protected boolean checkForDuplicateFeatureName(String featureName) {
		for (F entry : featuresList)
			if (entry != null && entry.getFeatureName() != null)
				if (featureName.equals(entry.getFeatureName()))
					return true;
		return false;
	}

	/**
	 * really necessary?
	 */
	protected void checkForDuplicateFeatureNames() {
		Set<String> featureNames = new HashSet<String>();
		String errorString = "";

		for (F entry : featuresList)
			if (entry != null && entry.getFeatureName() != null)
				if (featureNames.contains(entry.getFeatureName()))
					errorString = (errorString + entry.getFeatureName() + ", ");
				else
					featureNames.add(entry.getFeatureName());

		if (!errorString.equals(""))
			throw new IllegalArgumentException(
					"FeatureVector - duplicate Features: " + errorString.substring(0, errorString.length() - 2));
	}

	private boolean checkFeatureNameConsistency() {
		if (featuresList != null) {
			if (featuresList.size() != getFeaturesMap().size())
				throw new IllegalArgumentException("FeatureVector: name conflict, features with identical name?");
		}

		return true;
	}

	@Override
	public F getFeature(int index) {
		if (featuresList != null)
			if (featuresList.size() > index)
				return featuresList.get(index);
			else
				throw new IndexOutOfBoundsException();
		return null;
	}

	@Override
	public F getFeature(String featureName) {
		if (getFeaturesMap() != null)
			return getFeaturesMap().get(featureName);
		return null;
	}

	/**
	 * Expensive variant of adding a feature into the internal data representations.
	 */
	public void addFeature(F feature) {
		if (feature == null)
			return;

		if (featuresList == null)
			featuresList = new ArrayList<>();
		if (featuresMap == null)
			featuresMap = new TreeMap<>();

		if (!featuresList.contains(feature))
			for (int i = 0; i < featuresList.size(); i++)
				if (featuresList.get(i).getFeatureName().equals(feature.getFeatureName())) {
					featuresList.set(i, feature);
					featuresMap.put(feature.getFeatureName(), feature);
					return;
				}

		featuresList.add(feature);
		featuresMap.put(feature.getFeatureName(), feature);
	}

	/**
	 * Sets a feature at a given index. Does not handle index exceptions.
	 * 
	 * @param index
	 * @param feature
	 */
	public void setFeature(int index, F feature) {
		F featureToBeReplaced = null;

		if (featuresList != null)
			featureToBeReplaced = featuresList.set(index, feature);

		if (featuresMap != null) {
			featuresMap.remove(featureToBeReplaced.getFeatureName());
			featuresMap.put(feature.getFeatureName(), feature);
		}
	}

	public F removeFeature(String featureName) {
		F feature = featuresMap.remove(featureName);

		if (featuresList != null)
			if (feature != null) {
				featuresList.remove(feature);
				return feature;
			} else
				for (F f : featuresList)
					if (f.getFeatureName().equals(featureName)) {
						featuresList.remove(f);
						return f;
					}

		return feature;
	}

	/**
	 * removes a feature at a given index position.
	 * 
	 * @param index
	 * @return
	 */
	public F removeFeature(int index) {
		if (featuresList != null)
			if (featuresList.size() > index) {
				F removed = featuresList.remove(index);
				featuresMap.remove(removed.getFeatureName());
				return removed;
			}
		return null;
	}

	@Override
	public List<F> getVectorRepresentation() {
		return Collections.unmodifiableList(featuresList);
	}

	/**
	 * Features always describes the individual features of the vector. In contrast,
	 * getAttributes() retrieves the metadata.
	 */
	@Override
	public Set<String> getFeatureKeySet() {
		if (getFeaturesMap() == null)
			return null;

		return getFeaturesMap().keySet();
	}

	@Override
	public int sizeOfFeatures() {
		return featuresList.size();
	}

	public int getDimensions() {
		return featuresList.size();
	}

	@Override
	/**
	 * If the FeatureDataObject is derived from another IDObject this can be
	 * specified here.
	 */
	public IDObject getMaster() {
		return master;
	}

	/**
	 * Get the IDObject of the FeatureDataObject - if specified.
	 * 
	 * @param master
	 */
	public void setMaster(IDObject master) {
		this.master = master;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		String n = this.getName();
		if (n == null)
			n = this.getClass().getSimpleName();

		n += (", " + this.getID() + ", dim: " + getDimensions() + "\t");

		return n;
	}

	protected Map<String, F> getFeaturesMap() {
		if (featuresMap == null)
			createFeatureNamesMap();

		return featuresMap;
	}

	/**
	 * retrieves all features of a given FeatureType. Example: all
	 * MixedDataFeatures.
	 * 
	 * @param featureType
	 * @return
	 */
	public List<Feature<?>> getFeaturesByType(FeatureType featureType) {
		List<Feature<?>> list = new ArrayList<>();

		for (Feature<?> feature : featuresList)
			if (feature != null)
				if (feature.getFeatureType().equals(featureType))
					list.add(feature);

		return list;
	}

}
