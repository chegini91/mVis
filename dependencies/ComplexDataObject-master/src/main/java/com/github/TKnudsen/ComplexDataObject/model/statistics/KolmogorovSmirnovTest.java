package com.github.TKnudsen.ComplexDataObject.model.statistics;

public class KolmogorovSmirnovTest {

	/**
	 * Statistical test for the match of two given probability distributions.
	 * Computes the two-sample Kolmogorov-Smirnov test based on the implementation
	 * of apache commons.
	 * 
	 * @param probability1
	 * @param probability2
	 * @return
	 */
	public static double calculateKolmogorovSmirnov(double[] probability1, double[] probability2) {
		org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest test = new org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest();

		double kolmogorovSmirnovStatistic = test.kolmogorovSmirnovStatistic(probability1, probability2);

		return kolmogorovSmirnovStatistic;
	}
}
