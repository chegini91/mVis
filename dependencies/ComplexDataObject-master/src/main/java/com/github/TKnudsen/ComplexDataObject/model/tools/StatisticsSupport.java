package com.github.TKnudsen.ComplexDataObject.model.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.google.common.primitives.Doubles;

/**
 * <p>
 * Title: StatisticsSupport
 * </p>
 * 
 * <p>
 * Description: extension of apache commons math stat descriptive
 * DescriptiveStatistics. DescriptiveStatistics maintains the input data in
 * memory and has the capability of producing "rolling" statistics computed from
 * a "window" consisting of the most recently added values.
 * 
 * Aggregate Statistics Included: min, max, mean, geometric mean, n, sum, sum of
 * squares, standard deviation, variance, percentiles, skewness, kurtosis,
 * median
 * 
 * "Rolling" capability? Yes
 * 
 * Values stored? Yes
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2012-2018
 * </p>
 * 
 * @author Juergen Bernard
 *
 */
public class StatisticsSupport extends DescriptiveStatistics implements Iterable<Double> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4338838213221265737L;

	private double median = Double.NaN;
	private int count = -1;
	private int uniqueObservations = -1;

	/**
	 * NANs are removed!
	 * 
	 * @param vector
	 */
	public StatisticsSupport(Number[] vector) {
		for (int i = 0; i < vector.length; i++)
			if (!Double.isNaN(vector[i].doubleValue()))
				addValue(vector[i].doubleValue());
	}

	/**
	 * NANs are removed!
	 * 
	 * @param vector
	 */
	public StatisticsSupport(double[] vector) {
		for (int i = 0; i < vector.length; i++)
			if (!Double.isNaN(vector[i]))
				addValue(vector[i]);
	}

	/**
	 * NANs are removed!
	 * 
	 * @param values
	 */
	public StatisticsSupport(Collection<? extends Number> values) {
		Iterator<? extends Number> iterator = values.iterator();
		while (iterator.hasNext()) {
			Number n = iterator.next();
			if (n != null) {
				Double d = n.doubleValue();
				if (!Double.isNaN(d))
					addValue(d);
			}
		}
	}

	/**
	 * NANs are removed!
	 * 
	 * @param values
	 */
	public StatisticsSupport(List<Double> values) {
		for (int i = 0; i < values.size(); i++)
			if (!Double.isNaN(values.get(i)))
				addValue(values.get(i));
	}

	/**
	 * NANs are removed!
	 * 
	 * @param values
	 */
	public StatisticsSupport(Set<Double> values) {
		Iterator<Double> iterator = values.iterator();
		while (iterator.hasNext()) {
			double d = iterator.next();
			if (!Double.isNaN(d))
				addValue(d);
		}
	}

	/**
	 * 
	 * @param quantile
	 *            the median is called by typing 50.0, not by 0.5!
	 * @return
	 */
	public double[] getOutliers(double quantile) {
		double dNotOutmin = this.getPercentile(quantile);
		double dNotOutmax = this.getPercentile(100.0 - quantile);

		List<Double> outliers = new ArrayList<>();
		for (int i = 0; i < getValues().length; i++)
			if (getValues()[i] < dNotOutmin || getValues()[i] > dNotOutmax)
				outliers.add(getValues()[i]);

		double[] ret = new double[outliers.size()];
		for (int i = 0; i < outliers.size(); i++)
			ret[i] = outliers.get(i);
		return ret;
	}

	private void resetValues() {
		this.median = Double.NaN;
		this.count = -1;
		uniqueObservations = -1;
	}

	@Override
	public void addValue(double v) {
		resetValues();
		super.addValue(v);
	}

	/**
	 * Adds a list of values. may be slow for large data sets. Requires testing.
	 * 
	 * @param values
	 */
	public void addAll(List<Double> values) {
		for (Double d : values)
			addValue(d);
	}

	public double getMedian() {
		if (Double.isNaN(median))
			median = getPercentile(50);
		return median;
	}

	public double getMean() {
		return super.getMean();
	}

	public int getCount() {
		if (count == -1)
			count = getValues().length;
		return count;
	}

	@Override
	public double getMax() {
		return super.getMax();
	}

	@Override
	public double getMin() {
		return super.getMin();
	}
	
	/**
	 * Must be between 0 and 100
	 * 
	 * @param percent
	 * @return
	 */
	public double getPercentile(int percent) {
		return getPercentile((double) percent);
	}

	/**
	 * Must be between 0 and 100
	 * 
	 * @param percent
	 * @return
	 */
	public double getPercentile(double percent) {
		if (percent <= 0)
			return getMin();
		if (percent >= 100)
			return getMax();
		return super.getPercentile((double) percent);
	}

	@Override
	public double getStandardDeviation() {
		return super.getStandardDeviation();
	}

	@Override
	public double getVariance() {
		return super.getVariance();
	}

	/**
	 * Number of different values
	 * 
	 * @return
	 */
	public int getCountUniqueObservations() {
		if (uniqueObservations == -1) {
			List<Double> list = Doubles.asList(getValues());
			@SuppressWarnings({ "rawtypes", "unchecked" })
			Set uniqueValues = new HashSet(list);
			uniqueObservations = uniqueValues.size();
		}
		return uniqueObservations;
	}

	@Override
	public double[] getValues() {
		return super.getValues();
	}

	/**
	 * Predicts if a given variable is discrete by examining the ratio
	 * #uniques/#elements.
	 * 
	 * @param percent
	 *            the variable which values are checked.
	 * @return true if the ratio is smaller than the given parameter (0.01 means
	 *         1%).
	 */
	public boolean isLikelyDiscrete(double ratio) {
		int uniqueObservations = getCountUniqueObservations();

		double ratioObserved = (double) uniqueObservations / getValues().length;

		return (ratioObserved < ratio);
	}

	@Override
	public Iterator<Double> iterator() {
		return Doubles.asList(getValues()).iterator();
	}

}
