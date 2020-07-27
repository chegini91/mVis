package com.github.TKnudsen.ComplexDataObject.model.statistics;

import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * Title: Entropy
 * </p>
 *
 * <p>
 * Description: calculates the entropy for various value distributions.
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2016-2017
 * </p>
 *
 * @author Juergen Bernard
 * @version 1.01
 */
public class Entropy {

	public static double calculateEntropy(Map<String, Double> labelDistribution) {
		if (labelDistribution == null || labelDistribution.size() == 0)
			return 0;

		return calculateEntropy(labelDistribution.values());
	}

	public static double calculateEntropy(Collection<Double> distribution) {
		if (distribution == null || distribution.size() == 0)
			return 0;

		double entropy = 0.0;
		for (Double d : distribution)
			if (d > 0)
				entropy -= (d * Math.log(d));

		entropy /= Math.log(2.0);

		return entropy;
	}
}
