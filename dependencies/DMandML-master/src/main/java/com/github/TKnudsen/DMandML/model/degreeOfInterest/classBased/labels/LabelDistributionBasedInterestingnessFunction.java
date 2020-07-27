package com.github.TKnudsen.DMandML.model.degreeOfInterest.classBased.labels;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.IFeatureVectorObject;
import com.github.TKnudsen.ComplexDataObject.model.tools.MathFunctions;
import com.github.TKnudsen.ComplexDataObject.model.tools.StatisticsSupport;
import com.github.TKnudsen.DMandML.data.classification.IClassificationResult;
import com.github.TKnudsen.DMandML.data.classification.IClassificationResultSupplier;
import com.github.TKnudsen.DMandML.data.classification.LabelDistribution;
import com.github.TKnudsen.DMandML.data.classification.LabelDistributionTools;
import com.github.TKnudsen.DMandML.model.degreeOfInterest.classBased.ClassificationBasedInterestingnessFunction;
import com.google.common.collect.ImmutableMap;

/**
 * <p>
 * Title: LabelBasedInterestingnessFunction
 * </p>
 * 
 * <p>
 * Description: Uses the labels of a classification approach to assess the
 * interestingness of the FVs included in the data supplier.
 * 
 * Calculations are based on a given label distribution versus an observed
 * distribution. Inheriting classes need to formalize how label distributions
 * are to be observed.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 */
public abstract class LabelDistributionBasedInterestingnessFunction<FV extends IFeatureVectorObject<?, ?>>
		extends ClassificationBasedInterestingnessFunction<FV> {

	private Map<String, Double> targetLabelDistribution;

	public LabelDistributionBasedInterestingnessFunction(Supplier<List<FV>> featureVectorSupplier,
			IClassificationResultSupplier<FV> classificationResultSupplier,
			Map<String, Double> targetLabelDistribution) {
		super(featureVectorSupplier, classificationResultSupplier);

		this.targetLabelDistribution = LabelDistributionTools.normalizeLabelDistribution(targetLabelDistribution);
	}

	@Override
	/**
	 * calculate the difference between targeted and observed label distribution.
	 * then reccomend instances that would balance the deviance.
	 */
	public void run() {
		IClassificationResult<FV> classificationResult = getClassificationResultSupplier().get();

		Map<String, Double> targetLabelDistribution = getTargetLabelDistribution();

		// gather observed distribution
		Map<String, Double> observedLabelDistribution = calculateObservedLabelDistribution();
		observedLabelDistribution = LabelDistributionTools.normalizeLabelDistribution(observedLabelDistribution);

		Map<FV, Double> scores = new HashMap<>();

		// calculate a score for every FV depending on its winning class and the
		// respective need. Needed is when less is observed than required.
		List<FV> featureVectorList = getFeatureVectorSupplier().get();
		for (FV fv : featureVectorList) {
			LabelDistribution labelDistribution = classificationResult.getLabelDistribution(fv);
			if (labelDistribution == null)
				continue;

			String label = labelDistribution.getRepresentant();

			if (!targetLabelDistribution.containsKey(label))
				continue;

			double deltaProbability = targetLabelDistribution.get(label) - observedLabelDistribution.get(label);
			scores.put(fv, deltaProbability);
		}

		// postprocessing: normalize and fill interestingness map
		StatisticsSupport statistics = new StatisticsSupport(scores.values());
		interestingnessScores = new LinkedHashMap<>();
		for (FV fv : featureVectorList)
			interestingnessScores.put(fv,
					MathFunctions.linearScale(statistics.getMin(), statistics.getMax(), scores.get(fv)));
	}

	protected abstract Map<String, Double> calculateObservedLabelDistribution();

	public Map<String, Double> getTargetLabelDistribution() {
		return ImmutableMap.copyOf(targetLabelDistribution);
	}

	public void setTargetLabelDistribution(Map<String, Double> targetLabelDistribution) {
		this.targetLabelDistribution = targetLabelDistribution;

		resetInterestingnessScores();
	}
}
