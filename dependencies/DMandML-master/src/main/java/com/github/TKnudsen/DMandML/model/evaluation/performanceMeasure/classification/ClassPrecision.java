package com.github.TKnudsen.DMandML.model.evaluation.performanceMeasure.classification;

import java.util.List;

/**
 * @author Christian Ritter
 *
 */
public class ClassPrecision implements IClassificationPerformanceMeasure {

	private String className;

	public ClassPrecision(String className) {
		this.className = className;
	}

	@Override
	public Double calcPerformance(List<String> values, List<String> groundTruth) {
		if (values == null || groundTruth == null || values.size() != groundTruth.size())
			throw new IllegalArgumentException("Lists are null or of unequal size!");

		int tp = 0;
		int fp = 0;
		for (int i = 0; i < values.size(); i++) {
			String l = values.get(i);
			if (l.equals(className)) {
				if (groundTruth.get(i).equals(l))
					tp++;
				else
					fp++;
			}
		}
		if (tp == 0)
			return 0.0;
		else
			return 1.0 * tp / (tp + fp);
	}
	
	@Override
	public String getName() {
		return className + " Precision";
	}

	@Override
	public String getDescription() {
		return "Calculates the precision of a given class of a classification result";
	}

}
