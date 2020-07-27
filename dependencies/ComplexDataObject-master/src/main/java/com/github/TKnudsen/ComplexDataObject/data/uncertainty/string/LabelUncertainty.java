package com.github.TKnudsen.ComplexDataObject.data.uncertainty.string;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.github.TKnudsen.ComplexDataObject.data.uncertainty.IUncertaintyQualitative;

/**
 * <p>
 * Title: LabelUncertainty
 * </p>
 * 
 * <p>
 * Description: data model for uncertainties of string data. In general, high
 * values mean high uncertainty. This is incontrast to probability distributions
 * where high values mean high probabilities.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2015-2018
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 */
public class LabelUncertainty implements IUncertaintyQualitative<String> {

	private Map<String, Double> valueDistribution;

	private String representant;

	/**
	 * constructor for reflection-based and jackson-based access.
	 */
	@SuppressWarnings("unused")
	private LabelUncertainty() {
		this(null, null);
	}

	public LabelUncertainty(Map<String, Double> valueDistribution) {
		this(valueDistribution, null);
	}

	public LabelUncertainty(Map<String, Double> valueDistribution, String representant) {
		this.valueDistribution = valueDistribution;
		this.representant = representant;

		if (representant == null)
			this.representant = calculateMostCertainRepresentant();
	}

	/**
	 * The most meaningful representant is the value with the LEAST uncertainty.
	 * This notion is inverse to probability distributions (!).
	 * 
	 * @return
	 */
	private String calculateMostCertainRepresentant() {
		String rep = null;
		Double repRatio = Double.POSITIVE_INFINITY;

		if (valueDistribution == null)
			return null;

		for (String value : valueDistribution.keySet())
			if (valueDistribution.get(value) < repRatio) {
				rep = value;
				repRatio = valueDistribution.get(value);
			}

		return rep;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		Iterator<Entry<String, Double>> iterator = valueDistribution.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Double> probability = iterator.next();
			sb.append(probability.getKey());
			sb.append('=').append('"');
			sb.append(probability.getValue());
			sb.append('"');
			if (iterator.hasNext())
				sb.append(',').append(' ');
		}

		return sb.toString();
	}

	@Override
	public String getMostCertainRepresentant() {
		return representant;
	}

	@Override
	public Map<String, Double> getValueDistribution() {
		return valueDistribution;
	}

	public Set<String> getLabelSet() {
		return valueDistribution.keySet();
	}
}
