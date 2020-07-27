package com.github.TKnudsen.ComplexDataObject.model.correlation;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

/**
 * <p>
 * Title: PearsonsCorrelationMeasure
 * </p>
 * 
 * <p>
 * Description: measures the Pearson correlation for two given arrays.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2017-2018,
 * https://github.com/TKnudsen/ComplexDataObject
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class PearsonsCorrelationMeasure {
	PearsonsCorrelation correlationMeasure = new PearsonsCorrelation();

	/**
	 * Measures the Pearson rank correlation for two given arrays. throws a
	 * DimensionMismatchException if the two arrays do not have the same length (and
	 * are not null).
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public double correlation(double[] a, double[] b) {
		if (a == null || b == null)
			return Double.NaN;

		if (a.length != b.length)
			throw new DimensionMismatchException(a.length, b.length);

		return correlationMeasure.correlation(a, b);
	}
}
