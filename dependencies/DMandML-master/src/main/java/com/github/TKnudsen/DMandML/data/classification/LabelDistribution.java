package com.github.TKnudsen.DMandML.data.classification;

import java.util.Map;
import java.util.Set;

import com.github.TKnudsen.ComplexDataObject.data.probability.ProbabilityDistribution;

/**
 * <p>
 * Title: LabelDistribution
 * </p>
 * 
 * <p>
 * Description: data model for distributions of labels. Must add up to 100%
 * (probability distribution).
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2015-2018
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.03
 */
public class LabelDistribution extends ProbabilityDistribution<String> {

	/**
	 * the label representing the distribution does not necessarily need to be the
	 * most likely label.
	 */
	private String representant;

	/**
	 * constructor for reflection-based and jackson-based access.
	 */
	@SuppressWarnings("unused")
	private LabelDistribution() {
		this(null, null);
	}

	public LabelDistribution(Map<String, Double> valueDistribution) {
		this(valueDistribution, null);
	}

	public LabelDistribution(Map<String, Double> valueDistribution, String representant) {
		super(valueDistribution);

		if (representant == null)
			this.representant = getMostLikelyItem();
	}

	public String getRepresentant() {
		return representant;
	}

	public Map<String, Double> getValueDistribution() {
		return getProbabilityDistribution();
	}

	public Set<String> getLabelSet() {
		return keySet();
	}
}
