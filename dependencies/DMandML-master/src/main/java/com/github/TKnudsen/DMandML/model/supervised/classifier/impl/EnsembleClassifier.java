package com.github.TKnudsen.DMandML.model.supervised.classifier.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.github.TKnudsen.ComplexDataObject.data.probability.ProbabilityDistribution;
import com.github.TKnudsen.ComplexDataObject.model.tools.MathFunctions;
import com.github.TKnudsen.DMandML.data.classification.ClassificationResult;
import com.github.TKnudsen.DMandML.data.classification.IClassificationResult;
import com.github.TKnudsen.DMandML.data.classification.LabelDistribution;
import com.github.TKnudsen.DMandML.data.classification.LabelDistributionTools;
import com.github.TKnudsen.DMandML.model.supervised.classifier.IClassifier;

/**
 * <p>
 * Title: EnsembleClassifier
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: (c) 2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * 
 * @version 1.03
 * 
 */
public class EnsembleClassifier<FV> implements IClassifier<FV> {

	private final List<IClassifier<FV>> classifiers;
	private final String classAttribute;

	public EnsembleClassifier(Collection<? extends IClassifier<FV>> classifiers) {
		this.classAttribute = extractClassAttribute(classifiers);
		this.classifiers = new ArrayList<IClassifier<FV>>(classifiers);
	}

	private static String extractClassAttribute(Iterable<? extends IClassifier<?>> classifiers) {
		Set<String> classAttributes = new HashSet<String>();
		for (IClassifier<?> classifier : classifiers) {
			classAttributes.add(classifier.getClassAttribute());
		}
		if (classAttributes.size() != 1) {
			throw new IllegalArgumentException("The classifiers have different class attributes: " + classAttributes);
		}
		return classAttributes.iterator().next();
	}

	@Override
	public String getClassAttribute() {
		return classAttribute;
	}

	@Override
	public void train(List<FV> featureVectors) {

		// TODO Implementations do this. Consider it as part of the contract.
		if (featureVectors.size() < 2) {
			throw new IllegalArgumentException("at least two training instances required");
		}

		long totalBeforeNs = System.nanoTime();

		for (IClassifier<FV> classifier : classifiers) {
			try {
				long beforeNs = System.nanoTime();
				classifier.train(featureVectors);
				long afterNs = System.nanoTime();
				System.out.println(
						"EnsembleClassifier training took " + ((afterNs - beforeNs) / 1e6) + " ms for " + classifier);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		long totalAfterNs = System.nanoTime();
		System.out.println("EnsembleClassifier training took " + ((totalAfterNs - totalBeforeNs) / 1e6) + " ms");
	}

	@Override
	public List<String> test(List<FV> featureVectors) {
		List<String> labels = new ArrayList<String>();
		for (FV fv : featureVectors) {
			Map<String, Double> labelDistribution = getLabelDistribution(fv);
			if (labelDistribution != null && labelDistribution.size() == 0)
				labels.add(null);
			else {
				Entry<String, Double> entryWithHighestProbability = Collections.max(labelDistribution.entrySet(),
						Map.Entry.comparingByValue());
				String label = entryWithHighestProbability.getKey();
				labels.add(label);
			}
		}
		return labels;
	}

	@Override
	public Map<String, Double> getLabelDistribution(FV featureVector) {

		Map<String, List<Double>> labelDistributions = new HashMap<>();

		for (IClassifier<FV> classifier : classifiers) {
			Map<String, Double> labelDistribution = classifier.getLabelDistribution(featureVector);

			if (labelDistribution == null) {
				System.err.println(
						getName() + ": label distribution was null. Ignoring information for the ensemble result...");
				continue;
			}

			if (!ProbabilityDistribution.checkProbabilitySumMatchesHundredPercent(labelDistribution.values(),
					ProbabilityDistribution.EPSILON, true)) {
				System.err.println(getName() + ": sum of given label probabilites (" + classifier.getName()
						+ " classifier) was != 100% (" + MathFunctions.getSum(labelDistribution.values(), true) + ")");
			}

			for (String label : labelDistribution.keySet()) {
				List<Double> values = labelDistributions.computeIfAbsent(label, l -> new ArrayList<>());
				values.add(labelDistribution.get(label));
			}
		}

		Map<String, Double> labelDistribution = new LinkedHashMap<>();

		List<String> labelAlphabet = getLabelAlphabet();
		for (String label : labelAlphabet) {
			List<Double> values = labelDistributions.get(label);
			double probability = MathFunctions.getMean(values);
			labelDistribution.put(label, probability);
		}

		return labelDistribution;
	}

	@Override
	public IClassificationResult<FV> createClassificationResult(List<? extends FV> featureVectors) {

		Map<FV, Collection<LabelDistribution>> labelDistributions = new LinkedHashMap<>();

		for (IClassifier<FV> classifier : classifiers) {
			IClassificationResult<FV> classificationResult = classifier.createClassificationResult(featureVectors);

			if (classificationResult != null)
				for (FV fv : featureVectors) {
					Collection<LabelDistribution> collection = labelDistributions.computeIfAbsent(fv,
							v -> new ArrayList<>());
					collection.add(classificationResult.getLabelDistribution(fv));
				}
			else
				System.err.println(getName() + ": one of the classifier results was null and will be ignored.");
		}

		Map<FV, LabelDistribution> labelDistributionMap = new LinkedHashMap<>();
		for (Entry<FV, Collection<LabelDistribution>> entry : labelDistributions.entrySet()) {
			FV fv = entry.getKey();
			Collection<LabelDistribution> labelDistribution = entry.getValue();
			LabelDistribution mergedLabelDistribution = LabelDistributionTools
					.mergeLabelDistributions(labelDistribution);
			labelDistributionMap.put(fv, mergedLabelDistribution);
		}
		return new ClassificationResult<>(labelDistributionMap, getName(), new HashSet<>(getLabelAlphabet()));
	}

	@Override
	public List<String> getLabelAlphabet() {
		Set<String> labelAlphabet = new LinkedHashSet<String>();
		for (IClassifier<FV> classifier : classifiers) {
			labelAlphabet.addAll(classifier.getLabelAlphabet());
		}
		return new ArrayList<String>(labelAlphabet);
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public String getDescription() {
		return this.getClass().getCanonicalName();
	}

	@Override
	public String toString() {
		return getName();
	}

	/**
	 * Note: this is highly critical as these classifiers may run out of sync when
	 * trained from outside. However, it brings advantage in the (visual) analysis
	 * of individual classifier models / results.
	 * 
	 * @return
	 */
	public List<IClassifier<FV>> getClassifiers() {
		return classifiers;
	}

}
