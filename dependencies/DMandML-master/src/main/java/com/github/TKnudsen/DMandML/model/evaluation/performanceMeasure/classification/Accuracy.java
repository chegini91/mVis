package com.github.TKnudsen.DMandML.model.evaluation.performanceMeasure.classification;

import java.util.List;

/**
 * @author Christian Ritter
 *
 */
public class Accuracy implements IClassificationPerformanceMeasure {

	@Override
	public Double calcPerformance(List<String> values, List<String> groundTruth) {
		if (values == null || groundTruth == null || values.size() != groundTruth.size())
			throw new IllegalArgumentException("Lists are null or of unequal size!");
		int correct = 0;
		for (int i = 0; i < values.size(); i++) {
			if (values.get(i).equals(groundTruth.get(i)))
				correct++;
		}
		return 1.0 * correct / values.size();
	}

	@Override
	public String getName() {
		return "Accuracy";
	}

	@Override
	public String getDescription() {
		return "Calculates the accuracy of a classification result";
	}

}
