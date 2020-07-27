package com.github.TKnudsen.DMandML.model.unsupervised.outliers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.TKnudsen.DMandML.data.outliers.IOutlierAnalysisResult;

/**
 * <p>
 * Title: OutlierAnalysisAlgorithm
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: (c) 2017-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.04
 */
public abstract class OutlierAnalysisAlgorithm<FV> implements IOutlierAnalysisAlgorithm<FV> {

	private List<? extends FV> featureVectors;

	protected IOutlierAnalysisResult<FV> outlierAnalysisResult;

	public OutlierAnalysisAlgorithm() {
	}

	public OutlierAnalysisAlgorithm(List<? extends FV> featureVectors) {
		setFeatureVectors(featureVectors);
	}

	public abstract void calculateOutlierAnalysisResult();

	public List<FV> getFeatureVectors() {
		return Collections.unmodifiableList(featureVectors);
	}

	public void setFeatureVectors(List<? extends FV> featureVectors) {
		this.featureVectors = new ArrayList<>(featureVectors);

		resetScores();
	}

	@Override
	/**
	 * @deprecated use getOutlierAnalysisResult()
	 */
	public double getOutlierScore(FV featureVector) {
		return getOutlierAnalysisResult().getOutlierScore(featureVector);
	}

	@Override
	public IOutlierAnalysisResult<FV> getOutlierAnalysisResult() {
		if (outlierAnalysisResult == null)
			calculateOutlierAnalysisResult();

		return outlierAnalysisResult;
	}

	@Override
	public void resetScores() {
		outlierAnalysisResult = null;
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

}
