package com.github.TKnudsen.DMandML.model.evaluation.performanceMeasure.classification;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Christian Ritter
 *
 */
public class AveragePrecision implements IClassificationPerformanceMeasure {

	@Override
	public Double calcPerformance(List<String> values, List<String> groundTruth) {
		if (values == null || groundTruth == null || values.size() != groundTruth.size())
			throw new IllegalArgumentException("Lists are null or of unequal size!");

		Set<String> labels = new HashSet<>(groundTruth);
		double perf = 0.0;
		int c = 0;
		for (String s : labels) {
			ClassPrecision p = new ClassPrecision(s);
			perf += p.calcPerformance(values, groundTruth);
			c++;
		}
		if (c == 0)
			return 0.0;
		else
			return perf / c;
	}

	@Override
	public String getName() {
		return "Average Precision";
	}

	@Override
	public String getDescription() {
		return "Calculates the average precision over all classes of a classification result";
	}

}
