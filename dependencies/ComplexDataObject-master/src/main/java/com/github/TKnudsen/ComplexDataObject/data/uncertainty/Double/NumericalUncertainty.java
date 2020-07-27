package com.github.TKnudsen.ComplexDataObject.data.uncertainty.Double;

import java.util.Collection;

import com.github.TKnudsen.ComplexDataObject.data.uncertainty.IUncertaintyQuantitative;
import com.github.TKnudsen.ComplexDataObject.model.tools.StatisticsSupport;

/**
 * <p>
 * Title: NumericalUncertainty
 * </p>
 * 
 * <p>
 * Description: data model for uncertainties of numerical values.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2015-2017
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 */
public class NumericalUncertainty implements IUncertaintyQuantitative<Double> {

	private Double min;
	private Double max;
	private Double representant;
	private double variation;

	public NumericalUncertainty(Collection<? extends Double> values) {
		initialize(values);
	}

	private void initialize(Collection<? extends Double> values) {
		StatisticsSupport statisticsSupport = new StatisticsSupport(values);

		this.min = statisticsSupport.getMin();
		this.max = statisticsSupport.getMax();
		this.representant = statisticsSupport.getMedian();
		this.variation = statisticsSupport.getVariance();
	}

	@Override
	public String toString() {
		return "NumericalUncertainty. min, max, representant, variation: " + min + ", " + max + ", " + representant
				+ ", " + variation;
	}

	@Override
	public Double getMostCertainRepresentant() {
		return representant;
	}

	@Override
	public Double getMinimum() {
		return min;
	}

	@Override
	public Double getMaximum() {
		return max;
	}

	@Override
	public double getVariation() {
		return variation;
	}

}
