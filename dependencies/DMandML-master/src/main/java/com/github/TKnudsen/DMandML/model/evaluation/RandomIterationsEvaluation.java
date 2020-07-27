package com.github.TKnudsen.DMandML.model.evaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.TKnudsen.ComplexDataObject.data.features.Feature;
import com.github.TKnudsen.ComplexDataObject.data.interfaces.IFeatureVectorObject;
import com.github.TKnudsen.DMandML.model.evaluation.performanceMeasure.IPerformanceMeasure;
import com.github.TKnudsen.DMandML.model.supervised.ILearningModel;
import com.github.TKnudsen.DMandML.model.supervised.classifier.Classifiers;

/**
 * <p>
 * Title: RandomIterationsEvaluation
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Christian Ritter, Juergen Bernard
 * @version 1.04
 */
public class RandomIterationsEvaluation<O, X extends IFeatureVectorObject<O, ? extends Feature<O>>, Y, L extends ILearningModel<X, Y>>
		extends AbstractModelEvaluation<X, Y, L> {

	private int iterations;
	private double split = 0.66;
	private List<X> trainset;
	private List<X> testset;
	private List<Y> trainTruth;
	private List<Y> testTruth;

	public RandomIterationsEvaluation(List<? extends IPerformanceMeasure<Y>> performanceMeasures, int iterations) {
		super(performanceMeasures);
		this.iterations = iterations;
	}

	public RandomIterationsEvaluation(List<? extends IPerformanceMeasure<Y>> performanceMeasures, int iterations,
			double split) {
		super(performanceMeasures);
		this.iterations = iterations;
		this.split = split;
	}

	@Override
	public void evaluate(L learner, List<X> featureVectors, List<Y> groundTruth) {
		if (learner == null)
			throw new IllegalArgumentException("Learning Model must not be null");
		if (featureVectors == null || groundTruth == null || featureVectors.size() != groundTruth.size())
			throw new IllegalArgumentException("Lists are null or of unequal size!");

		performanceValues = new HashMap<>();
		for (IPerformanceMeasure<Y> pm : getPerformanceMeasures()) {
			performanceValues.put(pm, new ArrayList<>());
		}

		for (int i = 0; i < iterations; i++) {
			trainset = new ArrayList<>();
			testset = new ArrayList<>();
			trainTruth = new ArrayList<>();
			testTruth = new ArrayList<>();
			calcRandomTrainAndTestSets(featureVectors, groundTruth);
			Classifiers.setAttribute("class", trainset, trainTruth);
			
			learner.train(trainset);
			calculatePerformances(learner.test(testset), testTruth);
		}
	}

	private void calcRandomTrainAndTestSets(List<X> featureVectors, List<Y> groundTruth) {
		trainset = new ArrayList<>();
		testset = new ArrayList<>();
		trainTruth = new ArrayList<>();
		testTruth = new ArrayList<>();
		List<Integer> indices = new ArrayList<>();
		for (int i = 0; i < featureVectors.size(); i++) {
			indices.add(i);
		}
		int s = (int) Math.round(split * featureVectors.size());
		while (trainset.size() <= s) {
			int r = (int) (Math.random() * indices.size());
			int ind = indices.remove(r);
			trainset.add(featureVectors.get(ind));
			trainTruth.add(groundTruth.get(ind));
		}
		// a bit sloppy. check whether all possible labels are covered in a
		// classification task
		if (groundTruth.get(0) instanceof String) {
			Set<Y> all = new HashSet<>(groundTruth);
			Set<Y> train = new HashSet<>(trainTruth);
			if (all.size() != train.size()) {
				calcRandomTrainAndTestSets(featureVectors, groundTruth);
			}
		}

		for (int i : indices) {
			testset.add(featureVectors.get(i));
			testTruth.add(groundTruth.get(i));
		}
	}

	@Override
	protected void initDefaultPerformanceMeasures() {
		throw new UnsupportedOperationException(
				"RandomIterationsEvaluation: Empty performance measures are not supported yet.");
	}

	@Override
	protected Double cumulate(List<Double> values) {
		return values.stream().reduce(0.0, (x, y) -> x + y) / values.size();
	}

	@Override
	public String getName() {
		return "Random Iterations Evaluation";
	}

	@Override
	public String getDescription() {
		return "Performs several iterations of evaluation with random training instances chosen at each iteration.";
	}

}