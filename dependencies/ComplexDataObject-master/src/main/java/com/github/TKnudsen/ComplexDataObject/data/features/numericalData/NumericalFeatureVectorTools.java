package com.github.TKnudsen.ComplexDataObject.data.features.numericalData;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * Title: NumericalFeatureVectorTools
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2016
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.0
 */

public class NumericalFeatureVectorTools {

	/**
	 * retrieves the minimum value.
	 * 
	 * @param featureVector
	 * @return
	 */
	public static Double getMin(NumericalFeatureVector featureVector) {
		if (featureVector == null)
			return Double.NaN;

		Double d = Double.MAX_VALUE - 1;
		for (NumericalFeature feature : featureVector.getVectorRepresentation())
			d = Math.min(d, feature.doubleValue());
		return d;
	}

	/**
	 * retrieves the maximum value.
	 * 
	 * @param featureVector
	 * @return
	 */
	public static Double getMax(NumericalFeatureVector featureVector) {
		if (featureVector == null)
			return Double.NaN;

		Double d = Double.MIN_VALUE + 1;
		for (NumericalFeature feature : featureVector.getVectorRepresentation())
			d = Math.max(d, feature.doubleValue());
		return d;
	}

	/**
	 * calculates the mean value
	 * 
	 * @param features
	 * @param dim
	 * @return
	 */
	public static double getMean(List<NumericalFeatureVector> features, int dim) {
		double sum = 0;
		double count = 0;

		for (int n = 0; n < features.size(); n++) {
			if (!Double.isNaN(features.get(n).get(dim))) {
				sum += features.get(n).get(dim);
				count += 1;
			}
		}
		return sum / count;
	}

	public static List<double[]> toPrimitives(Set<NumericalFeatureVector> featureVectors) {
		List<double[]> returnValues = new ArrayList<>();

		for (NumericalFeatureVector fv : featureVectors)
			returnValues.add(fv.getVectorClone());

		return returnValues;
	}

	public static List<double[]> toPrimitives(List<NumericalFeatureVector> featureVectors) {
		List<double[]> returnValues = new ArrayList<>();

		for (int i = 0; i < featureVectors.size(); i++)
			returnValues.add(featureVectors.get(i).getVectorClone());

		return returnValues;
	}

	public static void addClassAttribute(List<NumericalFeatureVector> featureVectors, List<String> labels, String classAttribute) {
		for (int i = 0; i < featureVectors.size(); i++)
			featureVectors.get(i).add(classAttribute, labels.get(i));
	}

	public static void addNumericAttribute(List<NumericalFeatureVector> features, List<Double> labels, String attributeName) {
		for (int i = 0; i < features.size(); i++)
			features.get(i).add(attributeName, labels.get(i));
	}

	/**
	 * creates a matrix-like representation with the number of feature vectors
	 * in the first dimension and the dimensionality of the feature vectors in
	 * the second dimension.
	 * 
	 * @param fvs
	 * @return
	 */
	public static double[][] createMatrixRepresentation(List<NumericalFeatureVector> fvs) {
		if (fvs == null)
			throw new NullPointerException("NumericalFeatureVectorTools: feature vectors must not be null");

		if (fvs.size() == 0)
			throw new IllegalArgumentException("NumericalFeatureVectorTools: feature vectors size was 0");

		double[][] values = new double[fvs.size()][fvs.get(0).getDimensions()];
		for (int i = 0; i < fvs.size(); i++)
			values[i] = fvs.get(i).getVectorClone();

		return values;
	}

	/**
	 * creates a vector with the information of one feature from the
	 * FeatureVectors, addressed by the index.
	 * 
	 * @param fvs
	 * @param index
	 * @return
	 */
	public static double[] retrieveVariable(List<NumericalFeatureVector> fvs, int index) {
		if (fvs == null)
			throw new NullPointerException("NumericalFeatureVectorTools: feature vectors must not be null");

		if (fvs.size() == 0)
			throw new IllegalArgumentException("NumericalFeatureVectorTools: feature vectors size was 0");

		double[] values = new double[fvs.size()];
		for (int i = 0; i < fvs.size(); i++)
			values[i] = fvs.get(i).get(index);

		return values;
	}

	public static double[] retrieveNumericalAttribute(List<NumericalFeatureVector> fvs, String classAttribute) {
		if (fvs == null)
			throw new NullPointerException("LDA: feature vectors must not be null");

		if (fvs.size() == 0)
			throw new IllegalArgumentException("LDA: feature vectors size was 0");

		double[] values = new double[fvs.size()];
		for (int i = 0; i < fvs.size(); i++) {
			if (fvs.get(i).getAttribute(classAttribute) != null)
				if (fvs.get(i).getAttribute(classAttribute) instanceof Number) {
					values[i] = ((Number) fvs.get(i).getAttribute(classAttribute)).doubleValue();
					continue;
				}
			values[i] = Double.NaN;
		}

		return values;
	}

}
