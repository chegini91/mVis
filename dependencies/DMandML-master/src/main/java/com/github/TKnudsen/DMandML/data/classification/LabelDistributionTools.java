package com.github.TKnudsen.DMandML.data.classification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.github.TKnudsen.ComplexDataObject.data.entry.EntryWithComparableKey;
import com.github.TKnudsen.ComplexDataObject.data.ranking.Ranking;
import com.github.TKnudsen.ComplexDataObject.model.tools.MathFunctions;

/**
 * <p>
 * Title: LabelDistributionTools
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2015-2017
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.03
 */
public class LabelDistributionTools {

	/**
	 * merges a series of LabelDistributions.
	 * 
	 * @param labelDistributions
	 * @return
	 */
	public static LabelDistribution mergeLabelDistributions(Collection<LabelDistribution> labelDistributions) {
		if (labelDistributions == null)
			return null;

		Map<String, List<Double>> valueDistributions = new LinkedHashMap<>();

		for (LabelDistribution labelDistribution : labelDistributions) {
			if (labelDistribution == null)
				continue;

			for (String label : labelDistribution.getLabelSet()) {
				if (valueDistributions.get(label) == null)
					valueDistributions.put(label, new ArrayList<>());

				valueDistributions.get(label).add(labelDistribution.getValueDistribution().get(label));
			}
		}

		Map<String, Double> valueDistribution = new LinkedHashMap<>();

		for (String label : valueDistributions.keySet())
			valueDistribution.put(label, MathFunctions.getMean(valueDistributions.get(label)));

		return new LabelDistribution(valueDistribution);
	}

	/**
	 * Provides a new Map with distributions adding up to 1.
	 * 
	 * @return
	 */
	public static Map<String, Double> normalizeLabelDistribution(Map<String, Double> distribution) {
		if (distribution == null)
			return null;

		double sum = 0;
		for (String label : distribution.keySet())
			sum += distribution.get(label);

		if (Double.isNaN(sum)) {
			System.err.println("LabelDistributionTools.normalizeLabelDistribution applied with NaN values.");
			return null;
		}

		Map<String, Double> normalized = new LinkedHashMap<>();

		for (String label : normalized.keySet())
			normalized.put(label, normalized.get(label) / sum);

		return normalized;
	}

	public static String getSecondHighestLabel(LabelDistribution labelDistribution) {
		if (labelDistribution == null)
			throw new NullPointerException("LabelDistributionTools.getSecondHighestLabel: labelDistribution was null");

		if (labelDistribution.getValueDistribution().size() < 2)
			throw new IllegalArgumentException(
					"LabelDistributionTools.getSecondHighestLabel: labelDistribution did not contain two entries.");

		Ranking<EntryWithComparableKey<Double, String>> ranking = new Ranking<EntryWithComparableKey<Double, String>>();

		for (String label : labelDistribution.keySet())
			ranking.add(new EntryWithComparableKey<Double, String>(labelDistribution.getValueDistribution().get(label),
					label));

		return ranking.get(ranking.size() - 2).getValue();
	}
}
