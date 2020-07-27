package com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.Double.probabilities;

import com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.Double.DoubleDistanceMeasure;
import com.github.TKnudsen.ComplexDataObject.model.statistics.JensenShannonDivergence;

/**
 * <p>
 * Title: JensenShannonDivergenceDistance
 * </p>
 * 
 * <p>
 * Description: Metric assessing distance between two probability distributions.
 * Builds up in Jensen Shannon Divergence, which, in turn, builds upon the
 * Kullback Leibler. The Jensen Shannon distances mitigates Kullback's problem
 * of infinite values (if one attribute is 0), though.
 * 
 * References
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2018, https://github.com/TKnudsen/ComplexDataObject
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class JensenShannonDivergenceDistance extends DoubleDistanceMeasure {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5659522174712724493L;

	public JensenShannonDivergenceDistance() {
	}

	@Override
	public double getDistance(double[] o1, double[] o2) {
		if (o1 == null || o2 == null)
			return Double.NaN;

		if (o1.length != o2.length)
			throw new IllegalArgumentException(getName() + ": given arrays have different length");

		double jensenShannonDivergence = JensenShannonDivergence.jensenShannonDivergence(o1, o2);

		return Math.sqrt(jensenShannonDivergence);
	}

	@Override
	public String getName() {
		return "Jensen Shannon Divergence Distance";
	}

	@Override
	public String getDescription() {
		return "Measure for the distance between two probability distributions";
	}

}
