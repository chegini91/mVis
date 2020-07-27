package com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.Double.probabilities;

import com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.Double.DoubleDistanceMeasure;
import com.github.TKnudsen.ComplexDataObject.model.statistics.KullbackLeiblerDivergence;

/**
 * <p>
 * Title: KullbackLeiblerDivergenceDistance
 * </p>
 * 
 * <p>
 * Description: Measure for the difference between two probability
 * distributions, also referred to as relative Entropy. Applies the square root
 * on the original Kullback Leibler Divergence.
 * 
 * References, according to Wikipedia:
 * 
 * Kullback, S.; Leibler, R.A. (1951). "On information and sufficiency". Annals
 * of Mathematical Statistics. 22 (1): 79–86. doi:10.1214/aoms/1177729694. MR
 * 0039968.
 * 
 * Kullback, S. (1959), Information Theory and Statistics, John Wiley & Sons.
 * Republished by Dover Publications in 1968; reprinted in 1978: ISBN
 * 0-8446-5625-9.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2018, https://github.com/TKnudsen/ComplexDataObject
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.03
 */
public class KullbackLeiblerDivergenceDistance extends DoubleDistanceMeasure {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1140421426076433023L;

	/**
	 * KL divergence returns infinity if one given probability is 0.
	 */
	private boolean handleZeroToInfinityProblem = true;

	private static double zeroReplacement = 0.00001;

	public KullbackLeiblerDivergenceDistance() {
		this(false);
	}

	public KullbackLeiblerDivergenceDistance(boolean handleZeroToInfinityProblem) {
		this.handleZeroToInfinityProblem = handleZeroToInfinityProblem;
	}

	@Override
	public double getDistance(double[] o1, double[] o2) {
		if (o1 == null || o2 == null)
			return Double.NaN;

		if (o1.length != o2.length)
			throw new IllegalArgumentException(getName() + ": given arrays have different length");

		double kullbackLeiblerDivergence = KullbackLeiblerDivergence.getKullbackLeiblerDivergence(o1, o2,
				handleZeroToInfinityProblem);

		return Math.sqrt(Math.abs(kullbackLeiblerDivergence));
	}

	@Override
	public String getName() {
		return "Kullback Leibler Divergence Distance";
	}

	@Override
	public String getDescription() {
		return "Measure for the difference between two probability distributions";
	}

	public boolean isHandleZeroToInfinityProblem() {
		return handleZeroToInfinityProblem;
	}

	public void setHandleZeroToInfinityProblem(boolean handleZeroToInfinityProblem) {
		this.handleZeroToInfinityProblem = handleZeroToInfinityProblem;
	}
}
