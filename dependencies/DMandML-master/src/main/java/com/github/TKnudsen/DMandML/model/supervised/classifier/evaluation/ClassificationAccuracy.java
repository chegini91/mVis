package com.github.TKnudsen.DMandML.model.supervised.classifier.evaluation;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.DMandML.model.supervised.classifier.IClassifier;

/**
 * <p>
 * Title: ClassificationAccuracy
 * </p>
 * 
 * <p>
 * Description: Accuracy measure for a series of test objects. Accuracy is
 * defined by the number of objects predicted correctly divided by the number of
 * all tested objects.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2017 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.03
 */
public class ClassificationAccuracy implements IClassifierEvaluation<NumericalFeatureVector> {

	@Override
	public double getQuality(IClassifier<NumericalFeatureVector> model, List<NumericalFeatureVector> testData,
			String targetVariable) {
		double count = 0;
		double correct = 0;

		List<String> test = model.test(testData);
		if (test == null || test.size() == 0)
			return Double.NaN;

		if (test.size() != testData.size())
			throw new IllegalArgumentException("input size != output size");

		for (int i = 0; i < testData.size(); i++) {
			if (testData.get(i) != null && testData.get(i).getAttribute(targetVariable) != null) {
				String label = testData.get(i).getAttribute(targetVariable).toString();
				if (label.equals(test.get(i)))
					correct++;
				count++;
			}
		}

		return correct / count;
	}

	@Override
	public String getName() {
		return "accuracy";
	}

	@Override
	public String getDescription() {
		return "returns the ratio of correctly predicted instances";
	}

}
