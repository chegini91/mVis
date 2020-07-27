package com.github.TKnudsen.DMandML.model.evaluation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.IFeatureVectorObject;
import com.github.TKnudsen.DMandML.model.evaluation.performanceMeasure.IPerformanceMeasure;
import com.github.TKnudsen.DMandML.model.supervised.ILearningModel;
import com.github.TKnudsen.DMandML.model.supervised.classifier.Classifiers;

/**
 * <p>
 * Title: KFoldCrossValidation
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Jürgen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Christian Ritter, Juergen Bernard
 * @version 1.03
 * 
 */
public class KFoldCrossValidation<X extends IFeatureVectorObject<?, ?>, Y, L extends ILearningModel<X, Y>>
		extends AbstractModelEvaluation<X, Y, L> {

	private int k;
	private boolean shuffle = false;

	public KFoldCrossValidation(int k) {
		this(null, k, false);
	}

	public KFoldCrossValidation(List<? extends IPerformanceMeasure<Y>> performanceMeasures, int k) {
		this(performanceMeasures, k, false);
	}

	public KFoldCrossValidation(List<? extends IPerformanceMeasure<Y>> performanceMeasures, int k, boolean shuffle) {
		super(performanceMeasures);
		this.k = k;
		this.shuffle = shuffle;
	}

	@Override
	public void evaluate(L learner, List<X> featureVectors, List<Y> groundTruth) {
		if (learner == null)
			throw new IllegalArgumentException("Learning Model must not be null");
		if (featureVectors == null || groundTruth == null || featureVectors.size() != groundTruth.size())
			throw new IllegalArgumentException("Lists are null or of unequal size!");

		if (shuffle)
			Collections.shuffle(featureVectors);

		performanceValues = new HashMap<>();
		for (IPerformanceMeasure<Y> pm : getPerformanceMeasures()) {
			performanceValues.put(pm, new ArrayList<>());
		}

		List<List<X>> groups = new ArrayList<>();
		List<List<Y>> truth = new ArrayList<>();
		double w = 1.0 * featureVectors.size() / k;
		for (int i = 0; i < k; i++) {
			groups.add(featureVectors.subList(((int) Math.round(i * w)), ((int) Math.round((i + 1) * w))));
			truth.add(groundTruth.subList(((int) Math.round(i * w)), ((int) Math.round((i + 1) * w))));
		}
		for (int i = 0; i < k; i++) {
			List<X> trainset = new ArrayList<>();
			List<X> testset = new ArrayList<>();
			List<Y> trainTruth = new ArrayList<>();
			List<Y> testTruth = new ArrayList<>();
			for (int j = 0; j < groups.size(); j++) {
				if (j == i) {
					testset.addAll(groups.get(j));
					testTruth.addAll(truth.get(j));
				} else {
					trainset.addAll(groups.get(j));
					trainTruth.addAll(truth.get(j));
				}
			}

			Classifiers.setAttribute("class", trainset, trainTruth);

			learner.train(trainset);
			calculatePerformances(learner.test(testset), testTruth);
		}
	}

	@Override
	protected void initDefaultPerformanceMeasures() {
		throw new UnsupportedOperationException(
				"KFoldCrossValidation: Empty performance measures are not supported yet.");
	}

	@Override
	protected Double cumulate(List<Double> values) {
		return values.stream().reduce(0.0, (x, y) -> x + y) / values.size();
	}

	@Override
	public String getName() {
		return "k-fold Cross Validation";
	}

	@Override
	public String getDescription() {
		return "Splits the data set into k subsets and evaluates k times using each subset as training set once.";
	}
}
