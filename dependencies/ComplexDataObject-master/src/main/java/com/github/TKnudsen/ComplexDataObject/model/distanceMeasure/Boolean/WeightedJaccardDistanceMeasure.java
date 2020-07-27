package com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.Boolean;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.WeightedDistanceMeasure;

/**
 * <p>
 * Title: WeightedJaccardDistanceMeasure
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
 * @author Christian Ritter, J�rgen Bernard
 * @version 1.01
 */
public class WeightedJaccardDistanceMeasure extends WeightedDistanceMeasure<Boolean[]> {

	private List<Double> weights;

	public WeightedJaccardDistanceMeasure(List<Double> weights) {
		super(weights);
	}
	
	public WeightedJaccardDistanceMeasure(List<Double> weights, double nullValue) {
		super(weights, nullValue);
	}

	@Override
	public String getDescription() {
		return "Calculates weighted Jaccard distance, i.e. the complementary of the intersection over union of two sets.";
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

		double union = 0;
		double xor = 0;

		int length = o1.length;

		for (int i = 0; i < length; i++) {
			if (o1[i] == null || o2[i] == null)
				union += (1 - getNullValue()) * getWeights().get(i);
			else if (o1[i] && o2[i])
				union += getWeights().get(i);
			else if (!o1[i] && o2[i] || o1[i] && !o2[i])
				xor += getWeights().get(i);
		}

		if ((union + xor) == 0)
			return 0;

		return 1 - (union / (union + xor));
	}

	@Override
	public String getName() {
		return "WeightedJaccard";
	}

	@Override
	public List<Double> getWeights() {
		return weights;
	}

	public void setWeights(List<Double> weights) {
		this.weights = weights;
	}

}
