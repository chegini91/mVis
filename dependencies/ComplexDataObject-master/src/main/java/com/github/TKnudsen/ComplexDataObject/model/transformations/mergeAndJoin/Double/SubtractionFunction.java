package com.github.TKnudsen.ComplexDataObject.model.transformations.mergeAndJoin.Double;

import com.github.TKnudsen.ComplexDataObject.model.transformations.mergeAndJoin.IObjectMerger;

/**
 * <p>
 * Title: SubtractionFunction
 * </p>
 * 
 * <p>
 * Description: provides the difference between two doubles.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class SubtractionFunction implements IObjectMerger<Double> {

	private boolean ignoreNAN = false;

	public SubtractionFunction() {
	}

	public SubtractionFunction(boolean ignoreNAN) {
		this.ignoreNAN = ignoreNAN;
	}

	@Override
	public Double merge(Double object1, Double object2) {
		if (Double.isNaN(object1) && Double.isNaN(object2))
			return Double.NaN;

		if (ignoreNAN)
			if (Double.isNaN(object1))
				return 0.0 - object2;
			else if (Double.isNaN(object2))
				return object1 - 0.0;

		if (Double.isNaN(object1) || Double.isNaN(object2))
			return Double.NaN;

		return object1 - object2;
	}

	public boolean isIgnoreNAN() {
		return ignoreNAN;
	}

	public void setIgnoreNAN(boolean ignoreNAN) {
		this.ignoreNAN = ignoreNAN;
	}

}
