package com.github.TKnudsen.DMandML.model.semiSupervised.activeLearning.uncertaintySampling;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.github.TKnudsen.ComplexDataObject.data.entry.EntryWithComparableKey;
import com.github.TKnudsen.ComplexDataObject.data.interfaces.IFeatureVectorObject;
import com.github.TKnudsen.ComplexDataObject.data.ranking.Ranking;
import com.github.TKnudsen.ComplexDataObject.model.statistics.Entropy;
import com.github.TKnudsen.DMandML.data.classification.IClassificationResult;
import com.github.TKnudsen.DMandML.model.semiSupervised.activeLearning.AbstractActiveLearningModel;
import com.github.TKnudsen.DMandML.model.supervised.classifier.use.IClassificationApplicationFunction;

/**
 * <p>
 * Title: EntropyBasedActiveLearning
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
 * @version 1.06
 */
public class EntropyBasedActiveLearning<FV extends IFeatureVectorObject<?, ?>> extends AbstractActiveLearningModel<FV> {

	protected EntropyBasedActiveLearning() {
	}

	public EntropyBasedActiveLearning(
			Function<List<? extends FV>, IClassificationResult<FV>> classificationApplyFunction) {
		super(classificationApplyFunction);
	}

	public EntropyBasedActiveLearning(IClassificationApplicationFunction<FV> cassificationApplicationFunction) {
		super(cassificationApplicationFunction);
	}

	@Override
	protected void calculateRanking() {

		ranking = new Ranking<>();
		queryApplicabilities = new HashMap<>();
		remainingUncertainty = 0.0;

		IClassificationResult<FV> classification = getClassificationApplicationFunction().apply(candidates);

		// calculate ranking based on entropy
		for (FV fv : candidates) {
			Map<String, Double> labelDistribution = null;
			labelDistribution = classification.getLabelDistribution(fv).getValueDistribution();

			double entropy = Entropy.calculateEntropy(labelDistribution);

			ranking.add(new EntryWithComparableKey<Double, FV>(1 - entropy, fv));

			queryApplicabilities.put(fv, entropy);
			remainingUncertainty += (entropy);
		}

		remainingUncertainty /= (double) candidates.size();
		System.out.println("EntropyBasedActiveLearning: remaining uncertainty = " + remainingUncertainty);
	}

	@Override
	public String getName() {
		return "Entropy-Based Sampling";
	}

	@Override
	public String getDescription() {
		return getName();
	}

}
