package com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.Double;

import java.io.Serializable;

import com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.IDistanceMeasure;

/**
 * <p>
 * Title: DoubleDistanceMeasure
 * </p>
 * 
 * <p>
 * Description: Basic class for all double[] distance measures.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2017-2018,
 * https://github.com/TKnudsen/ComplexDataObject
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.05
 */
public abstract class DoubleDistanceMeasure implements IDistanceMeasure<double[]>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2559705521219780141L;

	public double applyAsDouble(double[] t, double[] u) {
		return getDistance(t, u);
	}

	public double dist(double[] a, double[] b) {
		return getDistance(a, b);
	}
}
