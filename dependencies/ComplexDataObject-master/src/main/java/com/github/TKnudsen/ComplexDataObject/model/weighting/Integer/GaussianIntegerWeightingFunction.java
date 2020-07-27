package com.github.TKnudsen.ComplexDataObject.model.weighting.Integer;

/**
 * <p>
 * Title: GaussianWeightingFunction
 * </p>
 * 
 * <p>
 * Description: Provides a weighting for a given interval, the center of the
 * values is defined by a reference (default: 0.0). The variance defines the
 * diversity of the kernel.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2016
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.0
 */
public class GaussianIntegerWeightingFunction implements IIntegerWeightingKernel {

	private Integer interval = Integer.MAX_VALUE;
	private Integer reference = 0;
	private Double variance;

	/**
	 * for serialization
	 */
	public GaussianIntegerWeightingFunction() {
		this.variance = 1.0;
	}

	public GaussianIntegerWeightingFunction(Double variance) {
		this.variance = variance;
	}

	@Override
	public double getWeight(Integer t) {
		if (Math.abs(reference - t) > interval)
			return 0.0;

		if (interval == 0)
			return 1.0;

		return gaussian(t);
	}

	/**
	 * 
	 * @param a
	 * @param b
	 * @param x
	 * @return
	 */
	public double gaussian(double x) {
		double a = 1 / (variance * Math.sqrt(2 * Math.PI));
		return a * Math.exp(-Math.pow((x - reference), 2) / (2 * Math.pow(variance, 2)));
	}

	@Override
	public Integer getReference() {
		return reference;
	}

	@Override
	public void setReference(Integer t) {
		this.reference = t;
	}

	public Double getVariance() {
		return variance;
	}

	public void setVariance(Double variance) {
		this.variance = variance;
	}

	@Override
	public Integer getInterval() {
		return this.interval;
	}

	@Override
	public void setInterval(Integer interval) {
		this.interval = interval;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof GaussianIntegerWeightingFunction))
			return false;

		GaussianIntegerWeightingFunction other = (GaussianIntegerWeightingFunction) o;

		return other.interval == interval && other.reference == reference && other.variance == variance;
	}

}
