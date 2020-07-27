package com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.mixedData;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.FeatureType;
import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeatureVector;
import com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.WeightedDistanceMeasure;

/**
 * <p>
 * Title: WeightedMixedDataFeatureVectorDistanceMeasure
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2017 Juergen Bernard
 * </p>
 * 
 * @author Christian Ritter
 * @version 1.01
 */
public class WeightedMixedDataFeatureVectorDistanceMeasure extends WeightedDistanceMeasure<MixedDataFeatureVector> {
	private WeightedDistanceMeasure<double[]> doubleDistanceMeasure;
	private WeightedDistanceMeasure<Boolean[]> booleanDistanceMeasure;
	private WeightedDistanceMeasure<String[]> stringDistanceMeasure;
	private List<Double> doubleWeights;
	private List<Double> stringWeights;
	private List<Double> booleanWeights;

	public WeightedMixedDataFeatureVectorDistanceMeasure(WeightedDistanceMeasure<double[]> doubleDistanceMeasure, WeightedDistanceMeasure<Boolean[]> booleanDistanceMeasure, WeightedDistanceMeasure<String[]> stringDistanceMeasure) {
		this(null, doubleDistanceMeasure, booleanDistanceMeasure, stringDistanceMeasure);
	}

	public WeightedMixedDataFeatureVectorDistanceMeasure(WeightedDistanceMeasure<double[]> doubleDistanceMeasure, WeightedDistanceMeasure<Boolean[]> booleanDistanceMeasure, WeightedDistanceMeasure<String[]> stringDistanceMeasure, double nullValue) {
		this(null, doubleDistanceMeasure, booleanDistanceMeasure, stringDistanceMeasure, nullValue);
	}

	public WeightedMixedDataFeatureVectorDistanceMeasure(List<Double> weights, WeightedDistanceMeasure<double[]> doubleDistanceMeasure, WeightedDistanceMeasure<Boolean[]> booleanDistanceMeasure,
			WeightedDistanceMeasure<String[]> stringDistanceMeasure) {
		super(weights);
		this.doubleDistanceMeasure = doubleDistanceMeasure;
		this.booleanDistanceMeasure = booleanDistanceMeasure;
		this.stringDistanceMeasure = stringDistanceMeasure;
	}

	public WeightedMixedDataFeatureVectorDistanceMeasure(List<Double> weights, WeightedDistanceMeasure<double[]> doubleDistanceMeasure, WeightedDistanceMeasure<Boolean[]> booleanDistanceMeasure,
			WeightedDistanceMeasure<String[]> stringDistanceMeasure, double nullValue) {
		super(weights, nullValue);
		this.doubleDistanceMeasure = doubleDistanceMeasure;
		this.booleanDistanceMeasure = booleanDistanceMeasure;
		this.stringDistanceMeasure = stringDistanceMeasure;
	}

	@Override
	public String getName() {
		return "MixedDataFeatureVectorDistanceMeasure";
	}

	@Override
	public String getDescription() {
		return "Combines different distance measures for each feature type respectively.";
	}

	@Override
	public double getDistance(MixedDataFeatureVector o1, MixedDataFeatureVector o2) {
		double numWeight = doubleWeights != null ? doubleWeights.stream().reduce(0.0, ((x, y) -> x + y)) : 1.0;
		double catWeight = stringWeights != null ? stringWeights.stream().reduce(0.0, ((x, y) -> x + y)) : 1.0;
		double binWeight = booleanWeights != null ? booleanWeights.stream().reduce(0.0, ((x, y) -> x + y)) : 1.0;
		double sumWeight = numWeight + catWeight + binWeight;
		
		double[] darr1 = o1.getVectorRepresentation().stream().filter(x -> x.getFeatureType() == FeatureType.DOUBLE).map(x -> (Double) x.getFeatureValue()).mapToDouble(Double::doubleValue).toArray();
		double[] darr2 = o2.getVectorRepresentation().stream().filter(x -> x.getFeatureType() == FeatureType.DOUBLE).map(x -> (Double) x.getFeatureValue()).mapToDouble(Double::doubleValue).toArray();
		Boolean[] barr1 = o1.getVectorRepresentation().stream().filter(x -> x.getFeatureType() == FeatureType.BOOLEAN).map(x -> (Boolean) x.getFeatureValue()).toArray(Boolean[]::new);
		Boolean[] barr2 = o2.getVectorRepresentation().stream().filter(x -> x.getFeatureType() == FeatureType.BOOLEAN).map(x -> (Boolean) x.getFeatureValue()).toArray(Boolean[]::new);
		String[] sarr1 = o1.getVectorRepresentation().stream().filter(x -> x.getFeatureType() == FeatureType.STRING).map(x -> (String) x.getFeatureValue()).toArray(String[]::new);
		String[] sarr2 = o2.getVectorRepresentation().stream().filter(x -> x.getFeatureType() == FeatureType.STRING).map(x -> (String) x.getFeatureValue()).toArray(String[]::new);

		double dVal = doubleDistanceMeasure.getDistance(darr1, darr2);
		double sVal = stringDistanceMeasure.getDistance(sarr1, sarr2);
		double bVal = booleanDistanceMeasure.getDistance(barr1, barr2);

		if (Double.isNaN(dVal))
			dVal = getNullValue();
		if (Double.isNaN(sVal))
			sVal = getNullValue();
		if (Double.isNaN(bVal))
			bVal = getNullValue();

		if (getWeights() != null && getWeights().size() == 3) {
			dVal *= getWeights().get(0);
			bVal *= getWeights().get(1);
			sVal *= getWeights().get(2);
		}

		return (numWeight / sumWeight * dVal + catWeight / sumWeight * sVal + binWeight / sumWeight * bVal);
	}

	/**
	 * @return the doubleWeights
	 */
	public List<Double> getDoubleWeights() {
		return doubleWeights;
	}

	/**
	 * @param doubleWeights
	 *            the doubleWeights to set
	 */
	public void setDoubleWeights(List<Double> doubleWeights) {
		this.doubleWeights = doubleWeights;
	}

	/**
	 * @return the stringWeights
	 */
	public List<Double> getStringWeights() {
		return stringWeights;
	}

	/**
	 * @param stringWeights
	 *            the stringWeights to set
	 */
	public void setStringWeights(List<Double> stringWeights) {
		this.stringWeights = stringWeights;
	}

	/**
	 * @return the booleanWeights
	 */
	public List<Double> getBooleanWeights() {
		return booleanWeights;
	}

	/**
	 * @param booleanWeights
	 *            the booleanWeights to set
	 */
	public void setBooleanWeights(List<Double> booleanWeights) {
		this.booleanWeights = booleanWeights;
	}
}
