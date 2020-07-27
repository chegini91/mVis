package com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.String;

import java.util.List;
import java.util.Map;

import com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.WeightedDistanceMeasure;

/**
 * <p>
 * Title: WeightedGoodAll1DistanceMeasure
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
public class WeightedGoodAll1DistanceMeasure extends WeightedDistanceMeasure<String[]> {
	private List<Map<String, Double>> pSquares;

	public WeightedGoodAll1DistanceMeasure(List<Double> weights, List<Map<String, Double>> pSquares) {
		super(weights);
		this.pSquares = pSquares;
	}

	@Override
	public String getDescription() {
		return "Calculates the weighted Goodall1 distance.";
	}

	@Override
	public double getDistance(String[] o1, String[] o2) {
		int numAttr1 = o1.length;
		int numAttr2 = o2.length;
		// check if arrays have same length
		if (numAttr1 != numAttr2 || numAttr1 != getWeights().size()) {
			try {
				throw new Exception("The Arrays have different Sizes.");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return Double.NaN;
		}

		double similarity = 0;
		double weightSum = 0;
		for (int i = 0; i < numAttr1; i++) {
			weightSum += getWeights().get(i);
		}

		double[] internalWeights = new double[numAttr1];
		for (int i = 0; i < numAttr1; i++) {
			if (weightSum != 0)
				internalWeights[i] = getWeights().get(i) / weightSum;
			else
				internalWeights[i] = 0;
		}

		double step;

		for (int i = 0; i < numAttr1; i++) {
			step = 0;
			if (o1[i] != null && o2[i] != null && o1[i].equals(o2[i])) {
				double sumPSquares = 0;

				for (double ps : pSquares.get(i).values())
					sumPSquares = sumPSquares + ps;

				step = 1 - sumPSquares;
			} else if (o1[i] == null || o2[i] == null) {
				double sumPSquares = 0;

				for (double ps : pSquares.get(i).values())
					sumPSquares = sumPSquares + ps;

				step = 1 - (1 - getNullValue()) * sumPSquares;
			}
			similarity += internalWeights[i] * step;
		}
		return 1 - similarity;
	}

	@Override
	public String getName() {
		return "WeightedGoodall1";
	}
}
