package com.github.TKnudsen.DMandML.model.semiSupervised.activeLearning.queryByCommittee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.github.TKnudsen.ComplexDataObject.data.entry.EntryWithComparableKey;
import com.github.TKnudsen.ComplexDataObject.data.interfaces.IFeatureVectorObject;
import com.github.TKnudsen.ComplexDataObject.data.ranking.Ranking;
import com.github.TKnudsen.DMandML.data.classification.IClassificationResult;

/**
 * <p>
 * Title: VoteComparisonQueryByCommittee
 * </p>
 * 
 * <p>
 * Description: queries controversial instances/regions in the input space.
 * Compares the label distributions of every candidate for a given set of
 * models. The winning candidate poses those label distributions where the
 * committee disagrees most.
 * 
 * Measure: Vote Comparison. Ratio of different Votes.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.06
 */
public class VoteComparisonQueryByCommittee<FV extends IFeatureVectorObject<?, ?>>
		extends AbstractQueryByCommitteeActiveLearning<FV> {

	protected VoteComparisonQueryByCommittee() {
	}

	public VoteComparisonQueryByCommittee(
			List<Function<List<? extends FV>, IClassificationResult<FV>>> classificationApplicationFunctions) {
		super(classificationApplicationFunctions);
	}

	@Override
	public String getComparisonMethod() {
		return "Measures the ratio of disagreeing suggestions for labels of different models (votes).";
	}

	@Override
	protected void calculateRanking() {

		ranking = new Ranking<>();
		queryApplicabilities = new HashMap<>();
		remainingUncertainty = 0.0;

		List<Function<List<? extends FV>, IClassificationResult<FV>>> classificationApplicationFunctions = getClassificationApplicationFunctions();

		List<IClassificationResult<FV>> results = new ArrayList<>();
		for (Function<List<? extends FV>, IClassificationResult<FV>> result : classificationApplicationFunctions)
			results.add(result.apply(candidates));

		// calculate overall score
		for (FV fv : candidates) {
			List<Map<String, Double>> labelDistributions = new ArrayList<>();

			for (IClassificationResult<FV> result : results)
				labelDistributions.add(result.getLabelDistribution(fv).getValueDistribution());

			// create unified distribution arrays
			Set<String> labelSet = new HashSet<>();
			for (Map<String, Double> map : labelDistributions)
				if (map != null)
					for (String s : map.keySet())
						labelSet.add(s);

			List<List<Double>> distributions = new ArrayList<>();
			for (Map<String, Double> map : labelDistributions) {
				if (map == null)
					continue;
				List<Double> values = new ArrayList<>();
				for (String s : labelSet)
					if (map.get(s) != null)
						values.add(map.get(s));
					else
						values.add(0.0);
				distributions.add(values);
			}

			double dist = 0;

			if (distributions != null && distributions.size() > 0) {
				Set<String> winningLabels = new HashSet<>();
				for (IClassificationResult<FV> result : results)
					winningLabels.add(result.getClass(fv));

				dist = (winningLabels.size() - 1) / (double) distributions.size();
			} else
				dist = 1;

			// update ranking

			ranking.add(new EntryWithComparableKey<Double, FV>(1 - dist, fv));

			queryApplicabilities.put(fv, dist);
			remainingUncertainty += dist;
		}

		remainingUncertainty /= (double) candidates.size();
		System.out.println("VoteComparisonQueryByCommittee: remaining uncertainty = " + remainingUncertainty);

	}

	@Override
	public String getName() {
		return "Vote Comparison QBC";
	}

	@Override
	public String getDescription() {
		return getName();
	}
}
