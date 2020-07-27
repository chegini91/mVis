package com.github.TKnudsen.ComplexDataObject.data.features;

import java.io.Serializable;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.IDObject;
import com.github.TKnudsen.ComplexDataObject.model.tools.MathFunctions;

/**
 * <p>
 * Title: Feature
 * </p>
 *
 * <p>
 * Description: Representation of a single feature consisting of its name and
 * its value.
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2016-2017
 * </p>
 *
 * @author Juergen Bernard
 * @version 1.02
 */
public abstract class Feature<V> implements IDObject, Comparable<Feature<V>>, Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 921823756506274008L;

	/**
	 * ID of the feature.
	 */
	protected long ID;

	/**
	 * The name/key/identifier/attribute/column of the feature. Needs to be
	 * comparable.
	 */
	protected String featureName;

	/**
	 * The value of the feature
	 */

	protected V featureValue;

	/**
	 * The type of feature. Default: FeatureType.Double
	 */
	protected FeatureType featureType = FeatureType.DOUBLE;

	protected int hashCode;

	/**
	 * Simple constructor
	 * 
	 * @param featureType
	 */
	protected Feature(FeatureType featureType) {
		this.featureType = featureType;
		this.ID = MathFunctions.randomLong();

		resetHashCode();
	}

	/**
	 *
	 * @param featureName
	 * @param featureValue
	 */
	public Feature(String featureName, V featureValue) {
		this(featureName, featureValue, FeatureType.DOUBLE);
	}

	public Feature(String featureName, V featureValue, FeatureType featureType) {
		this.featureName = featureName;
		this.featureValue = featureValue;
		this.featureType = featureType;

		this.ID = MathFunctions.randomLong();

		resetHashCode();
	}

	protected void resetHashCode() {
		hashCode = -1;
	}

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;

		resetHashCode();
	}

	public V getFeatureValue() {
		return featureValue;
	}

	public boolean setFeatureValue(V featureValue) {
		this.featureValue = featureValue;

		resetHashCode();

		return true;
	}

	/**
	 * return the type of feature. Default: DOUBLE
	 *
	 * @return
	 */
	public FeatureType getFeatureType() {
		return featureType;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(Feature<V> o) {
		int c = featureName.compareTo(o.getFeatureName());
		if (c == 0 && featureValue instanceof Comparable<?> && o.getFeatureValue() instanceof Comparable<?>)
			return ((Comparable<Feature<?>>) featureValue).compareTo((Feature<?>) o.getFeatureValue());
		return featureName.compareTo(o.getFeatureName());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		Feature<?> other = (Feature<?>) obj;

		if (!featureName.equals(other.featureName))
			return false;

		if (!featureValue.equals(other.featureValue))
			return false;

		if (!featureType.equals(other.featureType))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		if (hashCode != -1)
			return hashCode;

		hashCode = 1;

		if (featureName == null)
			hashCode = 23 * hashCode;
		else
			hashCode = 29 * hashCode + featureName.hashCode();

		if (featureValue == null)
			hashCode = 23 * hashCode;
		else
			hashCode = 29 * hashCode + featureValue.hashCode();

		return hashCode;
	}

	@Override
	public abstract Feature<V> clone();

	@Override
	public String toString() {
		return "Feature " + featureName + ": " + featureValue + " (" + featureType.name() + ") ";
	}

	@Override
	public long getID() {
		return ID;
	}
}
