package com.github.TKnudsen.DMandML.model.semiSupervised.activeLearning.uncertaintySampling;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import com.github.TKnudsen.ComplexDataObject.data.entry.EntryWithComparableKey;
import com.github.TKnudsen.ComplexDataObject.data.interfaces.IFeatureVectorObject;
import com.github.TKnudsen.ComplexDataObject.data.ranking.Ranking;
import com.github.TKnudsen.DMandML.data.classification.IClassificationResult;
import com.github.TKnudsen.DMandML.data.classification.LabelDistribution;
import com.github.TKnudsen.DMandML.model.semiSupervised.activeLearning.AbstractActiveLearningModel;
import com.github.TKnudsen.DMandML.model.supervised.classifier.use.IClassificationApplicationFunction;

/**
 * <p>
 * Title: LeastSignificantConfidence
 * </p>
 * 
 * <p>
 * Description: a baseline active learning model seeking the lowest maximum
 * likelihood among all instances.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.06
 */
public class LeastSignificantConfidence<FV extends IFeatureVectorObject<?, ?>> extends AbstractActiveLearningModel<FV> {
	protected LeastSignificantConfidence() {
	}

	public LeastSignificantConfidence(
			Function<List<? extends FV>, IClassificationResult<FV>> classificationApplyFunction) {
		super(classificationApplyFunction);
	}

	public LeastSignificantConfidence(IClassificationApplicationFunction<FV> cassificationApplicationFunction) {
		super(cassificationApplicationFunction);
	}

	@Override
	protected void calculateRanking() {

		ranking = new Ranking<>();
		queryApplicabilities = new HashMap<>();
		remainingUncertainty = 0.0;

		IClassificationResult<FV> classification = getClassificationApplicationFunction().apply(candidates);

		// calculate overall score
		for (FV fv : candidates) {
			double significance = 0.0;

			LabelDistribution labelDistribution = classification.getLabelDistribution(fv);
			if (labelDistribution != null)
				significance = labelDistribution.getProbability(labelDistribution.getMostLikelyItem());

			ranking.add(new EntryWithComparableKey<Double, FV>(significance, fv));
			queryApplicabilities.put(fv, 1 - significance);
			remainingUncertainty += (1 - significance);
		}

		remainingUncertainty /= (double) candidates.size();
		System.out.println("LastSignificantConfidence: remaining uncertainty = " + remainingUncertainty);
	}

	@Override
	public String getName() {
		return "Least Significant Confidence";
	}

	@Override
	public String getDescription() {
		return "Focus on instances with weakest winning labels.";
	}

}