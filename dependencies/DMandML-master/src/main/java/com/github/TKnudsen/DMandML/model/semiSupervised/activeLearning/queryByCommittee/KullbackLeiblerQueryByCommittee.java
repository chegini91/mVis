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
 * Title: KullbackLeiblerQueryByCommittee
 * </p>
 * 
 * <p>
 * Description: queries controversial instances/regions in the input space.
 * Compares the label distributions of every candidate for a given set of
 * models. The winning candidate poses those label distributions where the
 * committee disagrees most.
 * 
 * Measure: Kullback-Leibler Divergence. Divergence between models' label
 * probability distribution and consensus distribution.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.04
 */

public class KullbackLeiblerQueryByCommittee<FV extends IFeatureVectorObject<?, ?>>
		extends AbstractQueryByCommitteeActiveLearning<FV> {

	private boolean positiveDivergences = true;

	private boolean normalizeAlphabetLength = true;

	protected KullbackLeiblerQueryByCommittee() {
	}

	public KullbackLeiblerQueryByCommittee(
			List<Function<List<? extends FV>, IClassificationResult<FV>>> classificationApplicationFunctions) {
		super(classificationApplicationFunctions);
	}

	@Override
	public String getComparisonMethod() {
		return "Uses entropy to identify instances where models disagree";
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
				dist = calculateKLDivergence(distributions);
			} else
				dist = 1;

			// update ranking

			ranking.add(new EntryWithComparableKey<Double, FV>(1 - dist, fv));

			queryApplicabilities.put(fv, dist);
			remainingUncertainty += dist;
		}

		remainingUncertainty /= (double) candidates.size();
		System.out.println("KullbackLeiblerQueryByCommittee: remaining uncertainty = " + remainingUncertainty);
	}

	private List<Double> calculateConsensusDistribution(List<List<Double>> distributions) {
		// not weighted votes
		int distributionSize = distributions.get(0).size();
		List<Double> consensusDistribution = new ArrayList<>();
		for (int i = 0; i < distributionSize; i++)
			consensusDistribution.add(0d);

		for (List<Double> learnerDistribution : distributions) {
			for (int i = 0; i < distributionSize; i++) {
				double d = consensusDistribution.get(i) + learnerDistribution.get(i);
				consensusDistribution.set(i, d);
			}
		}
		// normalize
		for (int i = 0; i < distributionSize; i++) {
			// double d = consensusDistribution.get(i) / distributionSize;
			double d = consensusDistribution.get(i) / distributions.size();
			consensusDistribution.set(i, d);
		}

		return consensusDistribution;
	}

	private double calculateKLDivergence(List<List<Double>> distributions) {
		List<Double> consensusDistribution = calculateConsensusDistribution(distributions);
		int learnerCounts = distributions.size();
		double result = 0;
		for (int i = 0; i < learnerCounts; i++) {
			// double consensus = consensusDistribution.get(i);
			// for (Double d : distributions.get(i)) {
			// result += d * Math.log(d / consensus);
			// }

			// made some changes here
			for (int j = 0; j < consensusDistribution.size(); j++) {
				double consensus = consensusDistribution.get(j);
				Double d = distributions.get(i).get(j);
				if (d <= 0)
					result += 0;
				else if (consensus == 0)
					result += 1;
				else if (positiveDivergences)
					result += Math.abs(d * Math.log(d / consensus));
				else
					result += d * Math.log(d / consensus);
			}
		}
		// normalize?
		if (normalizeAlphabetLength)
			result = result / learnerCounts;

		return result;
	}

	public boolean isPositiveDivergences() {
		return positiveDivergences;
	}

	public void setPositiveDivergences(boolean positiveDivergences) {
		this.positiveDivergences = positiveDivergences;
	}

	public boolean isNormalizeAlphabetLength() {
		return normalizeAlphabetLength;
	}

	public void setNormalizeAlphabetLength(boolean normalizeAlphabetLength) {
		this.normalizeAlphabetLength = normalizeAlphabetLength;
	}

	@Override
	public String getName() {
		return "Kullback Leibler QBC";
	}

	@Override
	public String getDescription() {
		return "Active Learning Model using the Kullback Leibler Divergence in combination with a Query by Committee approach";
	}
}
