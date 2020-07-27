package com.github.TKnudsen.ComplexDataObject.model.distanceMeasure;

import java.util.List;

/**
 * <p>
 * Title: WeightedDistanceMeasure
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public abstract class WeightedDistanceMeasure<T> implements IWeightedDistanceMeasure<T> {
	private double nullValue;
	private List<Double> weights;

	public WeightedDistanceMeasure(List<Double> weights) {
		this(weights, 0.5);
	}
	
	public WeightedDistanceMeasure(List<Double> weights, double nullValue){
		setWeights(weights);
		setNullValue(nullValue);
	}
	
	public double applyAsDouble(T t, T u) {
		return getDistance(t, u);
	}

	/**
	 * @return the nullValue
	 */
	public double getNullValue() {
		return nullValue;
	}

	@Override
	public List<Double> getWeights() {
		return weights;
	}

	/**
	 * @param nullValue the nullValue to set
	 */
	public void setNullValue(double nullValue) {
		this.nullValue = nullValue;
	}

	public void setWeights(List<Double> weights) {
		this.weights = weights;
	}

}
