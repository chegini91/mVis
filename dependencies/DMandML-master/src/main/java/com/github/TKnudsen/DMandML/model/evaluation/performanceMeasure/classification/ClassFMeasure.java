package com.github.TKnudsen.DMandML.model.evaluation.performanceMeasure.classification;

import java.util.List;

/**
 * <p>
 * Title: ClassFMeasure
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2017 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Christian Ritter
 * @version 1.01
 */
public class ClassFMeasure implements IClassificationPerformanceMeasure {

	private double beta;
	private String className;

	public ClassFMeasure(String className) {
		this(className, 1.0);
	}

	public ClassFMeasure(String className, double beta) {
		this.className = className;
		this.beta = beta;
	}

	@Override
	public Double calcPerformance(List<String> values, List<String> groundTruth) {
		ClassRecall cr = new ClassRecall(className);
		ClassPrecision cp = new ClassPrecision(className);

		double r = cr.calcPerformance(values, groundTruth);
		double p = cp.calcPerformance(values, groundTruth);

		// if recall and precision is 0 f-measure is also 0
		if (p + r == 0)
			return 0.0;
		double beta2 = beta * beta;
		return (1 + beta2) * p * r / (beta2 * p + r);
	}

	@Override
	public String getDescription() {
		return "Calculates the F-measure of a given class of a classification result";
	}

	@Override
	public String getName() {
		return className + " F" + String.valueOf(beta) + "-Measure";
	}

}
