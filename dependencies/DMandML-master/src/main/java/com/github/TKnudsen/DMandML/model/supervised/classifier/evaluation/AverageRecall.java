package com.github.TKnudsen.DMandML.model.supervised.classifier.evaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.DMandML.model.supervised.classifier.IClassifier;

/**
 * <p>
 * Title: AverageRecall
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * @author Christian Ritter, Juergen Bernard
 * @version 1.03
 */
public class AverageRecall implements IClassifierEvaluation<NumericalFeatureVector> {

	@Override
	public double getQuality(IClassifier<NumericalFeatureVector> model, List<NumericalFeatureVector> testData,
			String targetVariable) {
		Set<String> labels = testData.stream().map(x -> x.getAttribute(targetVariable).toString())
				.collect(Collectors.toSet());
		Map<String, Double> truepositives = new HashMap<>();
		Map<String, Double> trues = new HashMap<>();
		for (String l : labels) {
			truepositives.put(l, 0.0);
			trues.put(l, 0.0);
		}

		List<String> test = model.test(testData);
		if (test == null || test.size() == 0)
			return Double.NaN;

		if (test.size() != testData.size())
			throw new IllegalArgumentException("input size != output size");

		for (int i = 0; i < testData.size(); i++) {
			if (testData.get(i) != null && testData.get(i).getAttribute(targetVariable) != null) {
				String trueLabel = testData.get(i).getAttribute(targetVariable).toString();
				String assignedLabel = test.get(i);

				if (assignedLabel == null) {
					System.err.println(getName() + ": classifier returned null label when testing");
					return Double.NaN;
				}

				if (trueLabel.equals(assignedLabel))
					truepositives.put(assignedLabel, truepositives.get(assignedLabel) + 1);
				trues.put(trueLabel, trues.get(trueLabel) + 1);
			}
		}

		List<Double> recallValues = new ArrayList<>();
		for (String s : truepositives.keySet()) {
			if (trues.get(s) != 0.0)
				recallValues.add(truepositives.get(s) / trues.get(s));
		}

		if (recallValues.size() < 1)
			return 0.0;
		else
			return recallValues.stream().reduce(0.0, (x, y) -> x + y).doubleValue() / recallValues.size();
	}

	@Override
	public String getName() {
		return "average recall";
	}

	@Override
	public String getDescription() {
		return getName();
	}
}
