package com.github.TKnudsen.DMandML.model.supervised.classifier.evaluation;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.DMandML.data.classification.IClassificationResult;
import com.github.TKnudsen.DMandML.data.classification.LabelDistribution;
import com.github.TKnudsen.DMandML.model.supervised.classifier.IClassifier;

/**
 * <p>
 * Title: ClassificationAccuracy
 * </p>
 * 
 * <p>
 * Description: Uses the probabilities of the classifier for the correct labels
 * as a means to assess the classification score.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2017-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 */
public class ClassPredictionProbabilityScore implements IClassifierEvaluation<NumericalFeatureVector> {

	@Override
	public double getQuality(IClassifier<NumericalFeatureVector> model, List<NumericalFeatureVector> testData,
			String targetVariable) {
		double count = 0;
		double predictionSum = 0;

		IClassificationResult<NumericalFeatureVector> classificationResult = model.createClassificationResult(testData);

		if (classificationResult == null || classificationResult.getFeatureVectors().size() == 0)
			return Double.NaN;

		if (classificationResult.getFeatureVectors().size() != testData.size())
			throw new IllegalArgumentException("input size != output size");

		if (classificationResult instanceof IClassificationResult<?>) {
			IClassificationResult<NumericalFeatureVector> probablisticClassificationResult = (IClassificationResult<NumericalFeatureVector>) classificationResult;

			for (int i = 0; i < testData.size(); i++) {
				if (testData.get(i) != null && testData.get(i).getAttribute(targetVariable) != null) {

					LabelDistribution labelDistribution = probablisticClassificationResult
							.getLabelDistribution(testData.get(i));

					String label = testData.get(i).getAttribute(targetVariable).toString();

					double probability = labelDistribution.getValueDistribution().get(label);
					predictionSum += probability;
					count++;
				}
			}

		} else
			throw new IllegalArgumentException(
					"ClassPredictionProbabilityScore: Classifier did not produce a probablistic classification result");

		return predictionSum / count;
	}

	@Override
	public String getName() {
		return "class prediction average score";
	}

	@Override
	public String getDescription() {
		return getName();
	}
}
