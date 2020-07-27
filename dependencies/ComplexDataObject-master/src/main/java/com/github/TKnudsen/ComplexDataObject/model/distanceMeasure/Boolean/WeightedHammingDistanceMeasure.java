package com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.Boolean;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.WeightedDistanceMeasure;

/**
 * <p>
 * Title: WeightedHammingDistanceMeasure
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
public class WeightedHammingDistanceMeasure extends WeightedDistanceMeasure<Boolean[]> {

	public WeightedHammingDistanceMeasure(List<Double> weights) {
		super(weights);
	}

	public WeightedHammingDistanceMeasure(List<Double> weights, double nullValue) {
		super(weights, nullValue);
	}

	@Override
	public String getDescription() {
		return "WeightedHammingDistanceMeasure";
	}

	@Override
	public double getDistance(Boolean[] o1, Boolean[] o2) {
		// check if arrays have same length
		if (o1.length != o2.length || o1.length != getWeights().size()) {
			try {
				throw new Exception("The Arrays have different Sizes.");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return Double.NaN;
		}

		int length = o1.length;
		double result = 0;
		double weightSum = 0;
		for (int i = 0; i < length; i++) {
			weightSum += getWeights().get(i);
		}

		double[] internalWeights = new double[length];
		for (int i = 0; i < length; i++) {
			internalWeights[i] = getWeights().get(i) / weightSum;
		}

		// sum b_i + c_i
		for (int i = 0; i < length; i++) {
			double xoredValues;
			if (o1[i] == null || o2[i] == null)
				xoredValues = getNullValue();
			else
				xoredValues = (o1[i] ^ o2[i]) ? 1.0 : 0.0;
			result += internalWeights[i] * xoredValues;
		}

		return result;
	}

	@Override
	public String getName() {
		return "WeightedHammingDistanceMeasure";
	}

}
