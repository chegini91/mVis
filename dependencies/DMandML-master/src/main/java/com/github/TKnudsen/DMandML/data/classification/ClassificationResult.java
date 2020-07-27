package com.github.TKnudsen.DMandML.data.classification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Title: ClassificationResult
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.05
 */
public class ClassificationResult<X> implements IClassificationResult<X> {

	private String name;

	private Set<String> labelAlphabet;

	private final Map<X, String> labels;

	Map<String, List<X>> classDistributions;

	private final Map<X, LabelDistribution> labelDistributionMap;

	/**
	 * constructor stores a reference on the object.
	 * 
	 * @param labelDistributionMap
	 */
	public ClassificationResult(Map<X, LabelDistribution> labelDistributionMap, Set<String> labelAlphabet) {
		this(labelDistributionMap, null, labelAlphabet);
	}

	/**
	 * constructor stores a reference on the object.
	 * 
	 * @param labelDistributionMap
	 */
	public ClassificationResult(Map<X, LabelDistribution> labelDistributionMap, String name,
			Set<String> labelAlphabet) {
		this.labelDistributionMap = Collections.unmodifiableMap(labelDistributionMap);

		this.labels = ClassificationResults.createwinningLabelsMap(labelDistributionMap);

		this.name = name;

		this.labelAlphabet = Collections.unmodifiableSet(labelAlphabet);
	}

	@Override
	public Collection<X> getFeatureVectors() {
		return labels.keySet();
	}

	@Override
	public String getClass(X featureVector) {
		return labels.get(featureVector);
	}

	protected static <X, Y> Map<X, Y> zip(List<X> xs, List<Y> ys) {
		Map<X, Y> map = new LinkedHashMap<X, Y>();

		for (int i = 0; i < xs.size(); i++) {
			map.put(xs.get(i), ys.get(i));
		}

		return map;
	}

	@Override
	public Map<String, List<X>> getClassDistributions() {
		if (classDistributions == null)
			calculateClassDistributions();

		return classDistributions;
	}

	@Override
	public LabelDistribution getLabelDistribution(X x) {
		return labelDistributionMap.get(x);
	}

	private void calculateClassDistributions() {
		classDistributions = new LinkedHashMap<>();

		for (X x : labels.keySet()) {
			if (classDistributions.get(labels.get(x)) == null)
				classDistributions.put(labels.get(x), new ArrayList<>());

			classDistributions.get(labels.get(x)).add(x);
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return getName();
	}

	@Override
	public Set<String> getLabelAlphabet() {
		return Collections.unmodifiableSet(labelAlphabet);
	}

}
