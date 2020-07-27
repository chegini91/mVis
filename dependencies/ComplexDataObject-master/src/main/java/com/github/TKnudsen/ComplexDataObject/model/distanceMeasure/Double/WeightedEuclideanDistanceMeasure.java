package com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.Double;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.WeightedDistanceMeasure;

/**
 * <p>
 * Title: WeightedEuclideanDistanceMeasure
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
public class WeightedEuclideanDistanceMeasure extends WeightedDistanceMeasure<double[]> {

	public WeightedEuclideanDistanceMeasure(List<Double> weights) {
		super(weights);
	}

	public WeightedEuclideanDistanceMeasure(List<Double> weights, double nullValue) {
		super(weights, nullValue);
	}

	@Override
	public String getDescription() {
		return "Calculated the weighted Euclidean distance.";
	}

	@Override
	public double getDistance(double[] o1, double[] o2) {
		if (o1.length != o2.length || o1.length != getWeights().size())
			throw new IllegalArgumentException(getName() + ": given arrays have different length");

		int length = o1.length;
		double result = 0;

		for (int i = 0; i < length; i++) {
			if (!Double.isNaN(o1[i] + o2[i]))
				result += getWeights().get(i) * Math.pow((o1[i] - o2[i]), 2);
			else
				result += getNullValue() * getWeights().get(i);
		}

		return Math.sqrt(result);
	}

	@Override
	public String getName() {
		return "WeightedEuclidean";
	}

}