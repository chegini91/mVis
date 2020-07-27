package com.github.TKnudsen.ComplexDataObject.model.processors.features.numericalData;

import java.util.List;
import java.util.Map.Entry;

import com.github.TKnudsen.ComplexDataObject.data.entry.EntryWithComparableKey;
import com.github.TKnudsen.ComplexDataObject.model.tools.StatisticsSupport;

/**
 * <p>
 * Title: StandardDeviationOutlierTreatment
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * @author Christian Ritter
 * @version 1.01
 */
public class StandardDeviationOutlierTreatment extends AbstractOutlierTreatment {

	private double multipleOfStandardDeviation;

	/**
	 * 
	 */
	public StandardDeviationOutlierTreatment() {
		this(2.96);
	}

	/**
	 * Removes/crops all feature values that are more than the given multiple of standard deviation away from the mean of their respective distribution.
	 * 
	 * @param d
	 */
	public StandardDeviationOutlierTreatment(double multipleOfStandardDeviation) {
		this.multipleOfStandardDeviation = multipleOfStandardDeviation;
	}

	@Override
	protected Entry<Double, Double> calculateBounds(List<Double> values) {
		StatisticsSupport statisticsSupport = new StatisticsSupport(values);
		double mean = statisticsSupport.getMean();
		double standardDeviation = statisticsSupport.getStandardDeviation();
		double min = statisticsSupport.getMin();
		double max = statisticsSupport.getMax();
		return new EntryWithComparableKey<>(Math.max(min, mean - multipleOfStandardDeviation * standardDeviation), Math.min(max, mean + multipleOfStandardDeviation * standardDeviation));
	}

}
