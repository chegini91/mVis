package com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.Double;

import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.ManhattanDistance;

/**
 * <p>
 * Title: ManhattanDistanceMeasure
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
public class ManhattanDistanceMeasure extends DoubleDistanceMeasure {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6401822601454203919L;

	DistanceMeasure distanceMeasure = new ManhattanDistance();

	@Override
	public double getDistance(double[] o1, double[] o2) {
		return distanceMeasure.compute(o1, o2);
	}

	@Override
	public String getName() {
		return "Manhattan Distance Measure";
	}

	@Override
	public String getDescription() {
		return getName() + ": represents the Minkovski distance with exponent 1";
	}
}
