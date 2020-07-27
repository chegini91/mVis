package com.github.TKnudsen.DMandML.model.evaluation.performanceMeasure.classification;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Christian Ritter
 *
 */
public class AverageRecall implements IClassificationPerformanceMeasure {

	@Override
	public Double calcPerformance(List<String> values, List<String> groundTruth) {
		if (values == null || groundTruth == null || values.size() != groundTruth.size())
			throw new IllegalArgumentException("Lists are null or of unequal size!");

		Set<String> labels = new HashSet<>(groundTruth);
		double perf = 0.0;
		int c = 0;
		for (String s : labels) {
			ClassRecall r = new ClassRecall(s);
			perf += r.calcPerformance(values, groundTruth);
			c++;
		}
		if (c == 0)
			return 0.0;
		else
			return perf / c;
	}
	
	@Override
	public String getName() {
		return "Average Recall";
	}

	@Override
	public String getDescription() {
		return "Calculates the average recall over all classes of a classification result";
	}

}
