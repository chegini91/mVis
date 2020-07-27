package com.github.TKnudsen.DMandML.model.semiSupervised.activeLearning.expectedErrorReduction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.github.TKnudsen.ComplexDataObject.data.entry.EntryWithComparableKey;
import com.github.TKnudsen.ComplexDataObject.data.interfaces.IFeatureVectorObject;
import com.github.TKnudsen.ComplexDataObject.data.ranking.Ranking;
import com.github.TKnudsen.ComplexDataObject.model.statistics.Entropy;
import com.github.TKnudsen.DMandML.data.classification.IClassificationResult;
import com.github.TKnudsen.DMandML.data.classification.LabelDistribution;
import com.github.TKnudsen.DMandML.model.semiSupervised.activeLearning.AbstractActiveLearningModel;
import com.github.TKnudsen.DMandML.model.supervised.classifier.Classifier;
import com.github.TKnudsen.DMandML.model.supervised.classifier.Classifiers;
import com.github.TKnudsen.DMandML.model.supervised.classifier.WekaClassifierWrapper;
import com.github.TKnudsen.DMandML.model.supervised.classifier.use.IClassificationApplicationFunction;

/**
 * <p>
 * Title: ExpectedLogLossReduction
 * </p>
 * 
 * <p>
 * Description: Ranks potential learning candidates by estimating the expected
 * error reduction when labeling a candidate with its respective label
 * distribution. This is an implementation of the method proposed in Section 4.1
 * (Equation (4.2)) in "Active Learning", by Burr Settles (2012).
 * </p>
 * 
 * @author Christian Ritter, Juergen Bernard,
 *         https://github.com/TKnudsen/DMandML
 * @version 1.06
 */
public class ExpectedLogLossReduction<FV extends IFeatureVectorObject<?, ?>> extends AbstractActiveLearningModel<FV> {

	private Classifier<FV> parameterizedClassifier = null;

	private Supplier<List<FV>> trainingDataSupplier;

	/**
	 * This active learning algorithm requires an instance of the classifier used
	 * for training (either the original or a new instance with identical
	 * parameterization). If, and only if, this classifier is extending
	 * {@link WekaClassifierWrapper} it is not changed during active learning (it
	 * then uses a parameterized copy).
	 * 
	 * @param classificationApplyFunction
	 * @param parameterizedClassifier
	 * @param trainingDataSupplier
	 */
	public ExpectedLogLossReduction(Function<List<? extends FV>, IClassificationResult<FV>> classificationApplyFunction,
			Classifier<FV> parameterizedClassifier, Supplier<List<FV>> trainingDataSupplier) {
		super(classificationApplyFunction);

		this.parameterizedClassifier = parameterizedClassifier;
		this.trainingDataSupplier = trainingDataSupplier;
	}

	/**
	 * This active learning algorithm requires an instance of the classifier used
	 * for training (either the original or a new instance with identical
	 * parameterization). If, and only if, this classifier is extending
	 * {@link WekaClassifierWrapper} it is not changed during active learning (it
	 * then uses a parameterized copy).
	 * 
	 * @param classificationApplyFunction
	 * @param parameterizedClassifier
	 * @param trainingDataSupplier
	 */
	public ExpectedLogLossReduction(IClassificationApplicationFunction<FV> cassificationApplicationFunction,
			Classifier<FV> parameterizedClassifier, Supplier<List<FV>> trainingDataSupplier) {
		super(cassificationApplicationFunction);

		this.parameterizedClassifier = parameterizedClassifier;
		this.trainingDataSupplier = trainingDataSupplier;
	}

	@Override
	protected void calculateRanking() {
		ranking = new Ranking<>();
		remainingUncertainty = 0.0;

		if (candidates.size() < 1)
			return;

		int U = candidates.size();

		IClassificationResult<FV> classification = getClassificationApplicationFunction().apply(candidates);

		List<LabelDistribution> dists = new ArrayList<>();

		for (FV fv : candidates)
			dists.add(classification.getLabelDistribution(fv));

		Set<String> labels = new HashSet<>();
		for (LabelDistribution ld : dists) {
			if (ld == null)
				continue;
			labels.addAll(ld.getLabelSet());
		}

		boolean moreThanOneLabel = trainingDataSupplier.get().stream()
				.map(x -> x.getAttribute(parameterizedClassifier.getClassAttribute())).collect(Collectors.toSet())
				.size() > 1;

		for (int i = 0; i < U; i++) {
			FV fv = candidates.get(i);
			LabelDistribution dist = dists.get(i);

			double expectedError = 0.0;
			// only useful if more than one label is set
			if (moreThanOneLabel) {
				if (dist != null)
					for (String label : labels) {
						List<FV> newTrainingSet = new ArrayList<>();
						for (FV fv1 : trainingDataSupplier.get()) {
							newTrainingSet.add(fv1);
						}
						FV fv2 = (FV) fv.clone();
						fv2.add(parameterizedClassifier.getClassAttribute(), label);
						newTrainingSet.add(fv2);
						Classifier<FV> newClassifier = null;
						try {
							if (parameterizedClassifier instanceof WekaClassifierWrapper)
								newClassifier = Classifiers
										.createParameterizedCopy((WekaClassifierWrapper<FV>) parameterizedClassifier);
							else
								newClassifier = parameterizedClassifier;
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							newClassifier.train(newTrainingSet);
							expectedError += dist.getValueDistribution().get(label)
									* calculatelogloss(newClassifier.createClassificationResult(candidates));
						} catch (Exception e) {
						}
					}
			}
			ranking.add(new EntryWithComparableKey<>(expectedError, fv));
			remainingUncertainty += expectedError;
		}
		remainingUncertainty /= U;
		System.out.println("ExpectedLogLossReduction: remaining uncertainty = " + remainingUncertainty);
	}

	private Double calculatelogloss(IClassificationResult<FV> classificationResult) {
		double loss = 0.0;
		for (FV fv : candidates) {
			loss += Entropy.calculateEntropy(classificationResult.getLabelDistribution(fv).getValueDistribution());
		}
		return loss;
	}

	@Override
	public String getDescription() {
		return "Expected Log Loss Reduction";
	}

	@Override
	public String getName() {
		return "Expected Log Loss Reduction";
	}
}