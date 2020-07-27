package com.github.TKnudsen.DMandML.model.evaluation.performanceMeasure.classification;

import java.util.List;

/**
 * @author Christian Ritter
 *
 */
public class ClassRecall implements IClassificationPerformanceMeasure {

	private String className;

	public ClassRecall(String className) {
		this.className = className;
	}

	@Override
	public Double calcPerformance(List<String> values, List<String> groundTruth) {
		if (values == null || groundTruth == null || values.size() != groundTruth.size())
			throw new IllegalArgumentException("Lists are null or of unequal size!");

		int tp = 0;
		int fn = 0;
		for (int i = 0; i < values.size(); i++) {
			String l = groundTruth.get(i);
			if (l.equals(className)) {
				if (values.get(i).equals(l))
					tp++;
				else
					fn++;
			}
		}
		if (tp == 0)
			return 0.0;
		else
			return 1.0 * tp / (tp + fn);
	}

	@Override
	public String getName() {
		return className + " Recall";
	}

	@Override
	public String getDescription() {
		return "Calculates the recall of a given class of a classification result";
	}

}
