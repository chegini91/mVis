package com.github.TKnudsen.DMandML.model.semiSupervised.activeLearning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import com.github.TKnudsen.ComplexDataObject.data.entry.EntryWithComparableKey;
import com.github.TKnudsen.ComplexDataObject.data.interfaces.IKeyValueProvider;
import com.github.TKnudsen.ComplexDataObject.data.interfaces.ISelfDescription;
import com.github.TKnudsen.ComplexDataObject.data.ranking.Ranking;
import com.github.TKnudsen.DMandML.data.classification.IClassificationResult;
import com.github.TKnudsen.DMandML.data.classification.IClassificationResultSupplier;
import com.github.TKnudsen.DMandML.model.supervised.classifier.use.IClassificationApplicationFunction;

/**
 * <p>
 * Title: AbstractActiveLearningModel
 * </p>
 * 
 * <p>
 * Abstract class for active learners. stores the candidata list as well as the
 * supplier of classification results.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.03
 */
public abstract class AbstractActiveLearningModel<FV extends IKeyValueProvider<Object>>
		implements IActiveLearningModelClassification<FV>, ISelfDescription {

	protected List<FV> candidates;

	private IClassificationResultSupplier<FV> classificationResultSupplier;

	private Function<List<? extends FV>, IClassificationResult<FV>> classificationApplicationFunction;

	protected Ranking<EntryWithComparableKey<Double, FV>> ranking;

	protected Map<FV, Double> queryApplicabilities;

	protected Double remainingUncertainty;

	protected AbstractActiveLearningModel() {

	}

	public AbstractActiveLearningModel(
			Function<List<? extends FV>, IClassificationResult<FV>> classificationApplicationFunction) {
		Objects.requireNonNull(classificationApplicationFunction,
				"Classification Application Function must not be null");

		this.classificationApplicationFunction = classificationApplicationFunction;
	}

	public AbstractActiveLearningModel(IClassificationApplicationFunction<FV> classificationApplicationFunction) {
		Objects.requireNonNull(classificationApplicationFunction,
				"Classification Application Function must not be null");

		this.classificationApplicationFunction = classificationApplicationFunction;
	}

	@Deprecated
	public AbstractActiveLearningModel(IClassificationResultSupplier<FV> classificationResultSupplier) {
		this.classificationResultSupplier = classificationResultSupplier;

		classificationApplicationFunction = null;
	}

	@Override
	public FV suggestCandidate() {
		List<FV> candidates = suggestCandidates(1);

		if (candidates != null && candidates.size() > 0)
			return candidates.get(0);

		return null;
	}

	@Override
	public List<FV> suggestCandidates(int count) {
		if (ranking == null) {
			calculateRanking();
		}

		List<FV> fvs = new ArrayList<>();
		for (int i = 0; i < count; i++)
			fvs.add(i, ranking.get(i).getValue());

		return fvs;
	}

	protected abstract void calculateRanking();

	/**
	 * getRanking should not be used to get the next candidates. Use
	 * {@link AbstractActiveLearningModel#suggestCandidate()} or
	 * {@link AbstractActiveLearningModel#suggestCandidates(int)} instead.
	 * 
	 * @return
	 */
	@Deprecated
	public Ranking<EntryWithComparableKey<Double, FV>> getRanking() {
		return ranking;
	}

	public List<FV> getLearningCandidates() {
		return this.candidates;
	}

	@Override
	public void setCandidates(List<? extends FV> candidates) {
		this.candidates = Collections.unmodifiableList(candidates);

		clearResults();
	}

	public void clearResults() {
		ranking = null;
		queryApplicabilities = null;
	}

	@Override
	public double getCandidateScore(FV featureVector) {
		if (queryApplicabilities == null && ranking != null)
			createQAfromRanking();
		if (queryApplicabilities != null)
			return queryApplicabilities.get(featureVector);

		return Double.NaN;
	}

	private void createQAfromRanking() {
		queryApplicabilities = new HashMap<>();
		for (EntryWithComparableKey<Double, FV> entry : ranking) {
			queryApplicabilities.put(entry.getValue(), -entry.getKey());
		}
	}

	/**
	 * copy of the applicability scores. high means applicable for AL.
	 * 
	 * @return
	 */
	public Map<FV, Double> getCandidateScores() {
		if (queryApplicabilities == null && ranking != null)
			createQAfromRanking();
		if (queryApplicabilities != null)
			return new LinkedHashMap<>(queryApplicabilities);

		return null;
	}

	@Override
	public double getRemainingUncertainty() {
		return remainingUncertainty;
	}

	@Override
	public String toString() {
		return this.getName();
	}

	@Override
	public IClassificationResultSupplier<FV> getClassificationResultSupplier() {
		return classificationResultSupplier;
	}

	public void setClassificationResultSupplier(IClassificationResultSupplier<FV> classificationResultSupplier) {
		this.classificationResultSupplier = classificationResultSupplier;
	}

	public Function<List<? extends FV>, IClassificationResult<FV>> getClassificationApplicationFunction() {
		return classificationApplicationFunction;
	}

	public void setClassificationApplicationFunction(Function<List<? extends FV>, IClassificationResult<FV>> classificationApplicationFunction) {
		this.classificationApplicationFunction = classificationApplicationFunction;
	}
}