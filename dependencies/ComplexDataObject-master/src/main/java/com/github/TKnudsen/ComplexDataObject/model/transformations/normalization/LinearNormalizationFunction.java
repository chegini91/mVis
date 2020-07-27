package com.github.TKnudsen.ComplexDataObject.model.transformations.normalization;

import java.util.Collection;

import com.github.TKnudsen.ComplexDataObject.model.tools.MathFunctions;
import com.github.TKnudsen.ComplexDataObject.model.tools.StatisticsSupport;

/**
 * <p>
 * Title: LinearNormalizationFunction
 * </p>
 * 
 * <p>
 * Description: scales a value into the interval [0...1]. Scaling can be based
 * on a given value interval [global min max] or based on a given value
 * distribution.
 * 
 * Setting new bounds is possible (even if previously calculated values will be
 * obsolete then). Reason: keeping the instance alive in value-dynamic contexts.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2016-2018
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.05
 */
public class LinearNormalizationFunction extends NormalizationFunction {

	public LinearNormalizationFunction(Collection<? extends Number> values) {
		super(values);
	}

	public LinearNormalizationFunction(Collection<? extends Number> values, boolean limitToBounds) {
		super(values, limitToBounds);
	}

	public LinearNormalizationFunction(StatisticsSupport statisticsSupport) {
		super(statisticsSupport);
	}

	public LinearNormalizationFunction(StatisticsSupport statisticsSupport, boolean limitToBounds) {
		super(statisticsSupport, limitToBounds);
	}

	public LinearNormalizationFunction(Number globalMin, Number globalMax) {
		super(globalMin, globalMax);
	}

	public LinearNormalizationFunction(Number globalMin, Number globalMax, boolean limitToBounds) {
		super(globalMin, globalMax, limitToBounds);
	}

	@Override
	public Number apply(Number value) {
		double scaled = MathFunctions.linearScale(getGlobalMin().doubleValue(), getGlobalMax().doubleValue(),
				value.doubleValue(), isLimitToBounds());

		return new Double(scaled);
	}

}