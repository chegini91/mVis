package com.github.TKnudsen.ComplexDataObject.model.statistics;

import com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.Double.probabilities.KullbackLeiblerDivergenceDistance;

/**
 * <p>
 * Title: JensenShannonDivergence
 * </p>
 *
 * <p>
 * Description: calculates the Jensen Shannon Divergence - a measure to assess
 * the difference between to probability distibutions.
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 *
 * @author Juergen Bernard
 * @version 1.01
 */
public class JensenShannonDivergence {

	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static double jensenShannonDivergence(double[] a, double[] b) {
		double[] med = new double[a.length];

		for (int i = 0; i < med.length; i++)
			med[i] = (a[i] + b[i]) / 2;

		KullbackLeiblerDivergenceDistance kld = new KullbackLeiblerDivergenceDistance(true);

		return (kld.dist(a, med) + kld.dist(b, med)) * 0.5;
	}

}
