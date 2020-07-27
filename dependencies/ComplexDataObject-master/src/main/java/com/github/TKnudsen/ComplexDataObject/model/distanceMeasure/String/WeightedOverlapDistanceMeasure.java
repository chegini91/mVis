package com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.String;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.WeightedDistanceMeasure;

/**
 * <p>
 * Title: WeightedOverlapDistanceMeasure
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
public class WeightedOverlapDistanceMeasure extends WeightedDistanceMeasure<String[]> {

	public WeightedOverlapDistanceMeasure(List<Double> weights) {
		super(weights);
	}

	@Override
	public String getDescription() {
		return "WeightedOverlapDistanceMeasure";
	}

	@Override
	public double getDistance(String[] o1, String[] o2) {
		// check if arrays have same length
		if (o1.length != o2.length || o1.length != getWeights().size()) {
			try {
				throw new Exception("The Arrays have different Sizes.");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return Double.NaN;
		}

		double similarity = 0;
		double weightSum = 0;
		for (int i = 0; i < o1.length; i++) {
			weightSum += getWeights().get(i);
		}

		double[] internalWeights = new double[o1.length];
		for (int i = 0; i < o1.length; i++) {
			if (weightSum != 0)
				internalWeights[i] = getWeights().get(i) / weightSum;
			else
				internalWeights[i] = 0;
		}

		for (int i = 0; i < o1.length; i++) {
			if (o1[i] == null || o2[i] == null)
				similarity += internalWeights[i] * (1 - getNullValue());
			else
				similarity += internalWeights[i] * ((o1[i].equals(o2[i])) ? 1 : 0);
		}
		return 1 - similarity;
	}

	@Override
	public String getName() {
		return "WeightedOverlapDistanceMeasure";
	}

}
