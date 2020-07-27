package com.github.TKnudsen.ComplexDataObject.model.weighting.Integer;

/**
 * <p>
 * Title: LinearIndexWeightingKernel
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2016-2017
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 */
public class LinearIndexWeightingKernel implements IIntegerWeightingKernel {

	private Integer reference;
	private Integer interval;

	/**
	 * for serialization
	 */
	public LinearIndexWeightingKernel() {
		this.interval = 3;
	}

	public LinearIndexWeightingKernel(Integer interval) {
		this.interval = interval;
	}

	@Override
	public Integer getInterval() {
		return interval;
	}

	@Override
	public void setInterval(Integer t) {
		this.interval = t;
	}

	@Override
	public double getWeight(Integer t) {
		if (Math.abs(reference - t) > interval)
			return 0.0;

		if (interval == 0)
			return 1.0;

		return 1.0 - (Math.abs(reference - t) / (double) interval);
	}

	@Override
	public Integer getReference() {
		return reference;
	}

	@Override
	public void setReference(Integer t) {
		this.reference = t.intValue();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof LinearIndexWeightingKernel))
			return false;

		LinearIndexWeightingKernel other = (LinearIndexWeightingKernel) o;

		return other.interval == interval && other.reference == reference;
	}

}
