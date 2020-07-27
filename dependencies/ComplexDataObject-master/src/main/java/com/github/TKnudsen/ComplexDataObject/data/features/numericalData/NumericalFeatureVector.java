package com.github.TKnudsen.ComplexDataObject.data.features.numericalData;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import com.github.TKnudsen.ComplexDataObject.data.features.AbstractFeatureVector;
import com.github.TKnudsen.ComplexDataObject.data.features.FeatureType;

import de.javagl.nd.tuples.d.DoubleTuple;

/**
 * <p>
 * Title: NumericalFeatureVector
 * </p>
 *
 * <p>
 * Description: Numerical representation of a high-dimensional object. Can be
 * used for algorithmic models applied in data mining, machine learning,
 * information retrieval, etc.
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2015-2017
 * </p>
 *
 * @author Juergen Bernard
 * @version 1.02
 */

public class NumericalFeatureVector extends AbstractFeatureVector<Double, NumericalFeature> implements DoubleTuple {

	private NumericalFeatureVector() {
		super();
	}

	public NumericalFeatureVector(List<NumericalFeature> features) {
		super(features);
	}

	public NumericalFeatureVector(NumericalFeature[] features) {
		super(features);
	}

	public NumericalFeatureVector(SortedMap<String, NumericalFeature> featuresMap) {
		super(featuresMap);
	}

	@Override
	public NumericalFeatureVector subTuple(int fromIndex, int toIndex) {
		return new NumericalFeatureVector(featuresList.subList(fromIndex, toIndex));
	}

	@Override
	public int getSize() {
		return sizeOfFeatures();
	}

	@Override
	public double get(int arg0) {
		return getFeature(arg0).doubleValue();
	}

	/**
	 * Conversion to primitive double format. Algorithms require the
	 * (traditional) version of double vectors.
	 *
	 * @return
	 */
	public double[] getVector() {
		return toPrimitive(featuresList);
	}

	public void setVector(double vector[]) {
		featuresList = new ArrayList<>();
		featuresMap = null;

		if (vector != null)
			for (int i = 0; i < vector.length; i++)
				featuresList.add(new NumericalFeature("Dim " + i + 1, vector[i]));
	}

	public double[] getVectorClone() {
		return this.getVector().clone();
	}

	@Override
	public NumericalFeatureVector clone() {
		List<NumericalFeature> features = new ArrayList<>();
		for (NumericalFeature f : featuresList)
			features.add(f.clone());

		NumericalFeatureVector clone = new NumericalFeatureVector(features);

		// attributes and meta information
		clone.setMaster(getMaster());
		for (String s : attributes.keySet())
			clone.add(s, getAttribute(s));

		// name and description
		clone.setName(getName());
		clone.setDescription(getDescription());

		return clone;
	}

	private static double[] toPrimitive(List<NumericalFeature> features) {
		if (features == null)
			return null;
		else if (features.size() == 0)
			return new double[0];

		final double[] result = new double[features.size()];
		for (int i = 0; i < features.size(); i++)
			if (features.get(i) instanceof NumericalFeature)
				result[i] = ((NumericalFeature) features.get(i)).doubleValue();
			else if (features.get(i).getFeatureType().equals(FeatureType.DOUBLE))
				result[i] = ((Double) features.get(i).getFeatureValue()).doubleValue();
		return result;
	}

	@Override
	public void addFeature(String featureName, Double value, FeatureType type) {
		addFeature(new NumericalFeature(featureName, value));
	}
}
