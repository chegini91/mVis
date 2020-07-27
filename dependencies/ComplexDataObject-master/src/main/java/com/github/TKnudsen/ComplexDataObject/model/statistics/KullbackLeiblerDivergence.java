package com.github.TKnudsen.ComplexDataObject.model.statistics;

/**
 * <p>
 * Title: KullbackLeiblerDivergence
 * </p>
 * 
 * <p>
 * Description: Measure for the difference between two probability
 * distributions, also referred to as relative Entropy.
 * 
 * References, according to Wikipedia:
 * 
 * Kullback, S.; Leibler, R.A. (1951). "On information and sufficiency". Annals
 * of Mathematical Statistics. 22 (1): 79–86. doi:10.1214/aoms/1177729694. MR
 * 0039968.
 * 
 * Kullback, S. (1959), Information Theory and Statistics, John Wiley & Sons.
 * Republished by Dover Publications in 1968; reprinted in 1978: ISBN
 * 0-8446-5625-9.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2018, https://github.com/TKnudsen/ComplexDataObject
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 */
public class KullbackLeiblerDivergence {

	private static double zeroReplacement = 0.00001;

	public static double getKullbackLeiblerDivergence(double[] o1, double[] o2, boolean handleZeroToInfinityProblem) {
		if (o1 == null || o2 == null)
			return Double.NaN;

		if (o1.length != o2.length)
			throw new IllegalArgumentException("KullbackLeiblerDivergence: given arrays have different length");

		double klDivergence = 0.0;

		for (int i = 0; i < o1.length; ++i) {
			double a = o1[i];
			if (a == 0)
				if (handleZeroToInfinityProblem)
					a = zeroReplacement;
				else
					return Double.POSITIVE_INFINITY;

			double b = o2[i];
			if (b == 0.0)
				if (handleZeroToInfinityProblem)
					b = zeroReplacement;
				else
					return Double.POSITIVE_INFINITY;

			klDivergence += a * Math.log(a / b);
		}

		return klDivergence;
	}

}
