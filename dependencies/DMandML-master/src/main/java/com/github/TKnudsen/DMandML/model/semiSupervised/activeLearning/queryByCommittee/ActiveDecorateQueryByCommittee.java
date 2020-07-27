package com.github.TKnudsen.DMandML.model.semiSupervised.activeLearning.queryByCommittee;

import java.util.List;
import java.util.function.Function;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.IFeatureVectorObject;
import com.github.TKnudsen.DMandML.data.classification.IClassificationResult;
import com.github.TKnudsen.DMandML.model.semiSupervised.activeLearning.uncertaintySampling.SmallestMarginActiveLearning;

/**
 * <p>
 * Title: ActiveDecorateQueryByCommittee
 * </p>
 * 
 * <p>
 * Description: constructs diverse committees using artificial training data.
 * Builds upon committees built with the Decorates classifiers.
 * 
 * Measure: Kullback-Leibler Divergence. Divergence between models' label
 * probability distribution and consensus distribution.
 * 
 * Citation: Diverse Ensembles for Active Learning. Prem Melville and Raymond J.
 * Mooney. Machine Learning, 2004.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */

public class ActiveDecorateQueryByCommittee<FV extends IFeatureVectorObject<?, ?>>
		extends AbstractQueryByCommitteeActiveLearning<FV> {

	private SmallestMarginActiveLearning<FV> smallestMargin;

	protected ActiveDecorateQueryByCommittee() {
	}

	public ActiveDecorateQueryByCommittee(
			List<Function<List<? extends FV>, IClassificationResult<FV>>> decorateClassifierCommitte) {
		super(decorateClassifierCommitte);

		smallestMargin = new SmallestMarginActiveLearning<>(decorateClassifierCommitte.get(0));
	}

	@Override
	public String getComparisonMethod() {
		return "Detects disagreement among an ensemble of hypotheses (classifiers) build with the Decorate classfier.";
	}

	@Override
	protected void calculateRanking() {
		if (smallestMargin != null) {
			queryApplicabilities = smallestMargin.getCandidateScores();

			ranking = smallestMargin.getRanking();

			remainingUncertainty = smallestMargin.getRemainingUncertainty();
		}

		else
			throw new NullPointerException(getName() + ": smallest margin not initialized yet.");
	}

	@Override
	public String getName() {
		return "Active-Decorate QBC";
	}

	@Override
	public String getDescription() {
		return getName();
	}
}
